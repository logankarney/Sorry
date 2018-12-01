package sample;

public class Player{
    private String name;
    private TileColor color;
    private Pawn[] pawns;
    private int playerID;

    public Player(String name, TileColor color, int playerID){
        this.name = name;
        this.color = color;
        this.playerID = playerID;
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