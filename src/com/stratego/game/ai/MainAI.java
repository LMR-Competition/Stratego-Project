package com.stratego.game.ai;

import java.util.Arrays;
import java.util.Random;

import com.stratego.game.MainGame;
import com.stratego.game.Movement;
import com.stratego.game.PieceData;
import com.stratego.game.Tile;

public class MainAI{
	private static int aiSide = 0;
	public static void aiTurn(){
		Tile home = null;
		Tile target = null;
	    for (int x = 0; x<10; x++){
	        for (int y = 0; y<10;y++){
	          if (MainGame.gameBoard[x][y].piece.soldierSide == aiSide){
	            if (home==null){
	            	home = MainGame.gameBoard[x][y];
	            } else if (MainGame.gameBoard[x][y].piece.soldierRank > home.piece.soldierRank){
	            	home = MainGame.gameBoard[x][y];
	            }
	          }
	        }
	    }
		target = aiFindNearest(home);
		aiReachTarget(home, target);
    //weighting?
	}
	public static Tile aiFindNearest(Tile home){
		Tile closestTile = null;
    	for (int x = 0; x<10; x++){
    	for (int y = 0; y<10;y++){
    	  if (MainGame.gameBoard[x][y].piece.soldierSide != aiSide){      
        		if (closestTile == null){
        			closestTile = MainGame.gameBoard[x][y];//finish checking for adjacent spy
        		} else if (Math.abs(MainGame.gameBoard[x][y].piece.x-home.piece.x)+ Math.abs(MainGame.gameBoard[x][y].piece.y-home.piece.y)<Math.abs(closestTile.piece.x - home.piece.x)+ Math.abs(closestTile.piece.y - home.piece.y)){
        			closestTile = MainGame.gameBoard[x][y];
        		}
        	}
    	}
    	}
    //Calc closest piece
    return closestTile;
	}
	public static boolean shouldMoveHere(Tile here){
		if ((MainGame.gameBoard[here.piece.x][here.piece.y].piece.soldierRank !=11 && MainGame.gameBoard[here.piece.x-1][here.piece.y].piece.soldierRank != 10&&MainGame.gameBoard[here.piece.x+1][here.piece.y].piece.soldierRank != 10&&MainGame.gameBoard[here.piece.x][here.piece.y-1].piece.soldierRank != 10&&MainGame.gameBoard[here.piece.x][here.piece.y+1].piece.soldierRank != 10)&&MainGame.gameBoard[here.piece.x][here.piece.y].moveable == true){
			return true;
		}else{
			return false;
		}
	}
	public static void aiReachTarget(Tile start, Tile finish){
		int goalX = finish.piece.x;
		int goalY = finish.piece.y;
		int currentX = start.piece.x;
		int currentY = start.piece.y;
		int moveToX;
		int moveToY;
		Pathway[] paths = new Pathway[100];
		for (int k = 0; k<100; k++){
			Arrays.fill(paths[k].xPath, -1);
		}
		paths[0].used = true;
		while (currentX != goalX && currentY != goalY){
			int usedPathways = 0;
			//collection of null checks on pathways
			for (int i = 1; i<100; i++){
				if (paths[i].used == true){
					usedPathways++;
				}
			}
			for (int j = 0; j<=usedPathways; j++){
				if (currentX<goalX){
					if (shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						currentX++;
						for(int loop = 0; loop <= 100; loop++){
							if (paths[j].xPath[loop] ==-1){
								paths[j].xPath[loop] =currentX;
								break;//only breaking out of one?
							}
						}
					}else if (currentY<goalY){
						
					}
				}
			}
		}
	}
	public static void setPlayerBoard() {
		int[] ranks = { 12, 11, 11, 11, 8, 5, 7, 4, 3, 4, 6, 6, 7, 5, 5, 8, 3,
				8, 6, 7, 11, 9, 5, 7, 8, 6, 4, 9, 9, 8, 10, 9, 9, 9, 1, 2, 9, 9,
				11, 11 };
		boolean[] filled = new boolean[40];
		for (int row = 9; row >= 6; row--) {
			int colToFill = 10;
			for (int col = 0; col < 10; col++) {

				if (col == 0) {
					Arrays.fill(filled, false);
				}
				boolean thisFilled = false;
				while (thisFilled == false) {
					int toFill = new Random().nextInt(10) + (9 - row) * 10;
					if (filled[toFill] == false) {
						Movement.preparePiece(col, row, ranks[toFill], 1);
						PieceData.removePieceFromWell(1, ranks[toFill]);
						filled[toFill] = true;
						thisFilled = true;
					}
				}
			}
		}
	}

	public static void setBoard() {
		int frontRowY = 3;
		int[] frontRank = { 1, 2, 9, 9, 11, 11, 11, 5, 7, 4 };
		int[] backRanks = { 7, 9, 9, 9, 9, 3, 4, 8, 4, 5, 5, 5, 6, 6, 6, 8, 6,
				10, 7, 7, 8, 8, 8, 3, 9, 11, 9 };//
		// frontrow
		boolean[] filled = new boolean[10];
		for (int col = 0; col < 10; col++) {

			if (col == 0) {
				Arrays.fill(filled, false);
			}

			boolean thisFilled = false;
			while (thisFilled == false) {
				int toFill = new Random().nextInt(10);// below not working
				// True and (false or true) and not false
				// true and ((true or true/false) and (true and true/false))
				if (filled[toFill] == false
						&& !((toFill == 2 || toFill == 3 || toFill == 6 || toFill == 7) && frontRank[toFill] == 1)) {
					Movement.preparePiece(toFill, frontRowY, frontRank[col],
							aiSide);
					PieceData.removePieceFromWell(aiSide, frontRank[col]);
					filled[toFill] = true;
					// array of pieces to place place next one based on col
					thisFilled = true;
				}
			}
		}

		filled = new boolean[27];
		for (int row = 2; row >= 0; row--) {
			int extraFill = 0;
			int colToFill = 10;
			if (row == 1) {
				colToFill = 9;
				extraFill = 10;
			} else if (row == 0) {
				colToFill = 8;
				extraFill = 19;
			}
			for (int col = 0; col < colToFill; col++) {

				if (col == 0) {
					Arrays.fill(filled, false);
				}
				boolean thisFilled = false;
				while (thisFilled == false) {
					int toFill = new Random().nextInt(colToFill);
					if (filled[toFill + extraFill] == false) {
						Movement.preparePiece(col, row, backRanks[toFill
								+ extraFill], aiSide);
						PieceData.removePieceFromWell(aiSide, backRanks[toFill
								+ extraFill]);
						filled[toFill + extraFill] = true;
						thisFilled = true;
					}
				}
			}
		}
		Movement.preparePiece(8, 0, 11, aiSide);
		Movement.preparePiece(9, 0, 12, aiSide);
		Movement.preparePiece(9, 1, 11, aiSide);
		PieceData.removePieceFromWell(aiSide, 11);
		PieceData.removePieceFromWell(aiSide, 12);
		PieceData.removePieceFromWell(aiSide, 11);
	}
}
