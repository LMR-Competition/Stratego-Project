package com.stratego.game.ai;

import com.stratego.game.*;

public class MainAI{
  int aiSide = 0;
  public static void aiTurn(){
    //weighting?
  }
  public static Tile aiFindNearest(Tile home){
    Tile closestTile = gameBoard[0][0];
    
    for (int x = 0; x<10; x++){
      for (int y = 0; y<10;y++){
        if (gameBoard[x][y].side != aiSide){
          
        }
      }
    }
    //Calc closest piece
    return closestTile;
    public static void setBoard(){
      int frontRowY = 3;
      int[] frontRank = {1,2,9,9,11,11,11,5,7,4};
      int[] backRanks = {3,3,4,4,5,5,5,6,6,6,6//
      //frontrow
      for (int col = 0; col < 10; col++){
        if (col ==0){
          boolean[] filled = new boolean[10];
          Arrays.fill(filled, false);
        }
        boolean filled = false;
        while (filled == false){
          int toFill = new Random().nextInt(10);
          if (filled[toFill] == false){
            preparePiece(col,frontRowY,frontrank[toFill], aiside);
            filled[toFill] = true;
            //array of pieces to place place next one based on col
            filled == true;
          }
        }
      }
      for (int col = 0; col < 10; col++){
        if (col ==0){
          boolean[] filled = new boolean[10];
          Arrays.fill(filled, false);
        }
        boolean filled = false;
        while (filled == false){
          int toFill = new Random().nextInt(10);
          if (filled[toFill] == false){
            preparePiece(col,2,frontrank[toFill], aiside);
            filled[toFill] = true;
            //array of pieces to place place next one based on col
            filled == true;
          }
        }
      }
    }
  }
}
