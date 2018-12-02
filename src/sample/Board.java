package sample;

import java.util.ArrayList;

public class Board {
    private Pawn[][] sorryBoard;
    private ArrayList<Pawn>[] start;
    private Player[] players;
    public Board(){
        sorryBoard = new Pawn[4][21];
        players = new Player[4];
        start = new ArrayList[4];
        // fill in start
        for (int i = 0; i<4; i++){
            for (int j = 0; j<4; j++){
                start[i].add(players[i].getPawns()[j]);
            }
        }
    }

    /**
     * Copy constructor
     */
    public Board(Board b){
        Pawn[][] temp = b.getSorryBoard();
        ArrayList<Pawn>[] tempStart = b.getStart();
        for (int i=0; i<4; i++){
            for (int j=0; j<15; j++){
                this.sorryBoard[i][j] = temp[i][j];
                if (j<4){
                    this.start[i].set(j,tempStart[i].get(j));
                }
            }
        }
    }

    /**
     * Attempt to move the specified pawn. True if successful.
     * @param p
     * @param numSpaces
     */
    public boolean move(Pawn p, int numSpaces){
        int row = p.getRow();
        int space = p.getSpace();
        int newRow;
        int newSpace;

        // Not in home row
        if (space > 2 || p.getPlayerID() != row) {
            // normal movement within one row
            if (space + numSpaces < 15) {
                newRow = row;
                newSpace = space + numSpaces;
            // approaching home row
            } else if (nextRow(row) == p.getPlayerID()) {
                // Not going into safe zone
                if (numSpaces - (14 - space) <= 2) {
                    newRow = nextRow(row);
                    newSpace = numSpaces - (14 - space) - 1;
                // Going into safe zone
                } else {
                    newRow = nextRow(row);
                    newSpace = numSpaces + space;
                }
            // Wrapping around normally
            } else {
                newRow = nextRow(row);
                newSpace = numSpaces - (14 - space) -1;
            }
        // In the spaces before your safe zone
        }else{
            newRow = row;
            if (space == 0){
                if (numSpaces == 1){
                    newSpace = 1;
                }else{
                    newSpace = 14 + numSpaces - 1;
                }
            }else{
                newSpace = 14 + numSpaces;
            }
        }
        //overshot home
        if (newSpace > 20){
            return false;
        }
        // Check for collision
        if (sorryBoard[newRow][newSpace] == null){
            sorryBoard[row][space] = null;
        }else if (sorryBoard[newRow][newSpace].getPlayerID() == p.getPlayerID()){
            return false;
        }else{
            sorryBoard[row][space] = null;
            returnToStart(sorryBoard[newRow][newSpace]);
        }
        resolveSlide(p);
        p.setLocation(newRow, newSpace);
        sorryBoard[newRow][newSpace] = p;
        return true;
    }


    private static int nextRow(int row){
        if (row == 3){
            return 0;
        }
        return row+1;
    }

    public Pawn[][] getSorryBoard() {
        return sorryBoard;
    }

    public ArrayList<Pawn>[] getStart() {
        return start;
    }

    /**
     * Send a pawn back to the start
     * @param p
     */
    public void returnToStart(Pawn p){
        sorryBoard[p.getRow()][p.getSpace()] = null;
        start[p.getPlayerID()].add(p);
        p.setInStart(true);
    }

    public boolean moveFromStart(Pawn p){
        int id = p.getPlayerID();
        if (sorryBoard[id][3] != null && sorryBoard[id][3].getPlayerID() != id){
            sorryBoard[p.getPlayerID()][3] = p;
            p.setInStart(false);
            start[p.getPlayerID()].remove(p);
            return true;
        }
        return false;
    }

    public void setPawnLocation(int row, int space, Pawn p){
        sorryBoard[row][space] = p;
        p.setLocation(row,space);
        p.setInStart(false);
    }

    public Player[] getPlayers() {
        return players;
    }

    /**
     * Performs a slide if necessary
     * @param p
     */
    private void resolveSlide(Pawn p){
        if (p.getPlayerID() != p.getRow()){
            if (p.getSpace() == 0){
                move(p, 1);
                move(p, 1);
                move(p, 1);
            }else if (p.getSpace() == 8){
                move(p, 1);
                move(p, 1);
                move(p, 1);
                move(p, 1);
            }
        }
    }
}
