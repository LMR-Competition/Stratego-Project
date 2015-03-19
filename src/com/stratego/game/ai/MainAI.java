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
      
    }
  }
}
