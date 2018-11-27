package sample;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class TileButton extends Button {
    private ImageView picture;
    private TileColor c;
    private int spot;

   /* public TileButton(ImageView picture, TileColor c, int spot){
        this.picture = picture;
        this.getChildren().add(picture);

        this.c = c;
        this.setId(c.name().toLowerCase() + "-tile");

        this.spot = spot;

        this.setPrefSize(46,46);
    } */

    public TileButton(TileColor c, int spot){
        this.c = c;
        String css = this.getClass().getResource("application.css")
                .toExternalForm();
        this.getStylesheets().add(css);
        this.setId(c.name().toLowerCase() + "-tile");

        this.spot = spot;

        this.setText(spot+ "");
        this.setOnAction(e -> System.out.println(spot));

        this.setPrefSize(46,46);
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
        this.getChildren().remove(0);
        this.getChildren().add(picture);
    }

    public TileColor getC() {
        return c;
    }

    public void setC(TileColor c) {
        this.c = c;
        this.setId("." + c.name().toLowerCase() + "-tile");
    }

    public int getSpot() {
        return spot;
    }

    public void setSpot(int spot) {
        this.spot = spot;
    }
}


