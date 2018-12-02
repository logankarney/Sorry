package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.*;
import sample.card.Card;

class SorryClient{


    private Socket connection;
    private DataOutputStream out;
    private BufferedReader in;
    private GameLogic game;

    SorryClient(){
        game = new GameLogic();
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
            String response = in.readLine();
            System.out.println(response);
            return response;
        } catch (Exception e){
            e.printStackTrace();
            return "error";
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
            return response;
        } catch (Exception e){
            e.printStackTrace();
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
            String response = in.readLine();
            System.out.println(response);
            return response;
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
            String response = in.readLine();
            System.out.println(response);
            return response;
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
            System.out.println(response);
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
            return response;
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
        sorry.register_user("lol");
        sorry.get_game_list();
        sorry.join_game("blue","test");
        sorry.create_game("test","blue");
    }

}

