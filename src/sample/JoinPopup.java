package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class JoinPopup {

    private static String chosenColor;
    private static String gameName;

    private static TextField gameNameField;

    static void display(boolean host){

        final ToggleGroup radioGroup = new ToggleGroup();

        RadioButton red = new RadioButton("Red");
        RadioButton blue = new RadioButton("Blue");
        RadioButton yellow = new RadioButton("Yellow");
        RadioButton green = new RadioButton("Green");

        red.setToggleGroup(radioGroup);
        blue.setToggleGroup(radioGroup);
        yellow.setToggleGroup(radioGroup);
        green.setToggleGroup(radioGroup);


        Stage popup = new Stage();
        popup.initStyle(StageStyle.DECORATED);
        popup.setResizable(false);

        // disables user input on other windows
        popup.initModality(Modality.APPLICATION_MODAL);

        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new Insets(10,10,10,10));

        Region blankSpace = new Region();
        blankSpace.setMinHeight(30);

        HBox bottomLine = new HBox();
        bottomLine.setPadding(new Insets(0,0,0,0));
        bottomLine.setSpacing(10);

        Button enterButton = new Button("GO");
        enterButton.setOnAction(e -> {

                    RadioButton selected = (RadioButton) radioGroup.getSelectedToggle();


                    try {
                        chosenColor = selected.getText();
                    } catch (NullPointerException ex){
                        chosenColor = "none";
                    } finally{
                        popup.close();
                    }

                    if(host){
                        try {
                            gameName = gameNameField.getText();
                        } catch(NullPointerException ex){
                            gameName = "Creative Name";
                        }
                    }
            }
        );

        int width = 200;
        int height = 150;

        if(host){
            gameNameField = new TextField();
            gameNameField.setPromptText("Game Name");
            width = 220;
            height = 155;
            bottomLine.getChildren().add(gameNameField);
        }

        bottomLine.getChildren().add(enterButton);

        content.getChildren().addAll(red,blue,yellow,green, bottomLine);



        Scene scene = new Scene(content, width, height);
        popup.setScene(scene);

        popup.setOnCloseRequest(e -> chosenColor = "none");

        enterButton.requestFocus();

        popup.setTitle("Choose Your Color!");

        //disables other windows
        popup.showAndWait();
    }

    public static String getChosenColor(){
        return chosenColor;
    }

    public static String getGameName(){
        return  gameName;
    }
}
