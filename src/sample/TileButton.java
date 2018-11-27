package sample;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileButton extends Button {
    private Image picture;
    private TileColor c;
    private int spot;

    public TileButton(Image picture, TileColor c, int spot){
        this.picture = picture;
        ImageView imageView = new ImageView(picture);
        imageView.setFitHeight(28);
        imageView.setFitWidth(28);
        this.setGraphic(imageView);

        this.setPrefSize(46,46);

        this.setOnAction(e -> System.out.println(spot));

        this.c = c;
        this.setId(c.name().toLowerCase() + "-tile");

        this.spot = spot;

        this.setPrefSize(46,46);
    }

    public TileButton(TileColor c, int spot){
        this.c = c;
        String css = this.getClass().getResource("application.css")
                .toExternalForm();
        this.getStylesheets().add(css);
        this.setId(c.name().toLowerCase() + "-tile");

        this.spot = spot;

        //this.setText(spot+ "");
        this.setOnAction(e -> System.out.println(spot));

        this.setPrefSize(46,46);
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
        ImageView imageView = new ImageView(picture);
        imageView.setFitHeight(28);
        imageView.setFitWidth(28);
        this.setGraphic(imageView);
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


