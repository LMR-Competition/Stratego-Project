/* NEEDS:
Log the player can see?
 */
package com.stratego.game;

public class Combat {
	
	public static void engage(Piece attacker, Piece defender) {
		
		if (attacker.soldierRank == 10 || defender.soldierRank > 10) {

			if (attacker.soldierRank == 10) {

				if (defender.soldierRank == 1) {

					MainGame.gameBoard[defender.x][defender.y].piece = attacker;
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;

				} else {

					MainGame.gameBoard[attacker.x][attacker.y].piece = null;

				}
			} if (defender.soldierRank == 11){
				if (attacker.soldierRank == 8){
					MainGame.gameBoard[defender.x][defender.y].piece = attacker;
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				} else {
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				}
			} else if (defender.soldierRank ==12 ){

			// TODO When won
			
			}

		} else {

			if (attacker.soldierRank < defender.soldierRank) {

				MainGame.gameBoard[defender.x][defender.y].piece = attacker;
				MainGame.gameBoard[attacker.x][attacker.y].piece = null;

			} else if (attacker.soldierRank > defender.soldierRank) {

				MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				

			} else {

				MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				MainGame.gameBoard[defender.x][defender.y].piece = null;
			}
		}
		if (MainGame.gameBoard[defender.x][defender.y].piece != null){
		MainGame.gameBoard[defender.x][defender.y].piece.visible = true;
		}
	}
}
