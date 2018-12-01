package sample;

public class Board {
    private static Board single_instance = null;

    private Board(){

    }

    public static Board getInstance(){
        if (single_instance == null){
            single_instance = new Board();
        }
        return single_instance;
    }
}
