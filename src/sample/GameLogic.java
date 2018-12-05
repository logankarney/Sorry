package sample;

import sample.card.*;

import java.util.ArrayList;

/**
 * Keeps game state, player list, and controls underlying playing of the game
 */
public class GameLogic{
    protected ArrayList<Player> players;
    protected Board board;
    protected Player currentPlayer;
    private int currentNumPlayers;

    public GameLogic(){
        players = new ArrayList<>();
        currentNumPlayers = 0;
    }

    /**
     * Add a player to the game
     * */
    public void addPlayer(Player player){
        if (currentNumPlayers < 4){
            for (Player p: players){
                if (p.getPlayerID() == player.getPlayerID()){
                    System.out.println("Color taken.");
                    return;
                }
            }
            players.add(player);
            currentNumPlayers++;
        }else{
            System.out.println("Too many players!");
        }
    }

    /**
     * Determines the winner of the game, if any
     */
    private Player winner(){
        for (Player pl: players){
            int pawnsHome = 0;
            for (Pawn p: pl.getPawns()){
                if (p.isHome()){
                    pawnsHome++;
                }
            }
            if (pawnsHome == 4){
                return pl;
            }
        }
        return null;
    }

    /**
     * Main gameplay loop
     */
    private void play(){
        while (winner() == null){
            Card card = drawCard();
            ArrayList<Board> possibleMoves = card.getMoves(currentPlayer,board);
//TODO
        }
    }

    public void startGame(){
        if (currentNumPlayers < 1){
            return;
        }
        Player[] playerArray = new Player[4];
        for (Player p : players){
            playerArray[p.getPlayerID()] = p;
        }
        board = new Board(playerArray, currentNumPlayers);
        currentPlayer = players.get(0);
    }

    public void nextTurn(){
        int i = players.indexOf(currentPlayer);
        if (i == players.size()-1){
            currentPlayer = players.get(0);
        }else{
            currentPlayer = players.get(i);
        }
    }

    public void saveMove(Board b){
        board = b;
    }

    /**
     * Randomly pick a card
     * @return
     */
    public Card drawCard(){
        int cardNum = (int)(Math.random() * 12 + 1);
        Card card;
        switch (cardNum){
            case 1:
                card = new OneCard();
                break;
            case 2:
                card = new TwoCard();
                break;
            case 4:
                card = new FourCard();
                break;
            case 6:
                card = drawCard();
                break;
            case 9:
                card = drawCard();
                break;
            case 10:
                card = new TenCard();
                break;
            case 11:
                card = new ElevenCard();
                break;
            case 13:
                card = new SorryCard();
                break;
            default:
                card = new StandardCard(cardNum);
                break;
        }

        return card;
    }

    public Board getBoard() {
        return board;
    }


/*public static void main(String[] args){
    GameLogic game = new GameLogic();
    game.addPlayer(new Player("Ryan", TileColor.BLUE));
    game.addPlayer(new Player("Em", TileColor.GREEN));
    game.addPlayer(new Player("Tanner", TileColor.YELLOW));
    game.addPlayer(new Player("Logan", TileColor.RED));

    game.startGame();

}*/


}