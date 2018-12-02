package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;

public class FourCard extends Card {
    public FourCard(){
        value = 4;
        desc =  "Move a pawn backwards four squares.";
    }

    public ArrayList<Board> getMoves(Player p, Board b) {
        ArrayList<Board> boards = new ArrayList<>();
        for (Pawn pawn: p.getPawns()){
            Board board = new Board(b);
            if (!pawn.isInStart()){
                if(board.move(pawn, -4)) boards.add(board);
            }
        }
        return boards;
    }
}
