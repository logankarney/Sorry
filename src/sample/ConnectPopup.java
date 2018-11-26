package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public final class ConnectPopup {

    static String username;
    static String port;

    static void display() {
        Stage popup = new Stage();
        popup.initStyle(StageStyle.DECORATED);
        popup.setResizable(false);

        // disables user input on other windows
        popup.initModality(Modality.APPLICATION_MODAL);

        VBox content = new VBox();
        content.setSpacing(10);
        content.setPadding(new Insets(10,10,10,10));

        TextField nameField = new TextField();
        nameField.setPromptText("Username");

        TextField portField = new TextField();
        portField.setPromptText("PortNumber");

        Region blankSpace = new Region();
        blankSpace.setMinHeight(30);

        HBox buttonContent = new HBox();

        Button enterButton = new Button("ENTER");
        enterButton.setOnAction(e -> {
            if(!nameField.getText().isEmpty())
                username = nameField.getText();
            else
                username = "Player";
            try {
                int validPort = Integer.parseInt(portField.getText());
                port = validPort + "";
            } catch (NumberFormatException ex){
                port = "12000";
            }

            popup.close();
        });

        Button cancelButton = new Button("CANCEL");
        cancelButton.setOnAction(e -> {System.exit(0);});

        buttonContent.getChildren().addAll(enterButton, cancelButton);
        buttonContent.setPadding(new Insets(0,0,0,50));
        buttonContent.setSpacing(10);



        content.getChildren().addAll(nameField, portField, blankSpace, buttonContent);

        Scene scene = new Scene(content, 300, 150);
        popup.setScene(scene);

        enterButton.requestFocus();

        popup.setTitle("Sorry!");
        popup.setOnCloseRequest(e -> System.exit(0));

        //disables other windows
        popup.showAndWait();
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ConnectPopup.username = username;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        ConnectPopup.port = port;
    }
}
