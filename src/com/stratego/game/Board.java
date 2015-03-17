package com.stratego.game;

public class Board {
	public static int[][] SoldierHereRank = new int[10][10];
	public static int[][] SoldierHereSide = new int[10][10];
	public static boolean[][] moveable = new boolean[10][10];
	public static int[][] Board = new int[10][10];
	public static void makeBoard(){
		for (int x = 0; x<=10; x++){
			for (int y = 0; y<=10; y++){
				if ((y==4&&(x==2||x==3|| x==6||x==7))||(y==5&&(x==2||x==3|| x==6||x==7))){
					moveable[x][y] = false;
				} else {
					moveable[x][y] = true;
				}
			}
		}
		for (int x = 0; x<=10; x++){
			for (int y = 0; y<=10; y++){
				Board[x][y] = x+y*10;
			}
		}
	}
	public static int getPieceRank(int currentx, int currenty){
		return SoldierHereRank[currentx][currenty];
	}
	public static int getPieceSide(int currentx, int currenty){
		return SoldierHereSide[currentx][currenty];
	}
}
