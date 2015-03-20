package com.stratego.game.ai;

import java.util.Arrays;
import java.util.Random;

import com.stratego.game.MainGame;
import com.stratego.game.Movement;
import com.stratego.game.Tile;

public class MainAI{
	private static int aiSide = 0;
	public static void aiTurn(){
    //weighting?
	}
	public static Tile aiFindNearest(Tile home){
    Tile closestTile = MainGame.gameBoard[0][0];
    
    for (int x = 0; x<10; x++){
      for (int y = 0; y<10;y++){
        if (MainGame.gameBoard[x][y].piece.soldierSide != aiSide){
          
        }
      }
    }
    //Calc closest piece
    return closestTile;
}
    	public static void setBoard(){
    		int frontRowY = 3;
      		int[] frontRank = {1,2,9,9,11,11,11,5,7,4};
      		int[] backRanks = {3,3,4,4,5,5,5,6,6,6,6};//
    		//frontrow
        			boolean[] filled = new boolean[10];
      		
      		for (int col = 0; col < 10; col++){
      			
    	  		if (col ==0){
          			Arrays.fill(filled, false);
        		}
    	  		
        		boolean thisFilled = false;
        		while (thisFilled == false){
        			int toFill = new Random().nextInt(10);
          			if (filled[toFill] == false){
        	  			Movement.preparePiece(col,frontRowY,frontRank[toFill], aiSide);
            			filled[toFill] = true;
            			//array of pieces to place place next one based on col
            			thisFilled = true;
          			}
        		}
      		}
      		
			filled = new boolean[10];

      		for (int col = 0; col < 10; col++){
      			
    	  		if (col ==0){
          			Arrays.fill(filled, false);
        		}
        		boolean thisFilled = false;
        		while (thisFilled == false){
        			int toFill = new Random().nextInt(10);
          			if (filled[toFill] == false){
        	  			Movement.preparePiece(col,2,frontRank[toFill], aiSide);
        	  			filled[toFill] = true;
            			//array of pieces to place place next one based on col
            			thisFilled = true;
          			}
        		}
      		}
      		}
        	
    	}
