//As of right now this class is used for creation and then all further functions are done by referenceing the piece stored on gameBoard
package com.stratego.game;
/** Rank 1-9 as expected, rank10=spy, Rank11=bomb, rank12=flag|| side 0=red | side 1=blue*/
public class Piece {
	
	public int soldierSide;
	
	public int soldierRank;

	boolean visible = false;
	
	boolean justAttacked = false;

	public int x, y;
	
	public Piece(int x, int y, int rank, int side){

		this.x = x;
		this.y = y;
		soldierRank = rank;
		soldierSide = side;
	}
}
