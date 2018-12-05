package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceNodeSetData;
import javafx.util.Pair;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import sample.card.Card;


class SorryClient implements Runnable {


    Socket connection;
    DataOutputStream out;
    BufferedReader in;
    static GameLogic game;
    boolean gameStarted;
    boolean isTurn;
    boolean gameWon;
    String user;
    String game_name;
    String color;
    private static Controller controller;

    public SorryClient(Controller controller){
        this.controller = controller;
        game = new GameLogic();
    }

    public void run(){
        while(true){
          try {
              JSONObject object = new JSONObject();
              JSONParser parser = new JSONParser();
              object = (JSONObject) parser.parse(in.readLine());
              if (object.containsValue("pawn_updated")) {
                  System.out.println("Updated!");
                  System.out.println(object.toString());
                  JSONObject data = (JSONObject)object.get("data");
                  /*if (data.get("player").toString().equals(user)) {
                      isTurn = true;
                  }*/
              }
              if(object.containsValue("next_turn")){
                  System.out.println(object.toString());
                  JSONObject data = (JSONObject)object.get("data");
                  System.out.get
                  if (data.get("player").toString().equals(user)) {
                      isTurn = true;
                  } else{
                      isTurn = false;
                  }
              }
              if(isTurn)
                  System.out.println("My turn");
              else
                  System.out.println("Not my turn");
          } catch (Exception e){
              e.printStackTrace();
          }
        }
    }

    SorryClient(){
        game = new GameLogic();
        gameStarted = false;
        isTurn = false;
    }

    public GameLogic getGame() {
        return game;
    }

    /**
     *
     * @param addr The address of the server to connect to
     * @param port Port number the server is listening at
     *
     * @return "Connection established"
     */
    String connect(InetAddress addr, int port){
       try {
           connection = new Socket(addr, port);
           out = new DataOutputStream(connection.getOutputStream());
           in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           //Passes off socket to a thread to listen for updates from server
          // messageHandler m = new messageHandler(addr,port,this);
         //  Thread t = new Thread(m);
         //  t.start();
           return "Connection established";
       } catch (Exception e){
           return "No server running at specified port or address.";
       }

    }

    /**
     * Adds a user to the server
     *
     * @param name The username to be registered with the server
     * @return response from server
     */
    String register_user(String name){
        try {
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","register_user");
            data.put("username", name);
            json.put("data", data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
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

    /**
     * Gets a list of currently running games on the server
     *
     * @return a list of currently running games on the server
     */
     String get_game_list(){
         if(!isTurn)
             return "Not your turn";
         try {
            JSONObject json = new JSONObject();
            JSONObject response = new JSONObject();
            JSONParser parser = new JSONParser();
            json.put("command","get_game_list");
            json.put("data","{}");
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
            while(true) {
                response = (JSONObject) parser.parse(in.readLine());
                if (response.containsValue("game_list"))
                    break;
            }
            JSONObject resp_data = (JSONObject)response.get("data");
            JSONArray games = (JSONArray)resp_data.get("games");
            JSONObject return_object = new JSONObject();
            ArrayList<String> colors = new ArrayList<>();
            for(int i = 0; i < games.size(); i++){
                JSONObject game = (JSONObject)games.get(i);
                for(Object g:game.keySet()){
                    JSONObject players = (JSONObject)game.get(g.toString());
                    for(Object h:players.keySet()){
                        colors = new ArrayList<>();
                        JSONObject player_data = (JSONObject)players.get(h.toString());
                        for(Object j: player_data.keySet()){
                            String color = player_data.get(j.toString()).toString();
                            colors.add(color);
                        }
                    }
                    return_object.put(g.toString(),colors);
                }
            }
            /*Set<String> colors = new HashSet<>();
            for(Object g:games){
                JSONObject temp = (JSONObject)g;
                for(Object h:temp.keySet()){
                    colors.clear();
                    String temp_string = h.toString();
                    JSONObject temp_players = (JSONObject)temp.get(temp_string);
                    for(Object i:temp_players.keySet()){
                        String temp_temp_string = i.toString();//lol
                        JSONObject temp_player_data = (JSONObject)temp_players.get(temp_temp_string);
                        for(Object j:temp_player_data.keySet()){
                            String temp_player = temp_player_data.get(j).toString();
                            colors.add(temp_player);
                            return_object.put(h.toString(),colors);
                        }
                    }
                }
            }*/
            return return_object.toString();

        } catch (Exception e){
            return "error";
        }
     }

    /**
     * @param color The color the user desires to be
     * @param name user name
     * @return string stating if user has successfully joined the game
     */
     String join_game(String color, String name){
         if (gameStarted)
             return "Game is already in session";
         try {
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","join_game");
            data.put("color", color);
            data.put("name", name);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
            JSONParser parser = new JSONParser();
            JSONObject player_joined = (JSONObject)parser.parse(in.readLine());
            JSONObject game_data = (JSONObject)parser.parse(in.readLine());
            if(player_joined.containsKey("error") || game_data.containsKey("error")){
                System.out.println("Something has gone wrong...");
                return player_joined.toString()+" "+game_data.toString();
            } else{
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

            }
            return user+" has successfully joined "+game_name+" and has been assigned "+color;
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

     }

    /**
     * @param name The name of the game to be created
     * @param color The desired color of the host
     * @return response from server
     */
     String create_game(String name, String color){
         if(gameStarted)
             return "Game is already in session";
         try{
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            JSONParser parser = new JSONParser();
            json.put("command","create_game");
            data.put("color", color);
            data.put("name", name);
            json.put("data", data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
            JSONObject response = (JSONObject)parser.parse(in.readLine());
            isTurn = true;
            return response.toString();
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * @param name The name of the game to get data from
     * @return String representing the JSON data of the currently running game
     */
    String get_game_data(String name){
        try{
            out.flush();
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            JSONParser parser = new JSONParser();
            json.put("command","get_game_data");
            data.put("name", name);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
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

    /**
     * @param game The game to update
     * @param pawn The pawn to move
     * @param position The pawn's position to be moved to
     * @param end True if turn is over, false if the turn is continuing
     * @return The color of the next player
     */
    String update_pawn(String game, String pawn, String position, boolean end){
        if(!isTurn)
            return "";
        try{
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            JSONObject response;
            JSONParser parser = new JSONParser();
            json.put("command","update_pawn");
            data.put("game", game);
            data.put("pawn", pawn);
            data.put("new_position", position);
            data.put("turn_ends", end);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
            while (true) {
                response = (JSONObject) parser.parse(in.readLine());
                if(response.containsValue("next_turn"))
                    break;
            }
            JSONObject response_data = (JSONObject)response.get("data");
            String player = response_data.get("player").toString();
            json = new JSONObject();
            data = new JSONObject();
            json.put("command","get_game_data");
            data.put("name",game);
            json.put("data",data);
            output = json.toString().getBytes();
            out.write(output);
            out.flush();
            while (true) {
                response = (JSONObject) parser.parse(in.readLine());
                if(response.containsValue("game_data"))
                    break;
            }
            response_data = (JSONObject)response.get("data");
            JSONObject players = (JSONObject)response_data.get("players");
            return players.get(player).toString().toUpperCase();
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * @param game The name of the game to start
     * @return String stating whether the game has begun or not.
     */
    String start_game(String game){
        if(gameStarted)
            return "Game is already in session";
        try{
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","start_game");
            data.put("game", game);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
            gameStarted = true;
            //this.game.startGame();
            return game_name+" has begun.";
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * @return Card from deck
     */
    Card drawCard(){
        return game.drawCard();
    }

    Player getPlayer(){
        return new Player(user,TileColor.valueOf(color));
    }

    void setGameWon(boolean won){
        gameWon = won;
    }

}

/*class messageHandler implements Runnable{

    SorryClient game;
    Socket connection;
    DataOutputStream out;
    BufferedReader in;

    messageHandler(InetAddress addr, int port,SorryClient game){
        try {
            connection = new Socket(addr,port);
            out = new DataOutputStream(connection.getOutputStream());
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.game = game;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            game.update_pawn("game","B1","B11",true);
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
}*/

class Game{

    public static void main(String[] args) throws Exception{

        SorryClient sorry = new SorryClient();
        sorry.connect(InetAddress.getByName("127.0.0.1") ,12000);
        sorry.register_user("Tanner");
        sorry.create_game("game","blue");
        Thread t = new Thread(sorry);
        t.start();
        Thread.sleep(10000);
        sorry.update_pawn("game","B1","B11",true);
        while(true){}

    }

}

class Game2{
    public static void main(String[] args) throws Exception{
        SorryClient sorry = new SorryClient();
        sorry.connect(InetAddress.getByName("127.0.0.1") ,12000);
        sorry.register_user("lol");
        sorry.join_game("red","game");
        sorry.start_game("game");
        Thread t = new Thread(sorry);
        t.start();
 //       sorry.get_game_data("game");
//        sorry.get_game_list();
     //   sorry.update_pawn("game","B1","B11",true);
        while(true){}
    }
}



