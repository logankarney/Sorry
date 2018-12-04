package sample;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.ExcludeCategories;

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
    public void shortSlideNoCollide() throws Exception {
        Board b = makeBoard();
        Pawn p = b.getPlayers()[0].getPawns()[0];
        b.moveFromStart(p);
        b.move(p, 12);
        Assert.assertEquals(p, b.getSorryBoard()[1][3]);
    }

    @org.junit.Test
    public void ownSlide() throws Exception {
        Board b = makeBoard();
        Pawn p = b.getPlayers()[0].getPawns()[0];
        b.moveFromStart(p);
        b.move(p, 5);
        Assert.assertEquals(p, b.getSorryBoard()[0][8]);
    }

    @org.junit.Test
    public void longSlideNoCollide() throws Exception {
        Board b = makeBoard();
        Pawn p = b.getPlayers()[0].getPawns()[0];
        b.moveFromStart(p);
        b.move(p, 20);
        Assert.assertEquals(p, b.getSorryBoard()[1][12]);
    }

    @org.junit.Test
    public void slideCollideEnemy() throws Exception{
        Board b = makeBoard();
        Pawn p = b.getPlayers()[0].getPawns()[0];
        Pawn e = b.getPlayers()[1].getPawns()[0];
        b.moveFromStart(p);
        b.moveFromStart(e);
        b.move(p, 12);
        Assert.assertEquals(p, b.getSorryBoard()[1][3]);
        Assert.assertEquals(e, b.getStart()[1].get(3));
    }

    @org.junit.Test
    public void slideCollideAlly() throws Exception{
        Board b = makeBoard();
        Pawn p = b.getPlayers()[0].getPawns()[0];
        Pawn a = b.getPlayers()[0].getPawns()[1];
        b.moveFromStart(p);
        b.move(p, 13);
        b.moveFromStart(a);
        b.move(a, 12);
        Assert.assertEquals(a, b.getSorryBoard()[1][3]);
        Assert.assertEquals(p, b.getStart()[0].get(2));
    }

    @org.junit.Test
    public void slideCollideBoth() throws Exception{
        Board b = makeBoard();
        Pawn p = b.getPlayers()[0].getPawns()[0];
        Pawn a = b.getPlayers()[0].getPawns()[1];
        Pawn e = b.getPlayers()[1].getPawns()[0];
        b.moveFromStart(e);
        b.moveFromStart(p);
        b.move(p, 13);
        b.moveFromStart(a);
        b.move(a, 12);
        Assert.assertEquals(a, b.getSorryBoard()[1][3]);
        Assert.assertEquals(p, b.getStart()[0].get(2));
        Assert.assertEquals(e, b.getStart()[1].get(3));
    }

    @org.junit.Test
    public void moveMultipleNoCollide() throws Exception {
        Board b1 = makeBoard();
        Pawn p1 = b1.getPlayers()[1].getPawns()[1];
        Pawn p2 = b1.getPlayers()[1].getPawns()[2];
        Pawn e1 = b1.getPlayers()[2].getPawns()[1];
        Pawn e2 = b1.getPlayers()[2].getPawns()[2];
        Board b2 = new Board (b1);
        Pawn[][] sorryBoard2 = new Pawn[4][21];

        b1.moveFromStart(p1);
        b1.moveFromStart(e1);
        b1.moveFromStart(p2);
        b1.moveFromStart(e2);
        b1.move(p1, 4);
        b1.move(p2, 16);
        b1.move(e1, 2);
        b1.move(e2, 18);

        Player[] b2Players = b2.getPlayers();
        b2.setPawnLocation(0,7,b2Players[1].getPawns()[1]);
        b2.setPawnLocation(1,4,b2Players[1].getPawns()[2]);
        b2.setPawnLocation(1,5,b2Players[2].getPawns()[1]);
        b2.setPawnLocation(2,5,b2Players[2].getPawns()[2]);

        for (int i =0; i<4; i++){
            for (int j=0; j<21; j++){
                Pawn tile1 = b1.getSorryBoard()[i][j];
                Pawn tile2 = b2.getSorryBoard()[i][j];
                if (tile1 != null || tile2 != null){
                    Assert.assertEquals(tile1.getPlayerID(), tile2.getPlayerID());
                }
            }
        }

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
        Assert.assertEquals(b.getSorryBoard()[1][3], p);
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