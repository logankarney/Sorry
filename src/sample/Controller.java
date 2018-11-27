package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class Controller extends Application {
    private Stage stage;

    @FXML AnchorPane pane;

    @FXML private TextField playerNameField;
    @FXML private TextField serverPortField;

    @FXML private Button joinButton;

    private MediaPlayer mediaPlayer;
    private Media sound;

    private String port;
    private String playerName;

    private TileButton[] redRow, blueRow, greenRow, yellowRow;

    private static boolean firstTime = true;

    private final int size = 16;

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setTitle("Sorry!");
        Scene scene = new Scene(root, 1280, 900);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    @FXML public void initialize(){
        sound = new Media(new File("src/res/AIRHORN.mp3").toURI().toString());

        //prevents ConnectPopup.display() from firing every time the scene changes
        if(firstTime) {
            //Gets player's preferred username and the server's port number
            ConnectPopup.display();


            playerName = ConnectPopup.getUsername();
            port = ConnectPopup.getPort();

            playerNameField.setText(playerName);
            serverPortField.setText(port);

            firstTime = false;
        }
    }


    @FXML private void mainMenuButtonClicked(Event e){
        System.out.println(e.getSource());
        //mediaPlayer = new MediaPlayer(sound);
       // mediaPlayer.stop();
      //  mediaPlayer.play();


        //saving for later use
       if(e.getSource() == joinButton) {
           changeFXML("game.fxml");
           addButtons();
           //pane.getChildren().clear();
           //TileButton button = new TileButton(TileColor.RED);
           //pane.getChildren().add(button);
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

    protected void setPane(AnchorPane pane){
        this.pane.getChildren().clear();
        this.pane.getChildren().add(pane);

    }

    private void addButtons(){
        //size = amount of tiles in rows, 5 for home, 1 for finish, 1 for spawn
        redRow = new TileButton[size + 7];
        blueRow = new TileButton[size + 7];
        yellowRow = new TileButton[size + 7];
        greenRow = new TileButton[size + 7];

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

        for(int i = size; i < redRow.length - 1; i++){
            redRow[i] = new TileButton(TileColor.RED, i);
            redRow[i].setLayoutX(33 + 46 * 2);
            //redRow[i].setLayoutY(33 + (i + 2) * 46);
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

            if(i == redRow.length - 2){
                redRow[i].setPrefSize(92,92);

                blueRow[i].setPrefSize(92,92);
                blueRow[i].setLayoutX(blueRow[i].getLayoutX() - 46);

                yellowRow[i].setPrefSize(92,92);
                yellowRow[i].setLayoutX(yellowRow[i].getLayoutX() - 46);
                yellowRow[i].setLayoutY(yellowRow[i].getLayoutY() - 46);

                greenRow[i].setPrefSize(92,92);
                greenRow[i].setLayoutY(greenRow[i].getLayoutY() - 46);
            }

            pane.getChildren().addAll(redRow[i], blueRow[i], yellowRow[i], greenRow[i]);
        }

        int last = redRow.length - 1;

        redRow[last] = new TileButton(TileColor.RED, last);
        redRow[last].setPrefSize(92,92);
        redRow[last].setLayoutX(46 * 4 + 33);
        redRow[last].setLayoutY(33 + 46);

        blueRow[last] = new TileButton(TileColor.BLUE, last);
        blueRow[last].setPrefSize(92, 92);
        blueRow[last].setLayoutX(33 + 14 * 46);
        blueRow[last].setLayoutY(33 + 46 * 4);

        yellowRow[last] = new TileButton(TileColor.YELLOW, last);
        yellowRow[last].setPrefSize(92,92);
        yellowRow[last].setLayoutX(33 + 11 * 46);
        yellowRow[last].setLayoutY(33 + 14 * 46);

        greenRow[last] = new TileButton(TileColor.GREEN, last);
        greenRow[last].setPrefSize(92,92);
        greenRow[last].setLayoutX(33 + 46);
        greenRow[last].setLayoutY(33 + 46 * 11);

        pane.getChildren().addAll(redRow[last], blueRow[last], yellowRow[last], greenRow[last]);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
