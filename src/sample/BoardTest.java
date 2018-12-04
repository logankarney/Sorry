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
    public void tryMoveFromStart() throws Exception {
        Board b = makeBoard();
        Board b2 = new Board(b);
        Pawn p = b.getPlayers()[1].getPawns()[3];
        b.move(p, 10);
        Assert.assertArrayEquals(b2.getSorryBoard(), b.getSorryBoard());
    }

    @org.junit.Test
    public void moveThree() throws Exception {
        Board b = makeBoard();
        Pawn p = b.getPlayers()[1].getPawns()[3];
        b.moveFromStart(p);
        b.move(p, 3);
        Assert.assertEquals(p, b.getSorryBoard()[1][6]);
    }

    @org.junit.Test
    public void moveFromStart() throws Exception {
        Board b = makeBoard();
        Pawn p = b.getPlayers()[1].getPawns()[3];
        b.moveFromStart(p);
        Assert.assertEquals(b.getSorryBoard()[1][3], p);
    }

    @org.junit.Test
    public void moveFromStartOnEnemy() throws Exception {
        Board b = makeBoard();
        Pawn e = b.getPlayers()[0].getPawns()[1];
        Pawn p = b.getPlayers()[1].getPawns()[3];
        b.moveFromStart(e);
        b.setPawnLocation(1,3,e);
        b.moveFromStart(p);
        //Assert.assertEquals(b.getSorryBoard()[1][3], p);
        Assert.assertTrue(e.isInStart());
        Assert.assertEquals(b.getStart()[0].get(3), e);
    }
    @org.junit.Test
    public void moveFromStartOnAlly() throws Exception {
        Board b = makeBoard();
        Pawn p1 = b.getPlayers()[1].getPawns()[3];
        Pawn p2 = b.getPlayers()[1].getPawns()[1];

        b.moveFromStart(p1);
        Assert.assertTrue(!b.moveFromStart(p2));
        Assert.assertEquals(b.getSorryBoard()[1][3], p1);
    }

    @org.junit.Test
    public void setPawnLocation() throws Exception {
        Board board1 = makeBoard();
        Pawn p = board1.getPlayers()[0].getPawns()[0];
        board1.setPawnLocation(2, 13, p);
        Assert.assertEquals(board1.getSorryBoard()[2][13],p);
        Assert.assertEquals(p.getRow(), 2);
        Assert.assertEquals(p.getSpace(), 13);
    }

    private Board makeBoard(){
        return new Board(new Player[]{new Player("A",TileColor.GREEN),
                new Player("A",TileColor.RED),
                new Player("A",TileColor.BLUE),
                new Player("A",TileColor.YELLOW)},4);
    }

}