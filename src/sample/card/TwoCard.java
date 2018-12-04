package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;

public class TwoCard extends Card {
    public TwoCard(){
        value = 2;
        desc =  "Start a Pawn Out or move one man, which is already in play, forward two squares. Draw again.";
    }

    public ArrayList<Board> getMoves(Player p, Board b){
        ArrayList<Board> boards = new ArrayList<>();

        for (Pawn pawn: p.getPawns()){
            boolean valid;
            Board board = new Board(b);
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
