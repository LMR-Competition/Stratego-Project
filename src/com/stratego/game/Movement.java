//Preferably PPnum is used to end setup well function when it reaches 79.
/*
 NEEDS:
 if(MOUSE REALEASED ON SQUARE WHILE HOLDING GHOST FROM WELL){
 PreparePiece(X OF SQUARE,Y OF SQUARE, RANK, SIDE);   //Square over being the square the mouse or piece or piece is over, which is better interface? 
 }
 if (PIECE CLICKED ON){
 GatherPiece(X OF HOME, Y OF HOME);
 }
 if (MOUSE RELEASED WHILE HeldPiece is not null && MOUSE OVER A SQUARE){
 PlacePiece(X OF SQUARE OVER, Y OF SQUARE OVER);   //Square over being the square the mouse or piece or piece is over, which is better interface? 
 }*/
package com.stratego.game;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class Movement {

	public static Piece heldPiece = null;

	/** Counter for piece number during generation */
	public static int PPnum = 0;

	public static void preparePiece(int x, int y, int rank, int side) {

		MainGame.soldiers[PPnum] = new Piece(x, y, rank, side);
		MainGame.gameBoard[x][y].piece = MainGame.soldiers[PPnum];
		PPnum++;
	}

	// ?? - This doesn't work
	public static void gatherPiece(int x, int y) {

		// if (MainGame.soldiers[MainGame.gameBoard[x][y].piece.].rank<=10){
		// //no soldiers supposed to be here?
		// heldPiece = gameBoard[x][y].piece;
		// } else {
		// //play error noise?
		// }
	}
	
	public static Point[] getMoveRange(Piece selected){
		
		int x = selected.x;
		int y = selected.y;
		
		Point[] points = new Point[]{new Point(x-1, y), new Point(x, y-1), new Point(x, y+1), new Point(x+1, y)};

		List<Point> pointList = new ArrayList<Point>();
		
		for(Point p : points){
			
			if(p.x >= 0 && p.x <= 9 && p.y >= 0 && p.y <= 0){
				
				pointList.add(p);
			}
		}
		
		return pointList.toArray(new Point[]{});
	}

	public static void placePiece(int x, int y) {

		// Moveable Destination?
		if ((heldPiece.x == x && heldPiece.y == y)
				|| 1 < Math.abs(heldPiece.x - x)
				|| 1 < Math.abs(heldPiece.y - y)) {

			// error noise?

		} else if (MainGame.gameBoard[x][y].piece != null) {

			Combat.engage(heldPiece, MainGame.gameBoard[x][y].piece);

		} else {

			MainGame.gameBoard[x][y].piece = heldPiece;
			MainGame.gameBoard[heldPiece.x][heldPiece.y].piece = null;
		}
		heldPiece = null;
	}
}
