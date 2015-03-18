package com.stratego.game;
/** Rank 1-9 as expected, rank10=spy, Rank11=bomb, rank12=flag|| side 0=red | side 1=blue*/
public class Piece {
	
	public static int[] soldierLocation = new int[2];
	
	public static int soldierSide;
	
	public static int soldierRank;
	
	public Piece(int rank, int side){
		
		soldierRank = rank;
		soldierSide = side;
	}
}
