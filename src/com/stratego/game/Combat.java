package com.stratego.game;

import com.stratego.game.ui.Screen;

public class Combat {

	public static void engage(Piece attacker, Piece defender) {

		if (attacker.soldierRank == 10 || defender.soldierRank > 10) {

			if (attacker.soldierRank == 10) {

				if (defender.soldierRank == 1) {

					PieceData.killPiece(MainGame.gameBoard[defender.x][defender.y].piece.soldierSide, MainGame.gameBoard[defender.x][defender.y].piece.soldierRank);
					Movement.preparePiece(defender.x, defender.y,
							attacker.soldierRank, attacker.soldierSide);
					MainGame.gameBoard[defender.x][defender.y].piece.justAttacked = true;
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;

				} else {

					MainGame.gameBoard[attacker.x][attacker.y].piece = null;
					MainGame.gameBoard[defender.x][defender.y].piece.justAttacked = true;

				}
			}
			if (defender.soldierRank == 11) {
				if (attacker.soldierRank == 8) {
					PieceData.killPiece(MainGame.gameBoard[defender.x][defender.y].piece.soldierSide, MainGame.gameBoard[defender.x][defender.y].piece.soldierRank);
					Movement.preparePiece(defender.x, defender.y,
							attacker.soldierRank, attacker.soldierSide);
					MainGame.gameBoard[defender.x][defender.y].piece.justAttacked = true;
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				} else {
					PieceData.killPiece(MainGame.gameBoard[attacker.x][attacker.y].piece.soldierSide, MainGame.gameBoard[attacker.x][attacker.y].piece.soldierRank);
					MainGame.gameBoard[attacker.x][attacker.y].piece = null;
					MainGame.gameBoard[defender.x][defender.y].piece.justAttacked = true;
				}
			} else if (defender.soldierRank == 12) {

				Screen.won = true;
			}

		} else {

			if (attacker.soldierRank < defender.soldierRank) {

				PieceData.killPiece(MainGame.gameBoard[defender.x][defender.y].piece.soldierSide, MainGame.gameBoard[defender.x][defender.y].piece.soldierRank);
				Movement.preparePiece(defender.x, defender.y,
						attacker.soldierRank, attacker.soldierSide);
				MainGame.gameBoard[defender.x][defender.y].piece.justAttacked = true;
				MainGame.gameBoard[attacker.x][attacker.y].piece = null;

			} else if (attacker.soldierRank > defender.soldierRank) {

				PieceData.killPiece(MainGame.gameBoard[attacker.x][attacker.y].piece.soldierSide, MainGame.gameBoard[attacker.x][attacker.y].piece.soldierRank);
				MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				MainGame.gameBoard[defender.x][defender.y].piece.justAttacked = true;

			} else {

				PieceData.killPiece(MainGame.gameBoard[defender.x][defender.y].piece.soldierSide, MainGame.gameBoard[defender.x][defender.y].piece.soldierRank);
				PieceData.killPiece(MainGame.gameBoard[attacker.x][attacker.y].piece.soldierSide, MainGame.gameBoard[attacker.x][attacker.y].piece.soldierRank);
				MainGame.gameBoard[attacker.x][attacker.y].piece = null;
				MainGame.gameBoard[defender.x][defender.y].piece = null;
			}
		}

		if (MainGame.gameBoard[defender.x][defender.y].piece != null) {

			MainGame.gameBoard[defender.x][defender.y].piece.visible = true;
		}
	}
}
