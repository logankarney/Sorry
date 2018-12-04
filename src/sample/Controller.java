package sample;

import javafx.application.Application;
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

import java.net.InetAddress;

public class Controller extends Application {

    @FXML AnchorPane pane;


    @FXML private TableView<GameInfo> tableView = new TableView<>();

    @FXML private Button hostButton, joinButton, refreshButton;

    @FXML private Button cardButton;

    @FXML private Text currentCard, yourTurn;

    @FXML private TextArea currentCardDescription;

    private static MediaPlayer backgroundPlayer, buttonPlayer;
    private static Media backgroundSound, buttonSound;

    private static String port, playerName, gameName;

    /** The outside rows for each color */
    private static TileButton[] redRow, blueRow, greenRow, yellowRow;

    /** the inside lane for each color */
    private static TileButton[] redHome, blueHome, greenHome, yellowHome;

    /** The spawn for each color, red's is 0, blue's is 1, ect */
    private static TileButton[] spawns;

    protected static TileColor playerColor = null;

    private static boolean firstTime = true;

    private final int size = 16;

    protected Image redPiece, bluePiece, yellowPiece, greenPiece;

    private static SorryClient sorryBoard;

    protected static boolean playersTurn = false;


    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setTitle("Sorry!");
        Scene scene = new Scene(root, 1280, 848);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

       // backgroundSound = new Media(new File("src/res/bensound-hipjazz.mp3").toURI().toString());
        //backgroundPlayer = new MediaPlayer(backgroundSound);
       // backgroundPlayer.play();

        //https://stackoverflow.com/questions/43190594/javafx-mediaplayer-loop
        //  answer by Berke Bakar
      //  backgroundPlayer.setOnEndOfMedia(() -> {
       //     backgroundPlayer.seek(Duration.ZERO);
      //      backgroundPlayer.play();
        //});



    //    buttonSound = new Media(new File("src/res/HITMARKER.mp3").toURI().toString());
      //  buttonPlayer = new MediaPlayer(buttonSound);
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
            redRow = new TileButton[size];
            redHome = new TileButton[7];

            blueRow = new TileButton[size];
            blueHome = new TileButton[7];

            yellowRow = new TileButton[size];
            yellowHome = new TileButton[7];

            greenRow = new TileButton[size];
            greenHome = new TileButton[7];

            spawns = new TileButton[4];

            sorryBoard = new SorryClient(this);
            populateTableView();
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
           yourTurn.setText("Your Turn");
           onDraw();
        }

        else if(e.getSource() == joinButton){
            try {
                GameInfo chosenGame = tableView.getSelectionModel().getSelectedItem();
                JoinPopup.display(false, chosenGame.getPlayers());

                String chosenColor = JoinPopup.getChosenColor();

                if(chosenColor.equals("none"))
                    return;

                try {
                    InetAddress address = InetAddress.getByName(chosenGame.getHostIP());
                    sorryBoard.connect(address,Integer.parseInt(port));

                    sorryBoard.register_user(playerName);
                    sorryBoard.join_game(chosenColor, chosenGame.getLobbyName());
                } catch(Exception ex){
                    //ex.printStackTrace();
                }

                changeFXML("game.fxml");
                addButtons();

            } catch(NullPointerException ex){
            }


       }

       else if(e.getSource() == refreshButton){
            onRefreshClick();
       }

       else if(e.getSource() == hostButton) {

           try {
               InetAddress inetAddress = InetAddress.getByName("127.0.0.1");

               JoinPopup.display(true, null);
               String chosenColor = JoinPopup.getChosenColor();
               gameName = JoinPopup.getGameName();
               if(chosenColor.equals("none") || gameName.equals("no game name chosen"))
                   return;


               //sorryBoard.register_user(playerName);
               //sorryBoard.join_game(chosenColor, playerName);

               changeFXML("game.fxml");

               addButtons();

               sorryBoard.connect(inetAddress, Integer.parseInt(port));
               sorryBoard.register_user(playerName);
               sorryBoard.create_game(gameName, chosenColor);
           } catch(Exception ex){
               //e.printStacktrace();
           }

       }

       /*else if(e.getSource() == toggleCSSButton){
           greenRow[3].setPicture(greenPiece);
           if(greenRow[3].getId().equals("yellow-tile"))
           greenRow[3].setId("yellow-move-tile");
           else
               greenRow[3].setId("yellow-tile");
       }*/

       /*else if(e.getSource() == startButton){
            String gameName = JoinPopup.getGameName();
            sorryBoard.start_game(gameName);
            removeStartButton();
       }*/

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

        int offset = 16;

        for(int i = size; i < redHome.length + offset - 1; i++){
            redHome[i - offset] = new TileButton(TileColor.RED, i);
            redHome[i - offset].setLayoutX(33 + 46 * 2);
            redHome[i - offset].setLayoutY(33 + (i + 1 - size)* 46);

            blueHome[i - offset] = new TileButton(TileColor.BLUE, i);
            blueHome[i - offset].setLayoutX((33 + 15 * 46) - (i - size) * 46);
            blueHome[i - offset].setLayoutY(33 + 46 * 2);

            yellowHome[i - offset] = new TileButton(TileColor.YELLOW, i);
            yellowHome[i - offset].setLayoutX(33 + 14 * 46);
            yellowHome[i - offset].setLayoutY(33 + 16 * 46 - (i - size + 1) * 46);

            greenHome[i - offset] = new TileButton(TileColor.GREEN, i);
            greenHome[i - offset].setLayoutX(33 + (i + 1- size)* 46);
            greenHome[i - offset].setLayoutY(33 + 46 * 14);

            //Scoring tile
            if(i == redHome.length + offset - 2){
                redHome[i - offset].setPrefSize(92,92);
                redHome[i - offset].setOccupiedBy(0);

                blueHome[i - offset].setPrefSize(92,92);
                blueHome[i - offset].setLayoutX(blueHome[i - offset].getLayoutX() - 46);
                blueHome[i - offset].setOccupiedBy(0);

                yellowHome[i - offset].setPrefSize(92,92);
                yellowHome[i - offset].setLayoutX(yellowHome[i - offset].getLayoutX() - 46);
                yellowHome[i - offset].setLayoutY(yellowHome[i - offset].getLayoutY() - 46);
                yellowHome[i - offset].setOccupiedBy(0);

                greenHome[i - offset].setPrefSize(92,92);
                greenHome[i - offset].setLayoutY(greenHome[i - offset].getLayoutY() - 46);
                greenHome[i - offset].setOccupiedBy(0);
            }

            pane.getChildren().addAll(redHome[i - offset], blueHome[i - offset], yellowHome[i - offset], greenHome[i - offset]);

        }

        //home area
        int last = 22;

        spawns[0] = new TileButton(TileColor.RED, last);
        spawns[0].setPrefSize(92,92);
        spawns[0].setLayoutX(46 * 4 + 33);
        spawns[0].setLayoutY(33 + 46);
        spawns[0].setOccupiedBy(4);

        spawns[1] = new TileButton(TileColor.BLUE, last);
        spawns[1].setPrefSize(92, 92);
        spawns[1].setLayoutX(33 + 14 * 46);
        spawns[1].setLayoutY(33 + 46 * 4);
        spawns[1].setOccupiedBy(4);

        spawns[2] = new TileButton(TileColor.YELLOW, last);
        spawns[2].setPrefSize(92,92);
        spawns[2].setLayoutX(33 + 11 * 46);
        spawns[2].setLayoutY(33 + 14 * 46);
        spawns[2].setOccupiedBy(4);

        spawns[3] = new TileButton(TileColor.GREEN, last);
        spawns[3].setPrefSize(92,92);
        spawns[3].setLayoutX(33 + 46);
        spawns[3].setLayoutY(33 + 46 * 11);
        spawns[3].setOccupiedBy(4);

        pane.getChildren().addAll(spawns[0], spawns[1], spawns[2], spawns[3]);
    }

    public void onRefreshClick(){
        String gamesList;
        try {
            gamesList = sorryBoard.get_game_list();
            System.out.println(gamesList);
            gamesList = "error";
        } catch (Exception e){
            gamesList = "error";
        }

        /*
        if(!gamesList.equals("none")){
            String[] games = gamesList.split("\n");
            for (String game : games) {
                String[] gameData = game.split("\t");
                GameInfo room = new FileInfo(gameData[0], gameData[1], ect.);
                table.getItems().add(room);
            }
        }
        */

        GameInfo game1 = new GameInfo("Logan's game", "logan",   "127.0.0.1","R");
        tableView.getItems().add(game1);

        tableView.refresh();
    }

    public void onDraw(){

        Card drawn = sorryBoard.getGame().drawCard();
        setCurrentCardText(drawn.getValue() + "");
        setCurrentCardDescription(drawn.getDesc());

        //TODO: disable drawing of cards, get valid moves
    }

    private void populateTableView(){
        TableColumn lobbyNameCol = new TableColumn("Lobby Name");
        lobbyNameCol.setCellValueFactory(new PropertyValueFactory<GameInfo,String>("lobbyName"));

        TableColumn hostNameCol = new TableColumn("Host Name");
        hostNameCol.setCellValueFactory(
                new PropertyValueFactory<GameInfo,String>("hostName")
        );

        TableColumn hostIPCol = new TableColumn("Host IP");
        hostIPCol.setCellValueFactory(
                new PropertyValueFactory<GameInfo,String>("hostIP")
        );

        TableColumn playersCol = new TableColumn("Players");
        playersCol.setCellValueFactory(
                new PropertyValueFactory<GameInfo,String>("players")
        );


        lobbyNameCol.setMinWidth(300);
        hostNameCol.setMinWidth(240);
        hostIPCol.setMinWidth(200);
        playersCol.setMinWidth(50);
        tableView.getColumns().addAll(lobbyNameCol, hostNameCol, hostIPCol, playersCol);
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

    public static TileButton[] getRedHome() {
        return redHome;
    }

    public static TileButton[] getBlueHome() {
        return blueHome;
    }

    public static TileButton[] getGreenHome() {
        return greenHome;
    }

    public static TileButton[] getYellowHome() {
        return yellowHome;
    }

    public static TileButton[] getSpawns() {
        return spawns;
    }

    public static class GameInfo{

        private final SimpleStringProperty lobbyName;
        private final SimpleStringProperty hostName;
        private final SimpleStringProperty hostIP;
        private final SimpleStringProperty players;

        private GameInfo(String lobbyName, String hostName, String hostIP, String players){
            this.lobbyName = new SimpleStringProperty(lobbyName);
            this.hostName = new SimpleStringProperty(hostName);
            this.hostIP = new SimpleStringProperty(hostIP);
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

        public String getHostIP() {
            return hostIP.get();
        }

        public SimpleStringProperty hostIPProperty() {
            return hostIP;
        }

        public SimpleStringProperty playersProperty() {
            return players;
        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}



