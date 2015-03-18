/* NEEDS:
Place to send information of the piece winning combat so the piece can be displayed to the opponent on their next turn
Or Log the player can see?
*/
package com.stratego.game;

public class Combat {
  public static void engage(Piece attacker, Piece defender){
    if (attacker.rank == 10 || defender.rank>9){
      if (attacker.rank == 10){
        if defender.rank == 1{
          gameBoard[defender.x][defender.y].piece=attacker;
          gameBoard[attacker.x][attacker.y].piece=null;
        } else{
          gameBoard[attacker.x][attacker.y].piece=null;
        } else //others
      }
      //special cases
    } else{// Send to GUI from inside or outside ifs? What to do when both removed
      if (attacker.rank < defender.rank){
        gameBoard[defender.x][defender.y].piece=attacker;
        gameBoard[attacker.x][attacker.y].piece=null;
      } else if (attacker.rank > defender.rank){
        gameBoard[attacker.x][attacker.y].piece=null;
      } else{
        gameBoard[attacker.x][attacker.y].piece=null;
        gameBoard[defender.x][defender.y].piece=null;
      }
    }
  }
}
