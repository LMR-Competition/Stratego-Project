package com.stratego.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.lutz.engine.ui.graphics.GraphicsEngine;

public class PieceDrawer {

	public static void drawPiece(GraphicsEngine engine, int pointValue,
			int side, int x, int y, int width, int height) {

		Graphics2D g = engine.getGraphics();

		if (side == 0) {

			g.setColor(Color.BLUE);

		} else {

			g.setColor(Color.RED);
		}

		g.fillRect(x + 2, y + 2, width - 5, height - 5);

		g.setColor(Color.YELLOW);
		Font f = new Font("Times New Roman", Font.PLAIN, 2);
		f = f.deriveFont((float) engine.percentageFontSize(f,
				(float) width / engine.getHeight()));
		g.setFont(f);

		String pointStr = Integer.toString(pointValue);
		
		switch(pointValue){
		
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
				+ (height / 2) + (g.getFontMetrics().getHeight() / 4)+5);
	}
}
