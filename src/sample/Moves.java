package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private static ArrayList<TileButton> moves;

    private static TileButton selectedPiece;

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



        for(int i = 0; i < 4; i++){
            spawns[i].setOccupiedBy(4);
        }

        //Have to explicitly set this as lamda's do not like assigning on actions in loops
        spawns[0].setOnAction(e -> {reset(); displayMoves(spawns[0], spawns[0].getC(), controller.cardValue);});
        spawns[0].setPieceColor(TileColor.RED);
        spawns[0].setPicture(controller.redPiece);

        spawns[1].setOnAction(e -> { reset(); displayMoves(spawns[1], spawns[1].getC(), controller.cardValue);});
        spawns[1].setPieceColor(TileColor.BLUE);
        spawns[1].setPicture(controller.bluePiece);

        spawns[2].setOnAction(e -> { reset(); displayMoves(spawns[2], spawns[2].getC(), controller.cardValue);});
        spawns[2].setPieceColor(TileColor.YELLOW);
        spawns[2].setPicture(controller.yellowPiece);

        spawns[3].setOnAction(e -> {reset(); displayMoves(spawns[3], spawns[3].getC(), controller.cardValue);});
        spawns[3].setPieceColor(TileColor.GREEN);
        spawns[3].setPicture(controller.greenPiece);

        moves = new ArrayList<TileButton>();

        selectedPiece = null;

        //for testing purposes

        /*blueRow[5].setPicture(controller.bluePiece);
        blueRow[5].setOccupiedBy(1);
        blueRow[5].setPieceColor(TileColor.BLUE);
        pieces.add( blueRow[5]);

        greenRow[10].setPicture(controller.yellowPiece);
        greenRow[10].setOccupiedBy(1);
        greenRow[10].setPieceColor(TileColor.YELLOW);

        pieces.add(greenRow[10]);
*/
        redHome[0].setPicture(controller.redPiece);
        redHome[0].setPieceColor(TileColor.RED);
        redHome[0].setOccupiedBy(1);
        redHome[0].setOnAction(e -> {
            displayMoves(redHome[0], TileColor.RED, controller.cardValue);
        });
    }

    public void movePiece(TileButton oldPiece, TileButton newPiece, TileColor playerColor, boolean swap){

        TileButton spawn = null;

        if(newPiece.getPicture() != null && swap == false) {
            spawn= getColorSpawn(newPiece.getPieceColor());
            spawn.setOccupiedBy(spawn.getOccupiedBy()+1);
        }


        TileButton temp = new TileButton(playerColor, -10);
        if(swap && newPiece.getPieceColor() != null){
            temp = new TileButton(newPiece.getPieceColor(), newPiece.getSpot());
            temp.setPicture(newPiece.getPicture());
            temp.setPieceColor(newPiece.getPieceColor());
        }
        newPiece.setPicture(oldPiece.getPicture());
        newPiece.setOccupiedBy(1);
        newPiece.setPieceColor(playerColor);
        newPiece.setOnAction(e -> displayMoves(newPiece, playerColor, controller.cardValue));

        oldPiece.setOccupiedBy(oldPiece.getOccupiedBy() - 1);
        if(oldPiece.getOccupiedBy() == 0) {
            oldPiece.setPicture(null);
            oldPiece.setPieceColor(null);

            oldPiece.setOnAction(e -> {
            });
        }

        if(swap && newPiece.getPieceColor() != null){
            oldPiece.setPicture(temp.getPicture());
            oldPiece.setOccupiedBy(1);
            oldPiece.setPieceColor(temp.getPieceColor());
            oldPiece.setOnAction(e -> displayMoves(oldPiece, oldPiece.getPieceColor(), controller.cardValue));
        }

        //if its a sliding spot
        if(newPiece.getSpot() == 0 || newPiece.getSpot() == 10)
            if(newPiece.getC() != newPiece.getPieceColor()) {
                TileButton[] row = getColorRow(newPiece.getC());

               /* //removes any piece thats on the slider CURRENT BUG
                for(int i = newPiece.getSpot(); i < newPiece.getSpot() + 3; i++){
                    if(row[i].getPicture() != null){
                        row[i].setPicture(null);
                        row[i].setPieceColor(null);
                        row[i].setOccupiedBy(0);
                    }
                }*/

                movePiece(newPiece, row[newPiece.getSpot() + 3], newPiece.getPieceColor(), false);
            }
    }


    public void displayMoves(TileButton tile, TileColor playerColor, int moveAmount){
        moves.clear();
            if(playerColor == tile.getPieceColor()) {
                ArrayList<TileButton> moves = move(tile, playerColor, moveAmount);
                for (TileButton m : moves) {
                    m.setId(getId(playerColor));
                    m.setOnAction(e -> {
                        selectedPiece = m;
                        reset();
                        if(moveAmount == 11)
                            movePiece(tile, m, playerColor, true);
                        else
                             movePiece(tile, m, playerColor, false);
                    });
                }
        }
    }


    public ArrayList<TileButton> move(TileButton origSpot, TileColor playerColor, int moveAmount){
           // Image image = origSpot.getPicture();

            int spot = origSpot.getSpot();

            TileButton[] currentRow = getColorRow(origSpot.getC());

            //condition for entering the home row
            if(spot < 2 && origSpot.getC() == playerColor){

                if(moveAmount < 2)
                    moves.add(currentRow[moveAmount + 1]);

               if(spot + 6 > moveAmount) {
                   TileButton[] row = getColorHome(playerColor);
                   moves.add(row[moveAmount - 1]);
               }
               return moves;
            }

            //21 is the 5th element
            //for when the piece is inside its homerow
            if(spot > 15){
                TileButton[] row = getColorHome(origSpot.getC());
                if(moveAmount + spot < 21){
                    System.out.println(spot);
                    moves.add(row[21 - spot + moveAmount]);
                    return moves;
                }
                else if(spot != 22)
                    moves.add(row[5]);
            }

            //if the card is 4
            if(moveAmount == 4)
                moveAmount *= -1;
            if(moveAmount == 10)
                move(origSpot, playerColor, -1);





            if(moveAmount != 13  && spot < 16)  {
                //if the move goes over to the next array
                if (moveAmount + spot > 15) {
                    TileColor nextColor = getNextRowColor(origSpot.getC(), false) ;

                    //TODO:When the player needs to go home

                        TileButton[] row = getColorRow(nextColor);
                        if(row[moveAmount + spot - 15 - 1].getOccupiedBy() == 0)
                            moves.add(row[moveAmount + spot - 15 - 1]);
                    //if the move stays in the current row;
                } else {
                    if(spot < 15 && moveAmount > 0) {
                        if(currentRow[spot + moveAmount].getOccupiedBy() == 0)
                            moves.add(currentRow[spot + moveAmount]);
                    }
                }

                if (moveAmount  < 0) {

                    if(moveAmount + spot < 0) {
                        TileColor nextColor = getNextRowColor(origSpot.getC(), true);
                        TileButton[] row = getColorRow(nextColor);
                        int amount = spot - 4;
                        if(row[amount + 16].getOccupiedBy() == 0)
                            moves.add(row[amount + 16]);
                    } else
                        if(currentRow[spot + moveAmount].getOccupiedBy() == 0)
                            moves.add(currentRow[spot + moveAmount]);
                }

            }
             for (TileButton t : getSpecialMoves(origSpot, playerColor, moveAmount)) {
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

    public TileButton[] getColorHome(TileColor c){
        TileButton[] row = redRow;
        switch (c){
            case RED:
                row = redHome;
                break;
            case BLUE:
                row = blueHome;
                break;
            case YELLOW:
                row = yellowHome;
                break;
            case GREEN:
                row = greenHome;
                break;
        }

        return row;
    }

    public TileButton getColorSpawn(TileColor c){
        TileButton spawn = spawns[0];
        switch (c){
            case RED:
                spawn = spawns[0];
                break;
            case BLUE:
                spawn = spawns[1];
                break;
            case YELLOW:
                spawn = spawns[2];
                break;
            case GREEN:
                spawn = spawns[3];
                break;
        }

        return spawn;
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

    public ArrayList<TileButton> getSpecialMoves(TileButton origSpot, TileColor playerColor, int moveAmount){
            ArrayList<TileButton> moves = new ArrayList<TileButton>();

            TileButton[] row = getColorRow(playerColor);

        System.out.println(origSpot.getSpot());

            if(moveAmount == 1 && origSpot.getSpot() == 22) {
                    if(row[3].getPieceColor() != playerColor)
                        moves.add(row[3]);

            } else if(moveAmount == 2 && origSpot.getSpot() == 22){
               // if (row[3].getOccupiedBy() == 0)
                    if(row[3].getPieceColor() != playerColor)
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

        for(int i = 0; i < redRow.length; i++){
            if(redRow[i].getPieceColor() != playerColor && redRow[i].getPieceColor() != null)
                moves.add(redRow[i]);
            if(blueRow[i].getPieceColor() != playerColor && blueRow[i].getPieceColor() != null)
                moves.add(blueRow[i]);
            if(yellowRow[i].getPieceColor() != playerColor && yellowRow[i].getPieceColor() != null)
                moves.add(yellowRow[i]);
            if(greenRow[i].getPieceColor() != playerColor && greenRow[i].getPieceColor() != null)
                moves.add(greenRow[i]);
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
