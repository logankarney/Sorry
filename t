[1mdiff --git a/bin/production/Sorry/sample/application.css b/bin/production/Sorry/sample/application.css[m
[1mindex 5e7d249..de0ece5 100644[m
[1m--- a/bin/production/Sorry/sample/application.css[m
[1m+++ b/bin/production/Sorry/sample/application.css[m
[36m@@ -5,9 +5,11 @@[m
     -fx-my-red: rgba(170, 10, 10, 0.35);[m
     -fx-my-green: rgba(47, 218, 84, 0.35);[m
     -fx-my-yellow: rgba(255, 249, 77, 0.35);[m
[32m+[m[32m    -fx-my-grey: rgba(71, 71, 71, 0.35);[m
 [m
     -fx-my-background: #c1c5cc;[m
 [m
[32m+[m
     -fx-my-border-color: black;[m
     -fx-my-border-size: 5px;[m
 }[m
[36m@@ -66,5 +68,32 @@[m
     -fx-border-color: -fx-my-border-color;[m
     -fx-border-padding: -fx-my-border-size;[m
 }[m
[32m+[m[32m/**  Move tiles **/[m
[32m+[m[32m#red-move-tile{[m
[32m+[m[32m    -fx-background-color: red;[m
[32m+[m[32m    -fx-border-color: -fx-my-border-color;[m
[32m+[m[32m    -fx-border-padding: -fx-my-border-size;[m
[32m+[m[32m}[m
[32m+[m
[32m+[m[32m#blue-move-tile{[m
[32m+[m[32m    -fx-background-color: blue;[m
[32m+[m[32m    -fx-border-color: -fx-my-border-color;[m
[32m+[m[32m    -fx-border-padding: -fx-my-border-size;[m
[32m+[m[32m}[m
[32m+[m
[32m+[m[32m#yellow-move-tile{[m
[32m+[m[32m    -fx-background-color: yellow;[m
[32m+[m[32m    -fx-border-color: -fx-my-border-color;[m
[32m+[m[32m    -fx-border-padding: -fx-my-border-size;[m
[32m+[m[32m}[m
[32m+[m
[32m+[m[32m#green-move-tile{[m
[32m+[m[32m    -fx-background-color: green;[m
[32m+[m[32m    -fx-border-color: -fx-my-border-color;[m
[32m+[m[32m    -fx-border-padding: -fx-my-border-size;[m
[32m+[m[32m}[m
[41m+[m
[41m+[m
[41m+[m
 [m
 [m
[1mdiff --git a/src/sample/Board.java b/src/sample/Board.java[m
[1mindex ba9ec6c..7d598fc 100644[m
[1m--- a/src/sample/Board.java[m
[1m+++ b/src/sample/Board.java[m
[36m@@ -89,6 +89,7 @@[m [mpublic class Board {[m
                 }[m
             }[m
         }[m
[32m+[m[32m        // Moving forward[m
         // Not in home row[m
         else if (space > 2 || p.getPlayerID() != row) {[m
             // normal movement within one row[m
[36m@@ -137,9 +138,10 @@[m [mpublic class Board {[m
             sorryBoard[row][space] = null;[m
             returnToStart(sorryBoard[newRow][newSpace]);[m
         }[m
[31m-        resolveSlide(p);[m
         p.setLocation(newRow, newSpace);[m
         sorryBoard[newRow][newSpace] = p;[m
[32m+[m[32m        resolveSlide(p);[m
[32m+[m
         return true;[m
     }[m
 [m
[36m@@ -203,20 +205,29 @@[m [mpublic class Board {[m
     }[m
 [m
     /**[m
[31m-     * Performs a slide if necessary[m
[32m+[m[32m     * Performs a slide if necessary and sends all pawns on the slider[m
[32m+[m[32m     * back to the start (including ally pawns)[m
      * @param p[m
      */[m
     private void resolveSlide(Pawn p){[m
[32m+[m[32m        // You don't slide on your own color[m
         if (p.getPlayerID() != p.getRow()){[m
[32m+[m[32m            //Short slider[m
             if (p.getSpace() == 0){[m
[31m-                move(p, 1);[m
[31m-                move(p, 1);[m
[31m-                move(p, 1);[m
[32m+[m[32m                for (int i = 1; i<4; i++){[m
[32m+[m[32m                    if (sorryBoard[p.getRow()][i] != null){[m
[32m+[m[32m                        returnToStart(sorryBoard[p.getRow()][i]);[m
[32m+[m[32m                    }[m
[32m+[m[32m                }[m
[32m+[m[32m                move(p, 3);[m
[32m+[m[32m            // Long slider[m
             }else if (p.getSpace() == 8){[m
[31m-                move(p, 1);[m
[31m-                move(p, 1);[m
[31m-                move(p, 1);[m
[31m-                move(p, 1);[m
[32m+[m[32m                for (int i = 9; i<13; i++){[m
[32m+[m[32m                    if (sorryBoard[p.getRow()][i] != null){[m
[32m+[m[32m                        returnToStart(sorryBoard[p.getRow()][i]);[m
[32m+[m[32m                    }[m
[32m+[m[32m                }[m
[32m+[m[32m                move(p, 4);[m
             }[m
         }[m
     }[m
[1mdiff --git a/src/sample/BoardTest.java b/src/sample/BoardTest.java[m
[1mindex b10fe8c..8acda63 100644[m
[1m--- a/src/sample/BoardTest.java[m
[1m+++ b/src/sample/BoardTest.java[m
[36m@@ -1,6 +1,8 @@[m
 package sample;[m
 [m
 import org.junit.Assert;[m
[32m+[m[32mimport org.junit.Test;[m
[32m+[m[32mimport org.junit.experimental.categories.ExcludeCategories;[m
 [m
 import static org.junit.Assert.*;[m
 [m
[36m@@ -29,6 +31,76 @@[m [mpublic class BoardTest {[m
     }[m
 [m
     @org.junit.Test[m
[32m+[m[32m    public void shortSlideNoCollide() throws Exception {[m
[32m+[m[32m        Board b = makeBoard();[m
[32m+[m[32m        Pawn p = b.getPlayers()[0].getPawns()[0];[m
[32m+[m[32m        b.moveFromStart(p);[m
[32m+[m[32m        b.move(p, 12);[m
[32m+[m[32m        Assert.assertEquals(p, b.getSorryBoard()[1][3]);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    @org.junit.Test[m
[32m+[m[32m    public void ownSlide() throws Exception {[m
[32m+[m[32m        Board b = makeBoard();[m
[32m+[m[32m        Pawn p = b.getPlayers()[0].getPawns()[0];[m
[32m+[m[32m        b.moveFromStart(p);[m
[32m+[m[32m        b.move(p, 5);[m
[32m+[m[32m        Assert.assertEquals(p, b.getSorryBoard()[0][8]);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    @org.junit.Test[m
[32m+[m[32m    public void longSlideNoCollide() throws Exception {[m
[32m+[m[32m        Board b = makeBoard();[m
[32m+[m[32m        Pawn p = b.getPlayers()[0].getPawns()[0];[m
[32m+[m[32m        b.moveFromStart(p);[m
[32m+[m[32m        b.move(p, 20);[m
[32m+[m[32m        Assert.assertEquals(p, b.getSorryBoard()[1][12]);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    @org.junit.Test[m
[32m+[m[32m    public void slideCollideEnemy() throws Exception{[m
[32m+[m[32m        Board b = makeBoard();[m
[32m+[m[32m        Pawn p = b.getPlayers()[0].getPawns()[0];[m
[32m+[m[32m        Pawn e = b.getPlayers()[1].getPawns()[0];[m
[32m+[m[32m        b.moveFromStart(p);[m
[32m+[m[32m        b.moveFromStart(e);[m
[32m+[m[32m        b.move(p, 12);[m
[32m+[m[32m        Assert.assertEquals(p, b.getSorryBoard()[1][3]);[m
[32m+[m[32m        Assert.assertEquals(e, b.getStart()[1].get(3));[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    @org.junit.Test[m
[32m+[m[32m    public void slideCollideAlly() throws Exception{[m
[32m+[m[32m        Board b = makeBoard();[m
[32m+[m[32m        Pawn p = b.getPlayers()[0].getPawns()[0];[m
[32m+[m[32m        Pawn a = b.getPlayers()[0].getPawns()[1];[m
[32m+[m[32m        b.moveFromStart(p);[m
[32m+[m[32m        b.move(p, 13);[m
[32m+[m[32m        b.moveFromStart(a);[m
[32m+[m[32m        b.move(a, 12);[m
[32m+[m[32m        Assert.assertEquals(a, b.getSorryBoard()[1][3]);[m
[32m+[m[32m        Assert.assertEquals(p, b.getStart()[0].get(2));[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
[32m+[m[32m/*[m
[32m+[m[32m    @org.junit.Test[m
[32m+[m[32m    public void moveMultipleNoCollide() throws Exception {[m
[32m+[m[32m        Board b1 = makeBoard();[m
[32m+[m[32m        Pawn p1 = b1.getPlayers()[1].getPawns()[1];[m
[32m+[m[32m        Pawn p2 = b1.getPlayers()[1].getPawns()[2];[m
[32m+[m[32m        Pawn e1 = b1.getPlayers()[2].getPawns()[1];[m
[32m+[m[32m        Pawn e2 = b1.getPlayers()[2].getPawns()[2];[m
[32m+[m[32m        b1.moveFromStart(p1);[m
[32m+[m[32m        b1.moveFromStart(p2);[m
[32m+[m[32m        b1.moveFromStart(e1);[m
[32m+[m[32m        b1.moveFromStart(e2);[m
[32m+[m
[32m+[m[32m        Board b2 = new Board (b1);[m
[32m+[m[32m        //b1.move(p1, )[m
[32m+[m
[32m+[m[32m    }[m
[32m+[m[32m*/[m
     public void moveFromStart() throws Exception {[m
         Board b = makeBoard();[m
         Pawn p = b.getPlayers()[1].getPawns()[3];[m
[1mdiff --git a/src/sample/Pawn.java b/src/sample/Pawn.java[m
[1mindex 2cbfd95..bd5f4df 100644[m
[1m--- a/src/sample/Pawn.java[m
[1m+++ b/src/sample/Pawn.java[m
[36m@@ -65,4 +65,6 @@[m [mpublic class Pawn{[m
     public int getPlayerID() {[m
         return playerID;[m
     }[m
[32m+[m
[32m+[m
 }[m
\ No newline at end of file[m
