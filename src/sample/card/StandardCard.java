package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;

public class StandardCard extends Card {
    public StandardCard(int value){
        this.value = value;
        this.desc = "Move " + value + " spaces.";
    }

    @Override
    public ArrayList<Board> getMoves(Player p, Board b) {
        ArrayList<Board> boards = new ArrayList<>();
        for (Pawn pawn: p.getPawns()){
            if (!pawn.isInStart()){
                Board board = new Board(b);
                if (board.move(pawn,value)) boards.add(board);
            }
        }
        return boards;
    }
}
