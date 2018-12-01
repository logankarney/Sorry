package sample;

import javafx.util.Pair;


public class Pawn{
    private TileColor color;
    private int row;
    private int space;
    private boolean isInStart;
    private boolean isInHomeRow;
    private boolean isHome;

    public Pawn (TileColor color){
        this.color = color;
        isInStart = true;
        isHome =  false;
        isInHomeRow = false;
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
        space = -1;
    }

    public TileColor getColor() {
        return color;
    }

    public boolean isHome() {
        return isHome;
    }

    public boolean isInHomeRow() {
        return isInHomeRow;
    }

    public boolean isInStart() {
        return isInStart;
    }

}