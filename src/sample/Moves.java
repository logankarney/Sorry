package sample;

import sample.Controller;
import sample.TileButton;
import sample.TileColor;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Moves {
    /** The outside rows for each color */
    private static TileButton[] redRow, blueRow, greenRow, yellowRow;

    /** the inside lane for each color */
    private static TileButton[] redHome, blueHome, greenHome, yellowHome;

    /** The spawn for each color, red's is 0, blue's is 1, ect */
    private static TileButton[] spawns;

    protected static ArrayList<TileButton> pieces;

    private static ArrayList<TileButton> moves;

    private Controller controller;

    public Moves(Controller controller){
        this.controller = controller;
        this.redRow = controller.getRedRow();
        this.blueRow = controller.getBlueRow();
        this.yellowRow = controller.getYellowRow();
        this.greenRow = controller.getGreenRow();
        this.redHome = controller.getRedHome();
        this.blueHome = controller.getBlueHome();
        this.yellowHome = controller.getYellowHome();
        this.greenHome = controller.getGreenHome();
        this.spawns = controller.getSpawns();


        pieces = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            spawns[i].setOccupiedBy(4);
            pieces.add(spawns[i]);
        }

        moves = new ArrayList<TileButton>();

        blueRow[5].setPicture(controller.bluePiece);
        blueRow[5].setOccupiedBy(1);
        blueRow[5].setPieceColor(TileColor.BLUE);
        pieces.add( blueRow[5]);

        greenRow[10].setPicture(controller.yellowPiece);
        greenRow[10].setOccupiedBy(1);
        greenRow[10].setPieceColor(TileColor.YELLOW);

        pieces.add(greenRow[10]);

        redRow[3].setPicture(controller.redPiece);
        redRow[3].setPieceColor(TileColor.RED);
        redRow[3].setOccupiedBy(1);

        pieces.add(redRow[3]);

    }


    public void displayMoves(TileColor playerColor, int moveAmount){
        int offset = 0;
        switch(playerColor){
            case RED:
                offset = 0;
                break;
            case BLUE:
                offset = 4;
                break;
            case YELLOW:
                offset = 8;
                break;
            case GREEN:
                offset = 12;
                break;
        }

        /*for(int i = offset; i < offset + 4; i++){
            move(spawns[i], playerColor, moveAmount);
        }*/
        for(TileButton t : pieces){
            ArrayList<TileButton> moves = move(t, playerColor, moveAmount);
            for(TileButton m : moves){
                m.setId(getId(playerColor));
                m.setOnAction(e -> {
                    reset();
                });
            }
        }
    }


    public ArrayList<TileButton> move(TileButton origSpot, TileColor playerColor, int moveAmount){
           // Image image = origSpot.getPicture();

            int spot = origSpot.getSpot();

            TileButton[] currentRow = getColorRow(origSpot.getC());


            //if the card is 4
            if(moveAmount == 4)
                moveAmount *= -1;
            if(moveAmount == 10)
                move(origSpot, playerColor, -1);



            if(moveAmount != 13  && spot < 20)  {
                //if the move goes over to the next array
                if (moveAmount + spot > 15) {
                    TileColor nextColor = getNextRowColor(origSpot.getC(), false) ;

                    //TODO:When its the player's row

                        TileButton[] row = getColorRow(nextColor);
                        moves.add(row[moveAmount + spot - 15 - 1]);
                    //if the move stays in the current row;
                } else {
                    if(spot < 15 && moveAmount > 0)
                        moves.add(currentRow[spot + moveAmount]);
                }

                if (moveAmount  < 0) {

                    if(moveAmount + spot < 0) {
                        TileColor nextColor = getNextRowColor(origSpot.getC(), true);
                        TileButton[] row = getColorRow(nextColor);
                        int amount = spot - 4;
                        moves.add(row[amount + 16]);
                    } else
                        moves.add(currentRow[spot + moveAmount]);
                }

            }
             for (TileButton t : getSpecialMoves(playerColor, moveAmount)) {
                moves.add(t);
                }
            return  moves;
    }

    public TileColor getNextRowColor(TileColor currentRow, boolean reverse){
        TileColor next;
        TileColor prev;
        switch(currentRow){
            case RED:
                next = TileColor.BLUE;
                prev = TileColor.GREEN;
                break;
            case BLUE:
                next = TileColor.YELLOW;
                prev = TileColor.RED;
                break;
            case YELLOW:
                next = TileColor.GREEN;
                prev = TileColor.BLUE;
                break;
            case GREEN:
                next= TileColor.RED;
                prev = TileColor.YELLOW;
                break;
                default:
                    next = TileColor.RED;
                    prev = TileColor.YELLOW;
        }

        if(reverse)
            return  prev;
        return  next;
    }

    public TileButton[] getColorRow(TileColor c){
        TileButton[] row = redRow;
        switch (c){
            case RED:
                row = redRow;
                break;
            case BLUE:
                row = blueRow;
                break;
            case YELLOW:
                row = yellowRow;
                break;
            case GREEN:
                row = greenRow;
                break;
        }

        return row;
    }

    public  void removeCss(ArrayList<TileButton> moves){
        for(TileButton t : moves){
            switch(t.getC()){
                case RED:
                    t.setId("red-tile");
                    break;
                case BLUE:
                    t.setId("blue-tile");
                    break;
                case YELLOW:
                    t.setId("yellow-tile");
                    break;
                case GREEN:
                    t.setId("green-tile");
                    break;
            }
        }
    }

    public ArrayList<TileButton> getSpecialMoves(TileColor playerColor, int moveAmount){
            ArrayList<TileButton> moves = new ArrayList<TileButton>();

            TileButton[] row = getColorRow(playerColor);

            if(moveAmount == 1) {
                if (row[3].getOccupiedBy() == 0)
                    moves.add(row[3]);
            } else if(moveAmount == 2){
                if (row[3].getOccupiedBy() == 0)
                    moves.add(row[3]);
            }

            else if(moveAmount == 10){
            }

            else if(moveAmount == 11){
                    for(TileButton t : getEnemyPieces(playerColor)){
                        moves.add(t);
                    }
            }

            else if(moveAmount == 13){
                for(TileButton t : getEnemyPieces(playerColor)){
                    moves.add(t);
                }
            }
            return moves;
    }

    public ArrayList<TileButton> getEnemyPieces(TileColor playerColor){
        ArrayList<TileButton> moves= new ArrayList<TileButton>();

        for(int i = 0; i < pieces.size(); i++){
            if(pieces.get(i).getPieceColor() != playerColor && pieces.get(i).getSpot() <= 15){
                moves.add(pieces.get(i));
            }
        }

        return moves;
    }

    public  void reset(){
        removeCss(moves);
        moves.clear();
    }

    public String getId(TileColor c){
        String rtn = "-move-tile";
        switch(c){
            case RED:
                rtn = "red" + rtn;
                break;
            case BLUE:
                rtn = "blue" + rtn;
                break;
            case YELLOW:
                rtn = "yellow" + rtn;
                break;
            case GREEN:
                rtn = "green" + rtn;
                break;
        }
        return  rtn;
    }
}
