package sample.card;

import sample.Board;
import sample.Pawn;
import sample.Player;

import java.util.ArrayList;


public abstract class Card {

    protected int value;
    protected String desc;

    public Card(){}

    public Card(int value){
        this.value = value;

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(int value) {
        this.desc = desc;
    }


    /*
     * See all of a players' possible moves
     */
    public abstract ArrayList<Board> getMoves(Player p, Board b);

}
