package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

class Sorry{


    public static ArrayList<Card> deck;
    private static ArrayList<Card> discard;
    public static ArrayList<Object> board;
    private Socket connection;


    public void joinGame(String addr, int port, String color, String name){
        try {
            connection = new Socket(InetAddress.getByName(addr), port);
            JSONObject command = new JSONObject();
            JSONObject data = new JSONObject();
            command.put("command", "register_user");
            data.put("username", '['+name+']');
            command.put("data", data);
            System.out.println(command.toString());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output_string = command.toString();
            byte[] output = output_string.getBytes();
            out.write(output);
            System.out.println(in.readLine());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void generateDeck(){

        for(int i = 0; i < 5; i++){
            deck.add(new Card(1));
            if(i < 4) {
                deck.add(new Card(2));
                deck.add(new Card(3));
                deck.add(new Card(4));
                deck.add(new Card(5));
                deck.add(new Card(7));
                deck.add(new Card(8));
                deck.add(new Card(10));
                deck.add(new Card(11));
                deck.add(new Card(12));
                deck.add(new Card(13));
            }
        }
        Collections.shuffle(deck);
        //  System.out.println(deck.toString());

    }

    public void generateBoard(){



    }

    public static Card drawCard(){
        Card temp = deck.get(deck.size()-1);
        deck.remove(temp);
        discard.add(temp);
        return temp;
    }

}

class Game{

    public static void main(String[] args) throws Exception{
        Sorry sorry = new Sorry();
        sorry.joinGame("127.0.0.1",12000,"blue","lol");
    }

}

