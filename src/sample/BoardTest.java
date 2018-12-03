package sample;

import org.junit.Assert;

import static org.junit.Assert.*;

/***********************************************
 *
 * @author Ryan Jones 
 * @version 12/3/2018
 ***********************************************/
public class BoardTest {
    @org.junit.Test
    public void move() throws Exception {

    }

    @org.junit.Test
    public void returnToStart() throws Exception {

    }

    @org.junit.Test
    public void moveFromStart() throws Exception {

    }

    @org.junit.Test
    public void setPawnLocation() throws Exception {
        Player[] players = new Player[]{new Player("A",TileColor.GREEN),
                new Player("A",TileColor.RED),
                new Player("A",TileColor.BLUE),
                new Player("A",TileColor.YELLOW)};
        Board board1 = new Board(players, 4);
        Pawn p = board1.getPlayers()[0].getPawns()[0];
        board1.setPawnLocation(2, 13, p);
        Assert.assertEquals(board1.getSorryBoard()[2][13],p);
        Assert.assertEquals(p.getRow(), 2);
        Assert.assertEquals(p.getSpace(), 13);
    }

}