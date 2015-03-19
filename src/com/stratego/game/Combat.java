/* NEEDS:
Place to send information of the piece winning combat so the piece can be displayed to the opponent on their next turn
Or Log the player can see?
 */
package com.stratego.game;

public class Combat {
	
	public static void engage(Piece attacker, Piece defender) {
		
		if (attacker.soldierRank == 10 || defender.soldierRank > 9) {

			if (attacker.soldierRank == 10) {

				if (defender.soldierRank == 1) {

					MainGame.gameBoard[defender.x][defender.y].piece = attacker;
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;

				} else {

					MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				}
			}

			// TODO special cases

		} else {

			// Send to GUI from inside or outside ifs? What to do when both
			// removed

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
	}
}
