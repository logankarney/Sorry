package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;

public class TenCard extends Card {
    public TenCard(){
        value = 10;
        desc = "Move forward 10, or backward 1.";
    }

    @Override
    public ArrayList<Board> getMoves(Player p, Board b) {
        ArrayList<Board> boards = new ArrayList<>();

        for (Pawn pawn: p.getPawns()){
            Board board1 = new Board(b);
            Board board2 = new Board(b);

            if (!pawn.isInStart()){
                if (board1.move(pawn,10)){
                    boards.add(board1);
                }
                if(board2.move(pawn,-1)){
                    boards.add(board2);
                }
            }
        }
        return boards;
    }
}
