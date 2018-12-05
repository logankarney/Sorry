package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;

public class ElevenCard extends Card {
    public ElevenCard(){
        value = 11;
        desc ="Move forward 11 or swap one of your pawns with an opponent's";
    }

    @Override
    public ArrayList<Board> getMoves(Player p, Board b) {
        Player[] players = b.getPlayers();
        ArrayList<Board> boards = new ArrayList<>();

        for (Pawn pawn : p.getPawns()){
            if (!pawn.isInStart() && !(pawn.getSpace() > 15)){
                // Look for swaps
                for (int i=0; i<4; i++){
                    if (i != p.getPlayerID()) {
                        if (players[i] != null){
                            for (Pawn opponent : players[i].getPawns()) {
                                Board board = new Board(b);
                                //check for valid swap
                                if (!opponent.isInStart() && opponent.getSpace() < 15) {
                                    int row = opponent.getRow();
                                    int space = opponent.getSpace();
                                    board.setPawnLocation(pawn.getRow(), pawn.getSpace(), opponent);
                                    board.setPawnLocation(row, space, pawn);
                                    boards.add(board);
                                }
                            }
                        }
                    }
                }
            }
            // Look for move-11s
            Board board = new Board(b);
            if (board.move(pawn,11)) boards.add(board);
        }

        return boards;

    }
}
