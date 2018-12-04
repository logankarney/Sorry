package sample;

public class Player{
    private String name;
    private TileColor color;
    protected Pawn[] pawns;
    private int playerID;

    public Player(String name, TileColor color){
        this.name = name;
        this.color = color;
        if (color == TileColor.GREEN){
            playerID = 0;
        }else if (color == TileColor.RED){
            playerID = 1;
        }else if (color == TileColor.BLUE){
            playerID = 2;
        }else if (color == TileColor.YELLOW){
            playerID = 3;
        }
        pawns = new Pawn[4];
        pawns[0] = new Pawn(color, playerID);
        pawns[1] = new Pawn(color, playerID);
        pawns[2] = new Pawn(color, playerID);
        pawns[3] = new Pawn(color, playerID);
    }

    public int getPlayerID(){
        return playerID;
    }

    public String getName() {
        return name;
    }

    public TileColor getColor() {
        return color;
    }

    public Pawn[] getPawns() {
        return pawns;
    }
}