package com.stratego.game;
/** Rank 1-9 as expected, rank10=spy, Rank11=bomb, rank12=flag|| side 0=red | side 1=blue*/
public class Piece {
	public static int[] SoldierLocation = new int[2];
	public static int SoldierSide;
	public static int SoldierRank;
	public Piece(int rank, int side){
		SoldierRank = rank;
		SoldierSide = side;
	}
}
/* OLD:
package com.stratego.game;
/** Rank 1-9 as expected, Rank10=bomb, rank11=flag || side 0=red | side 1=blue*/
public class Piece {
	public static int[][] SoldierHereRank = new int[10][10];
	public static int[][] SoldierHereSide = new int[10][10];
	public static void createPiece(int x, int y, int rank, int side){
		SoldierHereRank[x][y] = rank;
		SoldierHereSide[x][y] = side;
	}
	public static int getPieceRank(int currentx, int currenty){
		return SoldierHereRank[currentx][currenty];
	}
	public static int getPieceSide(int currentx, int currenty){
		return SoldierHereSide[currentx][currenty];
	}
}
*/
