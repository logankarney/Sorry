package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller extends Application {
    private Stage stage;

    @FXML private Button hostButton;
    @FXML private Button joinButton;
    @FXML private Button optionsButton;

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setTitle("Sorry!");
        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    @FXML public void initialize(){
    }


    @FXML private void mainMenuButtonClicked(Event e){
        System.out.println(e.getSource());
        if(e.getSource() == hostButton){
            changeFXML("hostgame.fxml");
        } else if(e.getSource() == joinButton){

        } else if(e.getSource() == optionsButton){

        }
    }

    /** saving for dealing with pieces later **/
    @FXML private void onMouseOver(){
        System.out.println("Mouse entered");
    }


    private void changeFXML(String docName){
        //TODO: Fix way of changing FXML documents/controllers
        //https://codereview.stackexchange.com/questions/119418/javafx-fxml-window-switcher
        try {

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
