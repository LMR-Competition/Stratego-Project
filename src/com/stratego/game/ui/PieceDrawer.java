package com.stratego.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.lutz.engine.ui.graphics.GraphicsEngine;
import com.stratego.game.MainGame;

public class PieceDrawer {

	public static void drawPiece(GraphicsEngine engine, int pointValue,
			int side, int x, int y, int width, int height, int turn, int gridX,
			int gridY, boolean blackAndWhite) {

		Graphics2D g = engine.getGraphics();

		if (side == 0) {

			g.setColor(blackAndWhite ? Screen.makeBlackAndWhite(Color.BLUE
					.darker()) : Color.BLUE.darker());

		} else {

			g.setColor(blackAndWhite ? Screen.makeBlackAndWhite(Color.RED
					.darker()) : Color.RED.darker());
		}

		g.fillRect(x + 2, y + 2, width - 5, height - 5);

		if (turn == side || turn == -1 || gridX < 0 || gridY < 0
				|| MainGame.gameBoard[gridX][gridY].piece.justAttacked) {

			g.setColor(blackAndWhite ? Screen.makeBlackAndWhite(Screen.GOLD)
					: Screen.GOLD);
			Font f = new Font("Times New Roman", Font.PLAIN, 2);
			f = f.deriveFont((float) engine.percentageFontSize(f, (float) width
					/ engine.getHeight()));
			g.setFont(f);

			String pointStr = Integer.toString(pointValue);

			switch (pointValue) {

			case 10:

				pointStr = "S";

				break;

			case 11:

				pointStr = "B";

				break;

			case 12:

				pointStr = "F";

				break;
			}

			g.drawString(pointStr, x + (width / 2)
					- (g.getFontMetrics().stringWidth(pointStr) / 2), y
					+ (height / 2) + (g.getFontMetrics().getHeight() / 4) + 5);
		}
	}
}
