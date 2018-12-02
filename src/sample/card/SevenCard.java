package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;

public class SevenCard extends Card {
    public SevenCard(){
        value = 7;
        desc = "Split 7 spaces of movement between two pawns";
    }

    @Override
    public ArrayList<Board> getMoves(Player p, Board b) {
        ArrayList<Board> boards = new ArrayList<>();
        Pawn[] pawns = p.getPawns();
        for (Pawn pawn: pawns){
            if (!pawn.isInStart()) {
                for (int i = 1; i < 7; i++) {
                    for (Pawn partner : pawns) {
                        if (partner != pawn) {
                            Board board = new Board(b);
                            if (board.move(pawn, i) && board.move(partner, 7 - i)) {
                                boards.add(board);
                            }
                        }
                    }
                }
            }
        }

        return boards;
    }


}
