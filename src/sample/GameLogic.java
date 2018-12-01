package sample;

import sample.card.Card;

import java.util.ArrayList;

public class GameLogic{
    private ArrayList<Player> players;

    public GameLogic(){
        players = new ArrayList();

    }

    /**
     * Add a player to the game
     * */
    public void addPlayer(Player player){
        players.add(player);
    }






}