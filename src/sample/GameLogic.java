package sample;

import sample.card.Card;

import java.util.ArrayList;

public class GameLogic{
    private int[][] board;
    private ArrayList<Player> players;

    public GameLogic(){
        board = new int[4][15];
        players = new ArrayList();

        for (int i=0; i<4; i++){
            for (int j=0; j<15; j++){
                board[i][j] = 0;
            }
        }
    }

    /**
     * Add a player to the game
     * */
    public void addPlayer(Player player){
        players.add(player);
    }

    /**
     * Generates a random card 1 through 12, as well as 13 for a Sorry
     * @return Random card
     */
   public Card drawCard(){
       return new Card((int)(Math.random() * 10 + 1));
   }

   public ArrayList<Integer> viewValidMoves(Pawn pawn, int[] board, Card card){
        int cardNum = card.getValue();

   }


}