package sample;

import java.util.ArrayList;

public class Board {
    private Pawn[][] sorryBoard;
    private ArrayList<Pawn>[] start;
    private Player[] players;
    public Board(Player[] players, int numPlayers){
        sorryBoard = new Pawn[4][21];
        this.players = players;
        start = new ArrayList[numPlayers];
        // fill in start
        for (int i = 0; i<4; i++){
            start[i] = new ArrayList<>(4);
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
        sorryBoard = new Pawn[4][21];
        start = new ArrayList[4];
        for (int i=0; i<4; i++){
            this.start[i] = new ArrayList<>(4);
            for (int j=0; j<21; j++){
                this.sorryBoard[i][j] = temp[i][j];
                if (j<4){
                    this.start[i].add(tempStart[i].get(j));
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

        if (p.isInStart()){
            return false;
        }

        // Moving backwards
        if (numSpaces < 0){
            if (numSpaces == -1){
                if (space == 0){
                    newRow = nextRow(nextRow(nextRow(row)));
                    newSpace = 14;
                }else if (space == 15){
                    newRow = row;
                    newSpace = 1;
                }else{
                    newRow = row;
                    newSpace = space -1;
                }
            } else { // Hard coding -4 because it's a pain
                if (space < 4){
                    newRow = nextRow(nextRow(nextRow(row)));
                    newSpace = 14 + space + numSpaces;
                } else if (space == 15){
                    newRow = nextRow(nextRow(nextRow(row)));
                    newSpace = 13;
                } else if (space == 16){
                    newRow = nextRow(nextRow(nextRow(row)));
                    newSpace = 14;
                } else if (space == 17){
                    newRow = row;
                    newSpace = 0;
                } else if (space == 18){
                    newRow = row;
                    newSpace = 1;
                } else {
                    newRow = row;
                    newSpace = space + numSpaces;
                }
            }
        }
        // Moving forward
        // Not in home row
        else if (space > 2 || p.getPlayerID() != row) {
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
        p.setLocation(newRow, newSpace);
        sorryBoard[newRow][newSpace] = p;
        resolveSlide(p);

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

    /**
     * Attempt to move a pawn from spawn
     * @param p
     * @return true if move is successful
     */
    public boolean moveFromStart(Pawn p){
        int id = p.getPlayerID();

        if (sorryBoard[id][3] != null){
            // Ally pawn outside -- illegal
            if (sorryBoard[id][3].getPlayerID() == id){
                return false;
            }else{ // Enemy pawn outside
                returnToStart(sorryBoard[p.getPlayerID()][3]);
            }
        }
        // Move from start
        sorryBoard[p.getPlayerID()][3] = p;
        p.setInStart(false);
        start[p.getPlayerID()].remove(p);
        return true;
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
     * Performs a slide if necessary and sends all pawns on the slider
     * back to the start (including ally pawns)
     * @param p
     */
    private void resolveSlide(Pawn p){
        // You don't slide on your own color
        if (p.getPlayerID() != p.getRow()){
            //Short slider
            if (p.getSpace() == 0){
                for (int i = 1; i<4; i++){
                    if (sorryBoard[p.getRow()][i] != null){
                        returnToStart(sorryBoard[p.getRow()][i]);
                    }
                }
                move(p, 3);
            // Long slider
            }else if (p.getSpace() == 8){
                for (int i = 9; i<13; i++){
                    if (sorryBoard[p.getRow()][i] != null){
                        returnToStart(sorryBoard[p.getRow()][i]);
                    }
                }
                move(p, 4);
            }
        }
    }
}
