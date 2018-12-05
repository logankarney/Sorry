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
        pawns[0] = new Pawn(color, playerID, 0);
        pawns[1] = new Pawn(color, playerID, 1);
        pawns[2] = new Pawn(color, playerID, 2);
        pawns[3] = new Pawn(color, playerID, 3);
    }

    public Player(Player pl){
        this.name = pl.getName();
        this.color = pl.getColor();
        this.playerID = pl.getPlayerID();
        pawns = new Pawn[4];
        for (int i=0; i<4; i++){
            pawns[i] = new Pawn(pl.getPawns()[i]);
        }
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