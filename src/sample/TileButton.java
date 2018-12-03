package sample;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TileButton extends Button implements ActionListener{
    private Image picture;
    private TileColor c;
    private int spot;
    private static boolean selected = false;
    private int occupiedBy = 0;

    /*public TileButton(Image picture, TileColor c, int spot){
        this.picture = picture;
        ImageView imageView = new ImageView(picture);
        imageView.setFitHeight(28);
        imageView.setFitWidth(28);
        this.setGraphic(imageView);

        this.setPrefSize(46,46);

        this.c = c;
        this.setId(c.name().toLowerCase() + "-tile");

        this.spot = spot;
        this.selected = false;
        this.setPrefSize(46,46);
    }*/

    public TileButton(TileColor c, int spot){
        this.c = c;
        String css = this.getClass().getResource("application.css")
                .toExternalForm();
        this.getStylesheets().add(css);
        this.setId(c.name().toLowerCase() + "-tile");

        this.spot = spot;

        //this.setText(spot+ "");
        this.setOnAction(e ->{
            if(Controller.playersTurn ) {
                if(this.picture != null) {
                    //TODO: call method to get valid moves, change selected to true, change css of valid moves
                }
                else{
                    if(selected){
                        //TODO: return this tile as the next move
                    }
                }
            }
            System.out.println(spot);
        });

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

    public int getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(int occupiedBy) {
        if(this.spot > 20)
            this.setText(occupiedBy + "");
        this.occupiedBy = occupiedBy;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!selected){
            selected = true;
        }else{
    //TODO
        }
    }

    public boolean isSelected(){
        return selected;
    }
}


