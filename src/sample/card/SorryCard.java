package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;

public class SorryCard extends Card {
    public SorryCard(){
        value = 13;
        desc = "Send an opponent's pawn back to their start, replacing them with a pawn from your own.";
    }

    public ArrayList<Board> getMoves(Player p, Board b) {
        Player[] players = b.getPlayers();
        ArrayList<Board> boards = new ArrayList<>();

        for (Pawn pawn : p.getPawns()){
            if(pawn.isInStart()) {
                for (int i = 0; i < 4; i++) {
                    if (i != p.getPlayerID()) {
                        if(players[i] != null) {
                            for (Pawn opponent : players[i].getPawns()) {
                                // Check opponent isn't safe
                                if (!opponent.isInStart() && opponent.getSpace() < 15) {
                                    Board board = new Board(b);
                                    int row = opponent.getRow();
                                    int space = opponent.getSpace();
                                    board.returnToStart(opponent);
                                    board.setPawnLocation(row, space, pawn);
                                    boards.add(board);
                                }
                            }
                        }
                    }
                }
            }
        }

        return boards;
    }
}
