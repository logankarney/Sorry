package sample;

import sample.card.*;

import java.util.ArrayList;

public class GameLogic{
    private ArrayList<Player> players;
    private Board board;
    private Player currentPlayer;

    public GameLogic(){
        players = new ArrayList();
        board = new Board();
    }

    /**
     * Add a player to the game
     * */
    public void addPlayer(Player player){
        players.add(player);
    }


    public void saveMove(Board b){
        board = b;
    }

    public Card drawCard(){
        int cardNum = (int)(Math.random() * 12 + 1);
        Card card;
        switch (cardNum){
            case 1:
                card = new OneCard();
                break;
            case 2:
                card = new TwoCard();
                break;
            case 4:
                card = new FourCard();
                break;
            case 6:
                card = drawCard();
                break;
            case 7:
                card = new SevenCard();
                break;
            case 9:
                card = drawCard();
                break;
            case 10:
                card = new TenCard();
                break;
            case 11:
                card = new ElevenCard();
                break;
            case 13:
                card = new SorryCard();
                break;
            default:
                card = new StandardCard(cardNum);
                break;
        }

        return card;
    }




}