package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class Controller extends Application {
    private Stage stage;

    @FXML AnchorPane pane;

    @FXML private Button hostButton;
    @FXML private Button joinButton;
    @FXML private Button optionsButton;

    @FXML private Button backButton;

    private MediaPlayer mediaPlayer;
    private Media sound;

    private int port;
    private String playerName;

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setTitle("Sorry!");
        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        //Gets player's preferred username and the server's port number
        ConnectPopup.display();

    }


    @FXML public void initialize(){
        sound = new Media(new File("src/res/AIRHORN.mp3").toURI().toString());
    }


    @FXML private void mainMenuButtonClicked(Event e){
        System.out.println(e.getSource());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.stop();
        mediaPlayer.play();

        if(e.getSource() == hostButton){
            changeFXML("hostgame.fxml");
        } else if(e.getSource() == joinButton){

        } else if(e.getSource() == optionsButton){

        } else if(e.getSource() == backButton){
            changeFXML("main.fxml");
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

    public static void main(String[] args) {
        launch(args);
    }
}
