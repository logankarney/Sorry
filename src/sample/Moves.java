package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Moves {
    /** The  rows for each color */
    private static TileButton[] redRow, blueRow, greenRow, yellowRow;

    private static ArrayList<TileButton> moves;

    protected static boolean gameWon = false;

    private Controller controller;

    protected static TileColor color = null;


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
                 //spawn.setOccupiedBy(spawn.getOccupiedBy() - 1);
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

                //removes any piece thats on the slider
                for(int i = newPiece.getSpot() + 1; i < newPiece.getSpot() + 3; i++){
                    if(row[i].getPicture() != null)
                        removePiece(row[i]);
                }

                movePiece(newPiece, row[newPiece.getSpot() + 3], newPiece.getPieceColor(), false);
            }

            controller.playersTurn = false;
    }


    public void displayMoves(TileButton tile, TileColor playerColor, int moveAmount){
        reset();
        moves.clear();
        if(controller.playersTurn) {

            if (playerColor == tile.getPieceColor() && playerColor == color) {
                ArrayList<TileButton> moves = move(tile, playerColor, moveAmount);
                for (TileButton m : moves) {
                    m.setId(getId(playerColor));
                    m.setOnAction(e -> {
                        reset();
                        if (moveAmount == 11)
                            movePiece(tile, m, playerColor, true);
                        else
                            movePiece(tile, m, playerColor, false);
                    });
                }
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

    private Image getColorPicture(TileColor c){

        Image rtn = null;

        switch (c){
            case RED:
                rtn = controller.redPiece;
                break;
            case BLUE:
                rtn = controller.bluePiece;
                break;
            case YELLOW:
                rtn = controller.yellowPiece;
                break;
            case GREEN:
                rtn = controller.greenPiece;
                break;
        }

        return rtn;
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

    public String convertTileButton(TileButton t, boolean piece){
        String rtn = "";

        TileColor switcher = t.getC();

        //System.out.println("Current switching the " + t.getC() + " row, at spot " + t.getSpot() + ", containing a " + piece);

        if(t.getPieceColor() != null && piece)
            switcher = t.getPieceColor();

        switch (switcher){
            case RED:
                rtn = "R";
                break;
            case BLUE:
                rtn = "B";
                break;
            case YELLOW:
                rtn = "Y";
                break;
            case GREEN:
                rtn = "G";
                break;
        }

        return  rtn;
    }

    public void convertInput(String pawn,String pieceLocation){

            TileColor c = null;

            switch(pieceLocation.charAt(0)){
                case 'R':
                    c = TileColor.RED;
                    break;
                case 'B':
                    c = TileColor.BLUE;
                        break;
                case 'Y':
                    c = TileColor.YELLOW;
                    break;
                case 'G':
                    c = TileColor.GREEN;
                    break;
            }

            TileColor pieceColor = null;

        switch(pawn.charAt(0)){
            case 'R':
                pieceColor= TileColor.RED;
                break;
            case 'B':
                pieceColor= TileColor.BLUE;
                break;
            case 'Y':
                pieceColor = TileColor.YELLOW;
                break;
            case 'G':
                pieceColor = TileColor.GREEN;
                break;
        }

            int spot = Integer.parseInt(pieceLocation.substring(1));

            TileButton rtn = getColorRow(c)[spot];
            rtn.setPieceColor(pieceColor);
            rtn.setPicture(getColorPicture(rtn.getPieceColor()));
            rtn.setOnAction(e -> displayMoves(rtn, rtn.getPieceColor(), controller.cardValue));
            rtn.setOccupiedBy(rtn.getOccupiedBy() + 1);


            System.out.println("Piece color: " + rtn.getPieceColor());
            System.out.println("Tile color: " + rtn.getC());
            System.out.println("Spot: " + rtn.getSpot());

    }

    private void removePiece(TileButton t){
        TileButton spawn = getColorRow(t.getPieceColor())[22];
        spawn.setOccupiedBy(spawn.getOccupiedBy() + 1);

        t.setOccupiedBy(0);
        t.setPieceColor(null);
        t.setPicture(null);
        t.setOnAction(e -> {});

    }

    public void inputClearBoard(){
            for(int i = 0; i < redRow.length; i++){
                if(redRow[i].getOccupiedBy() > 0){
                    redRow[i].setOccupiedBy(0);
                    redRow[i].setPieceColor(null);
                    redRow[i].setPicture(null);
                    redRow[i].setOnAction(e -> {});
                }
                if(blueRow[i].getOccupiedBy() > 0){
                    blueRow[i].setOccupiedBy(0);
                    blueRow[i].setPieceColor(null);
                    blueRow[i].setPicture(null);
                    blueRow[i].setOnAction(e -> {});
                }
                if(yellowRow[i].getOccupiedBy() > 0){
                    yellowRow[i].setOccupiedBy(0);
                    yellowRow[i].setPieceColor(null);
                    yellowRow[i].setPicture(null);
                    yellowRow[i].setOnAction(e -> {});
                }
                if(greenRow[i].getOccupiedBy() > 0){
                    greenRow[i].setOccupiedBy(0);
                    greenRow[i].setPieceColor(null);
                    greenRow[i].setPicture(null);
                    greenRow[i].setOnAction(e -> {});
                }
            }
    }

    public ArrayList<String> getPieces(){
        ArrayList<String> pieces = new ArrayList<>();

        int redCounter = 1, blueCounter = 1, yellowCounter = 1, greenCounter = 1;

        for(int i = 0; i < redRow.length; i++){
            if(redRow[i].getOccupiedBy() > 0){
                for(int j = 0; j < redRow[i].getOccupiedBy(); j++){

                        //Piece #
                        String piece = convertTileButton(redRow[i], true) + redCounter;

                        //Tile's location
                        String pos = convertTileButton(redRow[i], false) + redRow[i].getSpot();

                        pieces.add(piece);
                        pieces.add(pos);
                        redCounter++;


                }

                if(redRow[21].getOccupiedBy() == 4)
                    gameWon = true;
            }

            if(blueRow[i].getOccupiedBy() > 0){
                for(int j = 0; j < blueRow[i].getOccupiedBy(); j++){

                    //Piece #
                    String piece = convertTileButton(blueRow[i], true) + blueCounter;

                    //Tile's location
                    String pos = convertTileButton(blueRow[i], false) + blueRow[i].getSpot();

                    pieces.add(piece);
                    pieces.add(pos);
                    blueCounter++;

                }

                if(blueRow[21].getOccupiedBy() == 4)
                    gameWon = true;
            }

            if(yellowRow[i].getOccupiedBy() > 0){
                for(int j = 0; j < yellowRow[i].getOccupiedBy(); j++){

                    //Piece #
                    String piece = convertTileButton(yellowRow[i], true) + yellowCounter;

                    //Tile's location
                    String pos = convertTileButton(yellowRow[i], false) + yellowRow[i].getSpot();

                    pieces.add(piece);
                    pieces.add(pos);
                    yellowCounter++;

                }

                if(yellowRow[21].getOccupiedBy() == 4)
                    gameWon = true;
            }

            if(greenRow[i].getOccupiedBy() > 0){
                for(int j = 0; j < greenRow[i].getOccupiedBy(); j++){

                    //Piece #
                    String piece = convertTileButton(greenRow[i], true) + greenCounter;

                    //Tile's location
                    String pos = convertTileButton(greenRow[i], false) + greenRow[i].getSpot();

                    pieces.add(piece);
                    pieces.add(pos);
                    greenCounter++;

                }
            }

            if(greenRow[21].getOccupiedBy() == 4)
                gameWon = true;
        }

        return pieces;
    }


    public static void main(String[] args){
        String temp ="{\"game\":\"Logan's game\",\"new_position\":\"R17\",\"pawn\":\"R3\",\"player\":\"Player1\"}";
        //String loc = temp.substring(temp.indexOf("new_position:") + 13, temp.indexOf(",pawn"));
        String loc = temp.substring(temp.indexOf("new_position\":") + 15, temp.indexOf((",\"pawn")) - 1);
        System.out.println(loc);

        String pawn = temp.substring(temp.indexOf("pawn\":") + 7, temp.indexOf(",\"player") - 1);

        System.out.println( pawn);

    }

}
