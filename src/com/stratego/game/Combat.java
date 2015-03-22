// TODO: Make this class report to the tile 'graveyard'
package com.stratego.game;

import com.stratego.game.ui.Screen;

public class Combat {

	public static void engage(Piece attacker, Piece defender) {

		if (attacker.soldierRank == 10 || defender.soldierRank > 10) {

			if (attacker.soldierRank == 10) {

				if (defender.soldierRank == 1) {

					Movement.preparePiece(defender.x, defender.y,
							attacker.soldierRank, attacker.soldierSide);
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;

				} else {

					MainGame.gameBoard[attacker.x][attacker.y].piece = null;

				}
			}
			if (defender.soldierRank == 11) {
				if (attacker.soldierRank == 8) {
					Movement.preparePiece(defender.x, defender.y,
							attacker.soldierRank, attacker.soldierSide);
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				} else {
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				}
			} else if (defender.soldierRank == 12) {

				Screen.won = true;
			}

		} else {

			if (attacker.soldierRank < defender.soldierRank) {

				Movement.preparePiece(defender.x, defender.y,
						attacker.soldierRank, attacker.soldierSide);
				MainGame.gameBoard[defender.x][defender.y].piece.justAttacked = true;
				MainGame.gameBoard[attacker.x][attacker.y].piece = null;

			} else if (attacker.soldierRank > defender.soldierRank) {

				MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				MainGame.gameBoard[defender.x][defender.y].piece.justAttacked = true;

			} else {

				MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				MainGame.gameBoard[defender.x][defender.y].piece = null;
			}
		}

		if (MainGame.gameBoard[defender.x][defender.y].piece != null) {

			MainGame.gameBoard[defender.x][defender.y].piece.visible = true;
		}
	}
}
