package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.card.Card;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;

public class Controller extends Application {

    @FXML AnchorPane pane;


    @FXML private TableView<GameInfo> tableView = new TableView<>();

    @FXML private Button hostButton, joinButton, refreshButton, endTurn;

    @FXML private Button cardButton;

    @FXML private Text currentCard, yourTurn;

    @FXML private TextArea currentCardDescription;

    private static MediaPlayer backgroundPlayer, buttonPlayer;
    private static Media backgroundSound, buttonSound;

    private static String port, playerName, gameName;

    /** The outside rows for each color */
    private static TileButton[] redRow, blueRow, greenRow, yellowRow;


    protected static TileColor playerColor = null;

    private static boolean firstTime = true;

    private final int size = 16;

    protected Image redPiece, bluePiece, yellowPiece, greenPiece;

    private static SorryClient sorryClient;
    private static GameLogic gameLogic;

    protected static boolean playersTurn = false, hasDrawn = false, gameStarted = false;

    private static boolean isHost = false;

    protected static Moves moves;

    protected static int cardValue = 0;

    private static String gamesList = "";


    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setTitle("Sorry!");
        Scene scene = new Scene(root, 1280, 848);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public void setGamesList(String gamesList){
        this.gamesList = gamesList;
        System.out.println("setGamesList: "+this.gamesList);
     //   System.out.println("controller");
        //System.out.println(gamesList);
    }


    @FXML public void initialize(){

        redPiece = new Image("/res/red.png");
        bluePiece = new Image("/res/blue.png");
        yellowPiece = new Image("/res/yellow.png");
        greenPiece = new Image("/res/green.png");
        //prevents ConnectPopup.display() from firing every time the scene changes
        if(firstTime) {


            //Gets player's preferred username and the server's port number
            ConnectPopup.display();


            playerName = ConnectPopup.getUsername();
            port = ConnectPopup.getPort();

            //size = amount of tiles in rows, 5 for home, 1 for finish, 1 for spawn
            redRow = new TileButton[size + 7];

            blueRow = new TileButton[size + 7];

            yellowRow = new TileButton[size + 7];

            greenRow = new TileButton[size + 7];


            sorryClient = new SorryClient(this);
            populateTableView();

            try {
                InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
                sorryClient.connect(inetAddress, Integer.parseInt(port));
                sorryClient.register_user(playerName);
            } catch (Exception e){}
            onRefreshClick();

            firstTime = false;
        }
    }


    @FXML private void fxButtonClicked(Event e){
//        buttonPlayer.stop();
 //       buttonPlayer.play();

        playerName = ConnectPopup.getUsername();
        port = ConnectPopup.getPort();

       if(e.getSource() == cardButton){
           onDraw();
        }

        else if(e.getSource() == endTurn){

           //String oldSpot = moves.oldSpot;
           //String newSpot = moves.newSpot;

           ArrayList<String> pieces = moves.getPieces();

           int i;

           for(i = 0; i < pieces.size() - 2; i+=2) {
              // System.out.println(pieces.get(i) + ":" + pieces.get(i + 1));

               sorryClient.update_pawn(gameName, pieces.get(i), pieces.get(i + 1), false);
           }

           //System.out.println(pieces.get(i) + ":" + pieces.get(i + 1));

           //sends the last piece to the server
            sorryClient.update_pawn(gameName, pieces.get(i), pieces.get(i + 1), true);

           //immune = true;

           yourTurn.setText("");

           if(moves.gameWon)
               sorryClient.setGameWon(true);
       }

        else if(e.getSource() == joinButton){
            try {
                GameInfo chosenGame = tableView.getSelectionModel().getSelectedItem();
                JoinPopup.display(false, chosenGame.getPlayers());

                gameName = chosenGame.getLobbyName();
                String chosenColor = JoinPopup.getChosenColor();

                if(chosenColor.equals("none"))
                    return;
                else{
                    switch(chosenColor){
                        case "Red":
                            playerColor = TileColor.RED;
                            break;
                        case "Blue":
                            playerColor = TileColor.BLUE;
                            break;
                        case "Yellow":
                            playerColor = TileColor.YELLOW;
                            break;
                        case "Green":
                            playerColor = TileColor.GREEN;
                            break;
                    }
                }

                try {

                    changeFXML("game.fxml");
                    addButtons();

                    moves = new Moves(this);
                    moves.color = playerColor;

                    //InetAddress address = InetAddress.getByName(chosenGame.getHostIP());
                    //sorryClient.connect(address,Integer.parseInt(port));
                    //sorryClient.register_user(playerName);
                    sorryClient.join_game(chosenColor, chosenGame.getLobbyName());

                    //TODO: MAYBE CHANGE THIS LOGIC
                    gameStarted = false;
                    playersTurn = false;
                    hasDrawn = true;

                } catch(Exception ex){
                    ex.printStackTrace();
                }



            } catch(NullPointerException ex){
            }


       }

       else if(e.getSource() == refreshButton){
            onRefreshClick();
       }

       else if(e.getSource() == hostButton) {

           try {

               isHost = true;


               JoinPopup.display(true, null);
               String chosenColor = JoinPopup.getChosenColor();
               gameName = JoinPopup.getGameName();
               if(chosenColor.equals("none") || gameName.equals("no game name chosen"))
                   return;
                else{
                   switch(chosenColor){
                       case "Red":
                           playerColor = TileColor.RED;
                           break;
                       case "Blue":
                           playerColor = TileColor.BLUE;
                           break;
                       case "Yellow":
                           playerColor = TileColor.YELLOW;
                           break;
                       case "Green":
                           playerColor = TileColor.GREEN;
                           break;
                   }
               }


               //sorryClient.register_user(playerName);
               //sorryClient.join_game(chosenColor, playerName);

               changeFXML("game.fxml");

               addButtons();

               moves = new Moves(this);
               moves.color = playerColor;
               playersTurn = true;


               sorryClient.create_game(gameName, chosenColor);
               sorryClient.get_game_list();
               playersTurn = true;
               hasDrawn = false;


           } catch(Exception ex){
               //e.printStacktrace();
           }

       }

       /*else if(e.getSource() == toggleCSSButton){
           greenRow[3].setPicture(greenPiece);
           if(greenRow[3].getId().equals("yellow-tile"))
           greenRow[3].setId("yellow-calculateMoves-tile");
           else
               greenRow[3].setId("yellow-tile");
       }*/

       /*else if(e.getSource() == startButton){
            String gameName = JoinPopup.getGameName();
            sorryClient.start_game(gameName);
            removeStartButton();
       }*/

    }

    public void setPlayerTurn(String name){
        Platform.runLater(() -> {
            if (playerName.equals(name)) {
                this.playersTurn = true;
                this.hasDrawn = false;
                moves.reset();
            } else {
                        moves.inputClearBoard();

                this.playersTurn = false;
                this.hasDrawn = true;
            }
        });
    }


    public void updateClient(ArrayList<String> messages){

     //   String name = messages.get(0);
        //name = name.substring(name.indexOf("player\":") + 9, name.length() - 2);

            Platform.runLater(() -> {

                /*ArrayList<String> backup = new ArrayList<>();
                for(String s : moves.getPieces()){
                    System.out.println("HERE");
                    backup.add(new String(s));
                }*/

                if (gameStarted && messages.size() > 0) {
                   // moves.inputClearBoard();
                }
                addPieces(messages);
                //TODO: parse message
                //moves.convertInput(pawn, location);

           /*     if(moves.isEmpty()){

                    System.out.println("its empty HERE");
                    for(String s : backup){
                        System.out.println("test");
                    }
                    addPieces(backup);
                }*/
            });



    }

    public void addPieces(ArrayList<String> messages){
        for (String temp : messages) {

            String name = messages.get(0);
            name = name.substring(name.indexOf("player\":") + 9, name.length() - 2);

            //String temp ="{\"game\":\"Logan's game\",\"new_position\":\"R17\",\"pawn\":\"R3\",\"player\":\"Player1\"}";
            //String loc = temp.substring(temp.indexOf("new_position:") + 13, temp.indexOf(",pawn"));
            String loc = temp.substring(temp.indexOf("new_position\":") + 15, temp.indexOf((",\"pawn")) - 1);

            String pawn = temp.substring(temp.indexOf("pawn\":") + 7, temp.indexOf(",\"player") - 1);


            System.out.println("creating the " + pawn + " pawn at " + loc +" due to " + name);
            moves.convertInput(pawn, loc);
        }
    }

    /** saving for dealing with pieces later **/
    @FXML private void onMouseOver(){
        System.out.println("Mouse entered");
    }


    private void changeFXML(String fxmlDoc){
        //https://stackoverflow.com/questions/46985889/javafx-window-changer-using-fxml/46999540#46999540
        //answer provided by Oshan_Mendis
        try {

            setPane(FXMLLoader.load(getClass().getResource(fxmlDoc)));

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    private void setPane(AnchorPane pane){
        this.pane.getChildren().clear();
        this.pane.getChildren().add(pane);

    }

    private void addButtons(){

        for(int i = 0; i < size; i++){

            redRow[i] = new TileButton(TileColor.RED, i);
            redRow[i].setLayoutX(33 + (i+1) * 46);
            redRow[i].setLayoutY(33);

            blueRow[i] = new TileButton(TileColor.BLUE, i);
            blueRow[i].setLayoutX(33 + 16 * 46);
            blueRow[i].setLayoutY(33 + (i+1) * 46);

            yellowRow[i] = new TileButton(TileColor.YELLOW, i);
            yellowRow[i].setLayoutX((33 + 16 * 46) - (i+1) * 46);
            yellowRow[i].setLayoutY(33 + 16 * 46);

            greenRow[i] = new TileButton(TileColor.GREEN, i);
            greenRow[i].setLayoutX(33);
            greenRow[i].setLayoutY((33 + size * 46) - (i+1) * 46);

            pane.getChildren().addAll(redRow[i], blueRow[i], yellowRow[i], greenRow[i]);

        }


        for(int i = size; i < redRow.length  - 1; i++){
            redRow[i] = new TileButton(TileColor.RED, i);
            redRow[i].setLayoutX(33 + 46 * 2);
            redRow[i].setLayoutY(33 + (i + 1 - size)* 46);

            blueRow[i] = new TileButton(TileColor.BLUE, i);
            blueRow[i].setLayoutX((33 + 15 * 46) - (i - size) * 46);
            blueRow[i].setLayoutY(33 + 46 * 2);

            yellowRow[i] = new TileButton(TileColor.YELLOW, i);
            yellowRow[i].setLayoutX(33 + 14 * 46);
            yellowRow[i].setLayoutY(33 + 16 * 46 - (i - size + 1) * 46);

            greenRow[i] = new TileButton(TileColor.GREEN, i);
            greenRow[i].setLayoutX(33 + (i + 1- size)* 46);
            greenRow[i].setLayoutY(33 + 46 * 14);

            //Scoring tile
            if(i == redRow.length -2){
                redRow[i].setPrefSize(92,92);
                redRow[i].setOccupiedBy(0);

                blueRow[i].setPrefSize(92,92);
                blueRow[i].setLayoutX(blueRow[i].getLayoutX() - 46);
                blueRow[i].setOccupiedBy(0);

                yellowRow[i].setPrefSize(92,92);
                yellowRow[i].setLayoutX(yellowRow[i].getLayoutX() - 46);
                yellowRow[i].setLayoutY(yellowRow[i].getLayoutY() - 46);
                yellowRow[i].setOccupiedBy(0);

                greenRow[i].setPrefSize(92,92);
                greenRow[i].setLayoutY(greenRow[i].getLayoutY() - 46);
                greenRow[i].setOccupiedBy(0);
            }

            pane.getChildren().addAll(redRow[i], blueRow[i], yellowRow[i], greenRow[i]);

        }

        //home area
        int last = 22;

        redRow[last] = new TileButton(TileColor.RED, last);
        redRow[last].setPrefSize(92,92);
        redRow[last].setLayoutX(46 * 4 + 33);
        redRow[last].setLayoutY(33 + 46);
        redRow[last].setOccupiedBy(4);

        blueRow[last] = new TileButton(TileColor.BLUE, last);
        blueRow[last] .setPrefSize(92, 92);
        blueRow[last] .setLayoutX(33 + 14 * 46);
        blueRow[last] .setLayoutY(33 + 46 * 4);
        blueRow[last] .setOccupiedBy(4);

        yellowRow[last]  = new TileButton(TileColor.YELLOW, last);
        yellowRow[last] .setPrefSize(92,92);
        yellowRow[last] .setLayoutX(33 + 11 * 46);
        yellowRow[last] .setLayoutY(33 + 14 * 46);
        yellowRow[last] .setOccupiedBy(4);

        greenRow[last] = new TileButton(TileColor.GREEN, last);
        greenRow[last] .setPrefSize(92,92);
        greenRow[last] .setLayoutX(33 + 46);
        greenRow[last] .setLayoutY(33 + 46 * 11);
        greenRow[last] .setOccupiedBy(4);

        pane.getChildren().addAll(redRow[last], blueRow[last], yellowRow[last], greenRow[last]);
    }

    public void onRefreshClick(){

        tableView.getItems().clear();;

        try {
            sorryClient.get_game_list();
            System.out.println("Button clicked");
            System.out.println("onRefreshClick: "+gamesList);
          //  gamesList = sorryClient.get_game_list();
         //   System.out.println(gamesList);
            //gamesList = "error";
        } catch (Exception e){
            gamesList = "error";
            System.out.println("Games list");
        }

        try {
            String sub = gamesList.substring(gamesList.indexOf("games\":[{") + 9, gamesList.length() - 4);
            String[] info = sub.split("}}},\\{");
            for (String all : info) {
                //System.out.println(all);
                String game_name = all.substring(1, all.indexOf("players") - 4);

                int nameSpot = all.indexOf("players");
                //String endSpot = all.substring(all.indexOf(nameSpot + 10), all.indexOf("\":", nameSpot));
                //System.out.println(nameSpot);
                String name = all.substring(nameSpot + 10);
                String names[] = name.split("\"");
                //System.out.println(game_name);

                String colors = "";

                if (all.contains("Red"))
                    colors += "R";
                if (all.contains("Blue"))
                    colors += "B";
                if (all.contains("Yellow"))
                    colors += "Y";
                if (all.contains("Green"))
                    colors += "G";

                System.out.println(colors);

                GameInfo room = new GameInfo(game_name, names[1], colors);
                tableView.getItems().add(room);
            }
        } catch(Exception e){
            System.out.println("caught exception");
        }



        /*
        if(!gamesList.equals("none")){
            String[] games = gamesList.split("\n");
            for (String game : games) {
                String[] gameData = game.split("\t");

            }
        }
        */

        //GameInfo game1 = new GameInfo("Logan's game", "logan",   "R");
        //tableView.getItems().add(game1);

        tableView.refresh();
    }
    public void onDraw(){

        if(!gameStarted){
            gameStarted = true;
            if(isHost)
                sorryClient.start_game(gameName);
        }

        if(!hasDrawn) {
            yourTurn.setText("Your Turn");

            gameLogic = sorryClient.getGame();
            moves.reset();
            Card drawn = gameLogic.drawCard();
            cardValue = drawn.getValue();
            setCurrentCardText(drawn.getValue() + "");
            setCurrentCardDescription(drawn.getDesc());
            hasDrawn = true;
        }
        //ArrayList<Board> moveList = drawn.getMoves(gameLogic.currentPlayer, gameLogic.board);
        /*for (TileButton t : moves.calculateMoves(redRow[3], TileColor.RED, drawn.getValue())) {
                    t.setId("red-calculateMoves-tile");
                    t.setOnAction(e -> {
                        moves.reset();
                    });
                }
*/
        //moves.displayMoves(TileColor.RED, drawn.getValue());


        //gameLogic.currentPlayer.pawns;
    }

    private void populateTableView(){
        TableColumn lobbyNameCol = new TableColumn("Lobby Name");
        lobbyNameCol.setCellValueFactory(new PropertyValueFactory<GameInfo,String>("lobbyName"));

        TableColumn hostNameCol = new TableColumn("Host Name");
        hostNameCol.setCellValueFactory(
                new PropertyValueFactory<GameInfo,String>("hostName")
        );

        TableColumn playersCol = new TableColumn("Players");
        playersCol.setCellValueFactory(
                new PropertyValueFactory<GameInfo,String>("players")
        );


        lobbyNameCol.setMinWidth(300);
        hostNameCol.setMinWidth(240);
        playersCol.setMinWidth(50);
        tableView.getColumns().addAll(lobbyNameCol, hostNameCol,  playersCol);
    }

    public String getCurrentCardText() {
        return currentCard.getText();
    }

    public void setCurrentCardText(String newCard) {
        this.currentCard.setText(newCard);
    }

    public void setCurrentCardDescription(String newDescription){
        this.currentCardDescription.setText(newDescription);
    }

    public void changecss(){
        //for each TileButton passed in
        /*
                //store in arraylist to clear later
                change css id to $playersColor-calculateMoves-tile
                set selected in that TileButton to true //TODO: have it change some variable to its position
                on selected click reset every tile in arraylist, then remove them
         */
    }

    public static TileButton[] getRedRow() {
        return redRow;
    }

    public static TileButton[] getBlueRow() {
        return blueRow;
    }

    public static TileButton[] getGreenRow() {
        return greenRow;
    }

    public static TileButton[] getYellowRow() {
        return yellowRow;
    }

    public Text getCurrentCard() {
        return currentCard;
    }

    public static class GameInfo{

        private final SimpleStringProperty lobbyName;
        private final SimpleStringProperty hostName;
        private final SimpleStringProperty players;

        private GameInfo(String lobbyName, String hostName, String players){
            this.lobbyName = new SimpleStringProperty(lobbyName);
            this.hostName = new SimpleStringProperty(hostName);
            this.players = new SimpleStringProperty(players);
        }

        public String getLobbyName() {
            return lobbyName.get();
        }

        public String getHostName() {
            return hostName.get();
        }


        public String getPlayers() {
            return players.get();
        }

        public SimpleStringProperty lobbyNameProperty() {
            return lobbyName;
        }

        public SimpleStringProperty hostNameProperty() {
            return hostName;
        }


        public SimpleStringProperty playersProperty() {
            return players;
        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}



