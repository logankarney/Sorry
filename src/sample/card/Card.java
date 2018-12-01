package sample.card;

import sample.Board;
import sample.Pawn;


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
     * Default behavior for a generic card
     */
    public Board getMoves(Pawn p, Board b){
        Board board = new Board(b);
        board.move(p, value);
        return board;
    }

}
