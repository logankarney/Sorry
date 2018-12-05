package sample;

import javafx.util.Pair;


public class Pawn{
    private TileColor color;
    private int playerID;
    private int row;
    private int space;
    private boolean isInStart;
    private boolean isHome;
    private int pawnID;

    public Pawn (TileColor color, int playerID, int pawnID){
        this.pawnID =pawnID;
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

    public Pawn(Pawn p){
        this.pawnID = p.getPawnID();
        this.color = p.getColor();
        this.playerID = p.getPlayerID();
        this.isInStart = p.isInStart();
        this.isHome = p.isHome();
        this.row = p.getRow();
        this.space = p.getSpace();
    }

    public TileColor getColor() {
        return color;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
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

    public int getPawnID() {
        return pawnID;
    }
}