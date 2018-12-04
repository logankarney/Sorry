package sample;

import javafx.util.Pair;


public class Pawn{
    private TileColor color;
    private int playerID;
    private int row;
    private int space;
    private boolean isInStart;
    private boolean isHome;

    public Pawn (TileColor color, int playerID){
        this.color = color;
        this.playerID = playerID;
        isInStart = true;
        isHome =  false;
        switch (color){
            case GREEN:
                row = 0;
                break;
            case RED:
                row = 1;
                break;
            case BLUE:
                row = 2;
                break;
            case YELLOW:
                row = 3;
                break;
        }
        space = 3;
    }

    public TileColor getColor() {
        return color;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setInStart(boolean inStart) {
        isInStart = inStart;
    }

    public boolean isInStart() {
        return isInStart;
    }

    public int getRow() {
        return row;
    }

    public int getSpace() {
        return space;
    }

    public void setLocation(int row, int space) {
        this.row = row;
        this.space = space;
    }

    public int getPlayerID() {
        return playerID;
    }


}