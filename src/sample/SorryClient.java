package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceNodeSetData;
import javafx.application.Platform;
import javafx.util.Pair;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import sample.card.Card;


class SorryClient  {


    Socket connection;
    static DataOutputStream out;
    static BufferedReader in;
    static GameLogic game;
    static boolean gameStarted;
    static boolean isTurn;
    static boolean gameWon;
    static boolean turnBegin = false;
    static String user;
    String game_name;
    String color;
    static Controller controller;

    public SorryClient(Controller controller){
        this.controller = controller;
        game = new GameLogic();
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
            messageHandler m = new messageHandler(connection);
            Thread t = new Thread(m);
            t.start();
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
        if(gameWon)
            return "The game has ended";
        try {
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            user = name;
            json.put("command","register_user");
            data.put("username", name);
            json.put("data", data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();

            return "Register successful";
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
     void get_game_list(){
         try {
            Thread.sleep(100);
            JSONObject json = new JSONObject();
            JSONObject response = new JSONObject();
            JSONParser parser = new JSONParser();
            json.put("command","get_game_list");
            json.put("data","{}");
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();

        } catch (Exception e){
            return;
        }
    }

    /**
     * @param color The color the user desires to be
     * @param name user name
     * @return string stating if user has successfully joined the game
     */
    void join_game(String color, String name){
        if (gameStarted)
            return;
        try {
            Thread.sleep(100);
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","join_game");
            data.put("color", color);
            data.put("name", name);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * @param name The name of the game to be created
     * @param color The desired color of the host
     * @return response from server
     */
    void create_game(String name, String color){
        if(gameStarted)
            return;
        try{
            Thread.sleep(100);
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

            isTurn = true;

        } catch (Exception e){
            e.printStackTrace();

        }
    }

    /**
     * @param name The name of the game to get data from
     * @return String representing the JSON data of the currently running game
     */
    static void get_game_data(String name){
        if(gameWon)
            return;
        try{
            Thread.sleep(100);
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            JSONParser parser = new JSONParser();
            json.put("command","get_game_data");
            data.put("name", name);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();

        } catch (Exception e){
            e.printStackTrace();

        }
    }

    /**
     * @param game The game to update
     * @param pawn The pawn to move
     * @param position The pawn's position to be moved to
     * @param end True if turn is over, false if the turn is continuing
     * @return The color of the next player
     */
    void update_pawn(String game, String pawn, String position, boolean end){
        if(gameWon)
            return;
        if(!isTurn)
            return;
        try{
            Thread.sleep(100);
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

        } catch (Exception e){
            e.printStackTrace();
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
            Thread.sleep(100);
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            json.put("command","start_game");
            data.put("game", game);
            json.put("data",data);
            byte[] output = json.toString().getBytes();
            out.write(output);
            out.flush();
            gameStarted = true;

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

    static void updateClient(ArrayList updates){
        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(updates);
        controller.updateClient(updates);
        //updates.clear();;
    }

    static void setPlayerTurn(String name){
        controller.setPlayerTurn(name);
    }

    static void setGameList(String games){
        controller.setGamesList(games);
    }



}

class messageHandler implements Runnable{

    Socket connection;
    DataOutputStream out;
    BufferedReader in;
    JSONObject game;

    messageHandler(Socket connection){
        try {
            this.connection = connection;
            out = new DataOutputStream(connection.getOutputStream());
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        ArrayList<String> updates = new ArrayList<>();
        String temp = "";
        while(true){
            try {
                JSONObject object;
                JSONParser parser = new JSONParser();
                object = (JSONObject) parser.parse(in.readLine());
                if(object.containsValue("game_list")) {
                    String games = object.get("data").toString();
                    System.out.println("Sorry Client: "+games);
                    SorryClient.setGameList(games);
                }
                if (object.containsValue("pawn_updated")) {
                    System.out.println("Updated!");

                    JSONObject data = (JSONObject)object.get("data");

                     if(!updates.contains(data.toString()))
                        updates.add(data.toString());

                }
                if(object.containsValue("next_turn")){

                    JSONObject data = (JSONObject)object.get("data");
                    System.out.println(data.toString());
                    String next_player = data.get("player").toString();
                    SorryClient.setPlayerTurn(next_player);

                    if (data.get("player").toString().equals(SorryClient.user)) {
                        SorryClient.isTurn = true;
                        SorryClient.turnBegin = true;

                    } else{

                        SorryClient.isTurn = false;
                        SorryClient.turnBegin = false;
                    }
                }
                if(!SorryClient.isTurn || !SorryClient.turnBegin) {
                    SorryClient.updateClient(updates);
                    Thread.sleep(500);
                    updates.clear();
                }
                if(SorryClient.isTurn) {

                }else {
                    System.out.println("Not my turn");
                    System.out.println(updates);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

class Game {

    public static void main(String[] args) throws Exception {

        SorryClient sorry = new SorryClient();

        sorry.register_user("Tanner");
        sorry.create_game("game", "blue");
        sorry.get_game_list();
    }
}
