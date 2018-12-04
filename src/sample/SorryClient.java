package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import javafx.util.Pair;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import sample.card.Card;


class SorryClient{


    private Socket connection;
    private DataOutputStream out;
    private BufferedReader in;
    private GameLogic game;
    private String user;
    private String game_name;
    private String color;
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
           return "Connection established.";
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
            System.out.println(response);
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
                // System.out.println(player_joined.toString());
                JSONArray player_joined_array = (JSONArray) player_joined.get("data");
                JSONObject player_joined_data = (JSONObject)player_joined_array.get(0);
                game_name = player_joined_data.get("game").toString();
                color = player_joined_data.get("color").toString().toUpperCase();
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
            return name+" has successfully joined "+game_name+" and has been assigned "+color;
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
               // System.out.println(player_joined.toString());
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
            json.put("command","get_game_data");
            data.put("name", name);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            String response = in.readLine();
    //        System.out.println(response);
            return response;
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    String update_pawn(String game, String pawn, String position, boolean end){
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
            String response = in.readLine();
            System.out.println(response);
            return response;
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
            String response = in.readLine();
            System.out.println(response);
            return game_name+" has begun.";
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
/*
    private void generateBoard(){

        String[] color = new String[4];
        color[0] = "red";
        color[1] = "blue";
        color[2] = "yellow";
        color[3] = "green";
        for(int j = 0; j < 4; j++){

            for(int k = 0; k < 15; k++){
                board.add(new Space(false,color[j],"",k));
            }
            homes.add(new ArrayList());
            for(int k = 16; k < 22; k++){
                homes.get(j).add(new Space(false,color[j],"",k));
            }

        }

    }

    static Card drawCard(){
        Card temp = deck.get(deck.size()-1);
        deck.remove(temp);
        discard.add(temp);
        return temp;
    }

}

class Space{
    boolean slide;
    String color;
    String player;
    int position;


    Space(boolean slide, String color, String player, int position){

        this.slide = slide;
        this.color = color;
        this.player = player;
        this.position = position;

    }

    String getColor(){
        return color;
    }


    public boolean isSlide() {
        return slide;
    }

    public void setSlide(boolean slide) {
        this.slide = slide;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    */
}

class Game{

    public static void main(String[] args) throws Exception{
        SorryClient sorry = new SorryClient();
        System.out.println(sorry.connect(InetAddress.getByName("127.0.0.1") ,12000));
        System.out.println(sorry.register_user("Tanner"));
        System.out.println(sorry.create_game("game","blue"));
        System.out.println(sorry.get_game_data("game"));
        while(true){}
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
        System.out.println(sorry.connect(InetAddress.getByName("127.0.0.1") ,12000));
        sorry.register_user("lol");
        System.out.println(sorry.get_game_list());
        System.out.println(sorry.join_game("green","game"));
        sorry.start_game("game");
        System.out.println(sorry.get_game_data("game"));
        while(true){}
        /*sorry.register_user("lol");
        sorry.get_game_list();
        sorry.join_game("blue","test");
        sorry.create_game("test","blue");*/
    }

}
