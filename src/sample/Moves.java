package sample;

import java.util.ArrayList;

public class Moves {
    /** The  rows for each color */
    private static TileButton[] redRow, blueRow, greenRow, yellowRow;

    private static ArrayList<TileButton> moves;

    private Controller controller;

    protected String oldSpot = "", newSpot = "";


    public Moves(Controller controller){
        this.controller = controller;
        this.redRow = controller.getRedRow();
        this.blueRow = controller.getBlueRow();
        this.yellowRow = controller.getYellowRow();
        this.greenRow = controller.getGreenRow();

        //Have to explicitly set this as lamda's do not like assigning on actions in loops
        redRow[22].setOnAction(e -> {reset(); displayMoves(redRow[22], redRow[22].getC(), controller.cardValue);});
        redRow[22].setPieceColor(TileColor.RED);
        redRow[22].setPicture(controller.redPiece);

        blueRow[22].setOnAction(e -> { reset(); displayMoves(blueRow[22], blueRow[22].getC(), controller.cardValue);});
        blueRow[22].setPieceColor(TileColor.BLUE);
        blueRow[22].setPicture(controller.bluePiece);

        yellowRow[22].setOnAction(e -> { reset(); displayMoves(yellowRow[22], yellowRow[22].getC(), controller.cardValue);});
        yellowRow[22].setPieceColor(TileColor.YELLOW);
        yellowRow[22].setPicture(controller.yellowPiece);

       greenRow[22].setOnAction(e -> {reset(); displayMoves(greenRow[22], greenRow[22].getC(), controller.cardValue);});
        greenRow[22].setPieceColor(TileColor.GREEN);
        greenRow[22].setPicture(controller.greenPiece);

        moves = new ArrayList<TileButton>();


        //for testing purposes

      /*  redRow[20].setPicture(controller.redPiece);
        redRow[20].setOccupiedBy(1);
        redRow[20].setPieceColor(TileColor.RED);
        redRow[20].setOnAction(e -> displayMoves(redRow[20], TileColor.RED, controller.cardValue));
*/

    }

    public void movePiece(TileButton oldPiece, TileButton newPiece, TileColor playerColor, boolean swap){

        //TODO: check if no piece was moved
        oldSpot = convertTileButton(oldPiece);
        newSpot = convertTileButton(newPiece);

        TileButton end = null;

        if(newPiece.getPicture() != null && swap == false) {
            end = getColorRow(newPiece.getPieceColor())[22];
            end.setOccupiedBy(end.getOccupiedBy()+1);
        }


        TileButton temp = new TileButton(playerColor, -10);
        if(swap && newPiece.getPieceColor() != null){
            temp = new TileButton(newPiece.getPieceColor(), newPiece.getSpot());
            temp.setPicture(newPiece.getPicture());
            temp.setPieceColor(newPiece.getPieceColor());
        }
        newPiece.setPicture(oldPiece.getPicture());

             newPiece.setOccupiedBy(newPiece.getOccupiedBy() +1);

             //TODO:Check if this works
             if(newPiece.getSpot() == 21) {
                 TileButton spawn = getColorRow(newPiece.getC())[22];
                 spawn.setOccupiedBy(spawn.getOccupiedBy() - 1);
             }

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
           // Image image = oldSpot.getPicture();;

            int spot = origSpot.getSpot();

            TileButton[] currentRow = getColorRow(origSpot.getC());

            //condition for entering the home row
            if(spot < 2 && origSpot.getC() == playerColor && moveAmount != 4 && moveAmount + spot > 1){

                if(spot == 0) {
                    moveAmount--;
                }

                if(moveAmount + origSpot.getSpot() + 15 < 22 && moveAmount + origSpot.getSpot() > 1) {

                    if(currentRow[moveAmount + 15].getOccupiedBy() == 0 || currentRow[moveAmount + 15].getSpot() == 21)

                    moves.add(currentRow[moveAmount + 15]);

                    //if(spot + 6 > moveAmount) {
                    //TileButton[] row = getColorHome(playerColor);
                    //moves.add(row[moveAmount - 1]);
                    //}
                }
                    return moves;
            }

            //for when the piece is inside its homerow
            if(spot > 15 && spot < 21 ){

                if(moveAmount == 4){
                    moveAmount *= -1;

                    //if the move requires them to leave the home area
                    if(spot + moveAmount < 16){
                        //if the move makes them back a row
                        if(spot + moveAmount - 13 < 0){
                                TileColor prevColor = getNextRowColor(origSpot.getC(), true);
                                TileButton[] prevRow = getColorRow(prevColor);

                                if(prevRow[spot + moveAmount +2].getOccupiedBy() == 0)
                                    moves.add(prevRow[spot + moveAmount + 2]);
                               System.out.println("Spot goes to prev row " + (spot + moveAmount + 2));
                       }
                       else{
                           System.out.println("Spot goes to current row " + (spot + moveAmount - 13));
                           if(currentRow[spot + moveAmount - 13].getOccupiedBy() == 0)
                            moves.add(currentRow[spot + moveAmount - 13]);
                        }
                    } else{
                        if(currentRow[spot + moveAmount].getOccupiedBy() == 0 || currentRow[spot].getSpot() == 21);
                            moves.add((currentRow[spot + moveAmount]));
                    }

                    return  moves;
                }


                if(moveAmount + spot < 22){
                    if(currentRow[spot + moveAmount].getOccupiedBy() == 0 || currentRow[spot + moveAmount].getSpot() == 21)
                        moves.add(currentRow[spot + moveAmount]);
                    return moves;
                }
            }

            //if the card is 4
            if(moveAmount == 4)
                moveAmount *= -1;
            if(moveAmount == 10)
                move(origSpot, playerColor, -1);





            if(moveAmount != 13  && spot < 16)  {
                //if the move goes over to the next array
                if (moveAmount + spot > 15 ) {
                    TileColor nextColor = getNextRowColor(origSpot.getC(), false) ;

                    //if the player is entering their home row next
                    if(nextColor == playerColor){
                        if(moveAmount + spot - 15 >= 3 ) {
                            TileButton[] row = getColorRow(nextColor);
                            if(moveAmount + spot -2 < 22)
                                moves.add(row[moveAmount + spot - 2]);
                            return moves;
                        }
                    }


                        TileButton[] row = getColorRow(nextColor);
                        if(row[moveAmount + spot - 15 - 1].getOccupiedBy() == 0)
                            moves.add(row[moveAmount + spot - 15 - 1]);
                    //if the move stays in the current row;
                } else {
                    if(spot < 15 && moveAmount > 0 ) {
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

        for(int i = 0; i < 16; i++){
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

    public boolean winner(TileButton home){
        if(home.getOccupiedBy() == 4){
            System.out.println("winner");
            return true;
        }
        return false;
    }

    public String convertTileButton(TileButton t){
        String loc = "";

        switch (t.getC()){
            case RED:
                loc = "R";
                break;
            case BLUE:
                loc = "B";
                break;
            case YELLOW:
                loc = "Y";
                break;
            case GREEN:
                loc = "G";
                break;
        }

        loc += t.getSpot() + "";

        return  loc;
    }

}
