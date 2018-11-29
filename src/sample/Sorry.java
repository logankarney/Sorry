package sample;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

class Sorry{


    private static ArrayList<Card> deck;
    private static ArrayList<Card> discard;
    private static ArrayList<Space> board;
    private static ArrayList<ArrayList <Space>> homes;
    private Socket connection;


    public void joinGame(String addr, int port, String color, String name){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Sorry(){

        deck = new ArrayList<>();
        discard = new ArrayList<>();
        board = new ArrayList<>();
        homes = new ArrayList<>();
        generateBoard();
        generateDeck();

    }

    private void generateDeck(){

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

    }

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

    public static Card drawCard(){
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


    public Space(boolean slide, String color, String player, int position){

        this.slide = slide;
        this.color = color;
        this.player = player;
        this.position = position;

    }

    public String getColor(){
        return color;
    }

}

class Game{

    public static void main(String[] args) throws Exception{
        Sorry sorry = new Sorry();
        sorry.joinGame("127.0.0.1",12000,"blue","lol");
    }

}

