package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;


public class OneCard extends Card {
    public OneCard(){
        value = 1;
        desc =  "Start a Pawn Out or move one man, which is already in play, forward one square.";
    }

    public ArrayList<Board> getMoves(Player p, Board b){
        ArrayList<Board> boards = new ArrayList<>();

        for (Pawn pawn: p.getPawns()){
            Board board = new Board(b);
            boolean valid;
            if (pawn.isInStart()){
                valid = board.moveFromStart(pawn);
            }else{
                valid = board.move(pawn, value);
            }

            if(valid) boards.add(board);
        }

        return boards;
    }

}
