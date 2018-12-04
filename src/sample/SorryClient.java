package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javafx.util.Pair;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import sample.card.Card;


class SorryClient{


    Socket connection;
    DataOutputStream out;
    BufferedReader in;
    static GameLogic game;
    String user;
    String game_name;
    String color;
    private static Controller controller;

    public SorryClient(Controller controller){
        this.controller = controller;
        game = new GameLogic();
    }


    SorryClient(){
        game = new GameLogic();
    }

    public GameLogic getGame() {
        return game;
    }

    String connect(InetAddress addr, int port){
       try {
           connection = new Socket(addr, port);
           out = new DataOutputStream(connection.getOutputStream());
           in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           Thread t = new Thread(new messageHandler(addr,port));
           t.start();
           return "Connection established";
       } catch (Exception e){
           return "No server running at specified port or address.";
       }

    }

     String register_user(String name){
        try {
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","register_user");
            data.put("username", name);
            json.put("data", data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject)parser.parse(in.readLine());
            if(response.containsKey("error")){
                System.out.println("Something has gone wrong...");
                return "Error: "+response.get("error");
            } else{
                JSONObject response_data = (JSONObject)response.get("data");
                //System.out.println("Success! Player "+response_data.get("username"));
                user = response_data.get("username").toString();
                return user+" has been successfully registered with the server.";
            }
        } catch (Exception e){
            e.printStackTrace();
            return "Error registering user.";
        }
    }

     String get_game_list(){
        try {
            JSONObject json = new JSONObject();
            json.put("command","get_game_list");
            json.put("data","{}");
            byte[] output = json.toString().getBytes();
            out.write(output);
            String response = in.readLine();
         //   System.out.println(response);
            JSONParser parser = new JSONParser();
            JSONObject resp_json = (JSONObject)parser.parse(response);
            JSONObject games = (JSONObject)resp_json.get("data");
            return games.toString();

        } catch (Exception e){
            //e.printStackTrace();
            return "error";
        }
     }

     String join_game(String color, String name){
        try {
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","join_game");
            data.put("color", color);
            data.put("name", name);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            JSONParser parser = new JSONParser();
            JSONObject player_joined = (JSONObject)parser.parse(in.readLine());
            JSONObject game_data = (JSONObject)parser.parse(in.readLine());
            //  System.out.println(game_data);
            if(player_joined.containsKey("error") || game_data.containsKey("error")){
                System.out.println("Something has gone wrong...");
                return player_joined.toString()+" "+game_data.toString();
            } else{
            //    System.out.println(player_joined.toString());
                JSONArray player_joined_array = (JSONArray) player_joined.get("data");
                JSONObject player_joined_data = (JSONObject)player_joined_array.get(0);
                game_name = player_joined_data.get("game").toString();
                this.color = player_joined_data.get("color").toString().toUpperCase();
                game_data = (JSONObject)game_data.get("data");
                JSONObject players = (JSONObject)game_data.get("players");
                for(Object s: players.keySet()){
                    String player = s.toString();
                    String temp_color = players.get(s).toString().toUpperCase();
                    game.addPlayer(new Player(player,TileColor.valueOf(temp_color)));
                }
                //System.out.println("Success! Player "+player_joined.get("username"));
                // user = player_joined.get("username").toString();
            }
            return user+" has successfully joined "+game_name+" and has been assigned "+color;
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

     }

     String create_game(String name, String color){
        try{
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","create_game");
            data.put("color", color);
            data.put("name", name);
            json.put("data", data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            JSONParser parser = new JSONParser();
            JSONObject player_joined = (JSONObject)parser.parse(in.readLine());
            JSONObject game_data = (JSONObject)parser.parse(in.readLine());
          //  System.out.println(game_data);
            if(player_joined.containsKey("error")){
                System.out.println("Something has gone wrong...");
            } else{
                System.out.println(player_joined.toString());
                JSONArray player_joined_array = (JSONArray) player_joined.get("data");
                JSONObject player_joined_data = (JSONObject)player_joined_array.get(0);
                game_name = player_joined_data.get("game").toString();
                color = player_joined_data.get("color").toString().toUpperCase();
                name = player_joined_data.get("name").toString();
                game_data = (JSONObject)game_data.get("data");
                JSONObject players = (JSONObject)game_data.get("players");
                for(Object s: players.keySet()){
                    String player = s.toString();
                    String temp_color = players.get(s).toString().toUpperCase();
                    game.addPlayer(new Player(player,TileColor.valueOf(temp_color)));
                }
                //System.out.println("Success! Player "+player_joined.get("username"));
               // user = player_joined.get("username").toString();
            }
            return game_name+" has been successfully created.";
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    String get_game_data(String name){
        try{
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            JSONParser parser = new JSONParser();
            json.put("command","get_game_data");
            data.put("name", name);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            JSONObject response = null;
            while(true){
                response = (JSONObject)parser.parse(in.readLine());
                if(response.containsValue("game_data")){
                    break;
                }
            }
            return response.toString();
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    String update_pawn(String game, String pawn, String position, boolean end, Board b){
        try{
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","update_pawn");
            data.put("game", game);
            data.put("pawn", pawn);
            data.put("new_position", position);
            data.put("turn_ends", end);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
            JSONObject gamedata = new JSONObject();
            gamedata.put("command","get_game_data");
            JSONObject gamedata_data = new JSONObject();
            gamedata_data.put("name",game);
            gamedata.put("data",gamedata_data);
            output = gamedata.toString().getBytes();
            out.write(output);
            JSONObject response = null;
            JSONParser parser = new JSONParser();
            while(true){
                response = (JSONObject)parser.parse(in.readLine());
                if(response.containsKey("game_data"))
                    break;
            }
            JSONObject response_data = (JSONObject) response.get("data");
            System.out.println(response.toString());
            JSONObject players_json = (JSONObject) parser.parse(response_data.get("players").toString());
            //System.out.println(response);
            Player[] players = new Player[players_json.size()];
            int i = 0;
            for(Object s:players_json.keySet()){
                String player = s.toString();
                String temp_color = players_json.get(s).toString().toUpperCase();
                players[i] = new Player(player,TileColor.valueOf(temp_color));
                i++;
            }
            Player[] temp_players = b.getPlayers();
            Player temp = null;
            for(i = 0; i < temp_players.length; i++){
                if(temp_players[i].getName() == user){
                    temp = temp_players[i];
                    break;
                }
            }
            this.game.saveMove(b);
       //     String response = in.readLine();
            System.out.println(response);
            return "updated";
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    String start_game(String game){
        try{
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","start_game");
            data.put("game", game);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
            JSONObject game_json = new JSONObject();
            game_json.put("command","get_game_data");
            game_json.put("data","");
            output = json.toString().getBytes();
            System.out.println(game_json);
            out.write(output);
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject)parser.parse(get_game_data(game));
            //System.out.println(response);
            JSONObject game_data = (JSONObject)response.get("data");
         //   JSONObject current_Game = (JSONObject)
        //    System.out.println(response);
            JSONObject players = (JSONObject)game_data.get("players");
            System.out.println(players);
            for(Object s:players.keySet()){
                String name = s.toString();
                String color = players.get(name).toString().toUpperCase();
                System.out.println(name+":"+color);
                this.game.addPlayer(new Player(name,TileColor.valueOf(color)));
            }
            this.game.startGame();
            return game_name+" has begun.";
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    Card drawCard(){
        return game.drawCard();
    }

    Player getPlayer(){
        return new Player(user,TileColor.valueOf(color));
    }

    Board getBoard(){
        return game.getBoard();
    }

    void setBoard(Board b){
        game.saveMove(b);
    }
}

class messageHandler implements Runnable{

    SorryClient game;
    Socket connection;
    DataOutputStream out;
    BufferedReader in;

    messageHandler(InetAddress addr, int port){
        try {
            connection = new Socket(addr,port);
            out = new DataOutputStream(connection.getOutputStream());
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            while (true) {
                System.out.println("here");
                JSONParser parser = new JSONParser();
                String input = in.readLine();
                System.out.println(input);
                JSONObject json = (JSONObject)parser.parse(input);
                if (json.containsKey("pawn_updated")){
                    System.out.println("update");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}

class Game{

    public static void main(String[] args) throws Exception{
        SorryClient sorry = new SorryClient();
        sorry.connect(InetAddress.getByName("127.0.0.1") ,12000);
        sorry.register_user("Tanner");
        sorry.create_game("game","blue");
     //   System.out.println(sorry.get_game_data("game"));
        while(true){
        }
       // System.out.println(sorry.join_game("green","what"));
        /*sorry.register_user("lol");
        sorry.get_game_list();
        sorry.join_game("blue","test");
        sorry.create_game("test","blue");*/
    }

}

class Game2{
    public static void main(String[] args) throws Exception{
        SorryClient sorry = new SorryClient();
        sorry.connect(InetAddress.getByName("127.0.0.1") ,12000);
        sorry.register_user("lol");
        System.out.println(sorry.get_game_list());
        System.out.println(sorry.join_game("green","game"));
        System.out.println(sorry.start_game("game"));
        Card c = sorry.drawCard();
        System.out.println(c.getValue()+":"+c.getDesc());
        System.out.println(sorry.getBoard().getPlayers()[0].getName());
        System.out.println(sorry.getBoard().getPlayers()[2].getName());
        Player temp = sorry.getBoard().getPlayers()[0];
        Board temp_board = sorry.getBoard();
        temp_board.moveFromStart(temp.getPawns()[0]);
        sorry.setBoard(temp_board);
        String pos = temp.getPawns()[0].getColor().toString().substring(0,1)+temp.getPawns()[0].getSpace();
        System.out.println(sorry.update_pawn("game","G1",pos,true,temp_board));
        while(true){
        }
     /*   c  = sorry.drawCard();
        System.out.println(c.getValue()+":"+c.getDesc());
        temp = sorry.getBoard().getPlayers()[0];
        temp_board = sorry.getBoard();
        ArrayList<Board> moves = c.getMoves(temp,temp_board);
        temp_board = moves.get(0);
        System.out.println(moves.toString());
        sorry.setBoard(temp_board);

        //sorry.update_pawn("game","B3","B3",true,moves.get(0));
        //System.out.println(sorry.get_game_data("game"));
        while(true){}
        /*sorry.register_user("lol");
        sorry.get_game_list();
        sorry.join_game("blue","test");
        sorry.create_game("test","blue");*/
    }

}
