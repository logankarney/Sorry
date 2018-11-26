package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public final class ConnectPopup {

    static String username;
    static int port;

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
        blankSpace.setMinHeight(50);

        Button enterButton = new Button("ENTER");
        enterButton.setOnAction(e -> {
            if(!nameField.getText().isEmpty())
                username = nameField.getText();
            else
                username = "Player";
            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex){
                port = 12000;
            }

            popup.close();
        });

        content.getChildren().addAll(nameField, portField, blankSpace, enterButton);

        Scene scene = new Scene(content, 300, 200);
        popup.setScene(scene);

        enterButton.requestFocus();

        popup.setTitle("Sorry!");
        //disables other windows
        popup.showAndWait();
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ConnectPopup.username = username;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        ConnectPopup.port = port;
    }
}
