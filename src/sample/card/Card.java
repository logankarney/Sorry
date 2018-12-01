package sample.card;

import sample.Pawn;

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

    public ArrayList<Integer> getMoves(Pawn p, int[][] board){

    }

}
