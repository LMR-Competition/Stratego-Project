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
	public static void setPlayerBoard(){
		int[] ranks = {12,11,11,11,8,5,7,4,3,4,6,6,7,5,5,8,3,8,6,7,9,9,5,7,8,6,4,9,9,8,9,9,9,9,1,2,9,9,11,11};
      	boolean[] filled = new boolean[40];
		for (int row = 9; row>=6; row--){
			int colToFill = 10;
			for (int col = 0; col < 10; col++){
      			
				if (col ==0){
    	  			Arrays.fill(filled, false);
	       		}
	      		boolean thisFilled = false;
       			while (thisFilled == false){
       				int toFill = new Random().nextInt(10)+(9-row)*10;
       				if (filled[toFill] == false){
       					Movement.preparePiece(col,row,ranks[toFill], 1);
       					filled[toFill] = true;
       					thisFilled = true;
       				}
       			}
			}
		}
	}
    public static void setBoard(){
   		int frontRowY = 3;
   		int[] frontRank = {1,2,9,9,11,11,11,5,7,4};
     	int[] backRanks = {9,9,9,9,9,3,4,8,4,5,5,5,6,6,6,8,6,7,7,7,8,8,8,3,9,9,9};//
   		//frontrow
 		boolean[] filled = new boolean[10];	
      	for (int col = 0; col < 10; col++){
      			
      		if (col ==0){
       			Arrays.fill(filled, false);
       		}
    	  		
       		boolean thisFilled = false;
       		while (thisFilled == false){
       			int toFill = new Random().nextInt(10);//below not working
       			//True  and (false  or true) and not false
       			// true and ((true or true/false) and (true and true/false))
       			if (filled[toFill] == false && !((toFill==2 || toFill==3 || toFill ==6 ||toFill==7)&&frontRank[toFill]==1)){
       				Movement.preparePiece(toFill,frontRowY,frontRank[col], aiSide);
       				filled[toFill] = true;
       				//array of pieces to place place next one based on col
       				thisFilled = true;
       			}
       		}
      	}
      		
      	filled = new boolean[27];
		for (int row = 2; row>=0; row--){
			int extraFill = 0;
			int colToFill = 10;
			if (row==1){
				colToFill = 9;
				extraFill = 10;
			} else if (row==0){
				colToFill =8;
				extraFill = 19;
			}
			for (int col = 0; col < colToFill; col++){
      			
				if (col ==0){
    	  			Arrays.fill(filled, false);
	       		}
	      		boolean thisFilled = false;
       			while (thisFilled == false){
       				int toFill = new Random().nextInt(colToFill);
       				if (filled[toFill+extraFill] == false){
       					Movement.preparePiece(col,row,backRanks[toFill+extraFill], aiSide);
       					filled[toFill+extraFill] = true;
       					thisFilled = true;
       				}
       			}
			}
		}
	Movement.preparePiece(8,0,11, aiSide);
	Movement.preparePiece(9,0,12, aiSide);
	Movement.preparePiece(9,1,11, aiSide);
    } 
}
