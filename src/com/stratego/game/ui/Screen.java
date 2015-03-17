package com.stratego.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.lutz.engine.resources.FontResource;
import com.lutz.engine.ui.graphics.GraphicsEngine;

public class Screen {

	/*
	 * Screen value for shown screen
	 * 
	 * 0 - Main menu 1 - Instructions screen 2 - Game screen
	 */
	private static int screenModeValue = 2, mouseX = 0, mouseY = 0;

	private static boolean resetSel = false, undoSel = false,
			endTurnSel = false, instructionsSel = false;

	public static MouseListener getMouseListener() {

		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				switch (screenModeValue) {

				case 2:

					if (resetSel) {

					} else if (undoSel) {

					} else if (endTurnSel) {

					} else if (instructionsSel) {

					}

					break;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		};
	}

	public static MouseMotionListener getMouseMotionListener() {

		return new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {

				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {

				mouseX = e.getX();
				mouseY = e.getY();
			}
		};
	}

	public static KeyListener getKeyListener() {

		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		};
	}

	private static final Color GOLD = new Color(255, 199, 0),
			DARK_RED = new Color(150, 0, 0), DARK_RED_BORDER = new Color(120,
					0, 0);

	public static void draw(GraphicsEngine engine) {

		Graphics2D g = engine.getGraphics();

		int mX = engine.scaleX(mouseX);
		int mY = engine.scaleY(mouseY);

		switch (screenModeValue) {

		case 0:

			Font titleFont = FontResource
					.getExternalFont("resources/fonts/parchment.ttf");

			titleFont = titleFont.deriveFont((float) engine.percentageFontSize(
					titleFont, 0.60));

			g.setFont(titleFont);

			g.setColor(GOLD.darker());

			g.drawString("stratego", (engine.getWidth() / 2)
					- (g.getFontMetrics().stringWidth("stratego") / 2) + 2,
					82 + (g.getFontMetrics().getHeight() / 4));

			g.setColor(GOLD);

			g.drawString("stratego", (engine.getWidth() / 2)
					- (g.getFontMetrics().stringWidth("stratego") / 2), 80 + (g
					.getFontMetrics().getHeight() / 4));

			break;

		case 2:

			int buttonBarHeight = 40;

			// BUTTON BAR

			g.setColor(DARK_RED);

			g.fillRect(0, 0, engine.getWidth(), buttonBarHeight);

			g.setColor(DARK_RED_BORDER);

			g.drawLine(0, buttonBarHeight, engine.getWidth(), buttonBarHeight);
			g.drawLine(0, buttonBarHeight - 1, engine.getWidth(),
					buttonBarHeight - 1);
			g.drawLine(0, 0, 0, buttonBarHeight);
			g.drawLine(1, 0, 1, buttonBarHeight);
			g.drawLine(engine.getWidth(), 0, engine.getWidth(), buttonBarHeight);
			g.drawLine(engine.getWidth() - 1, 0, engine.getWidth() - 1,
					buttonBarHeight);

			// BUTTONS

			Font font = new Font("Times New Roman", Font.PLAIN, 22);

			g.setFont(font);

			// END TURN

			String endTurn = "END TURN";

			int xL = (engine.getWidth() / 2)
					- (g.getFontMetrics().stringWidth(endTurn) / 2) - 20;
			int xR = (engine.getWidth() / 2)
					+ (g.getFontMetrics().stringWidth(endTurn) / 2) + 20;
			int yT = 2;
			int yB = 34;

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

				endTurnSel = true;

				g.setColor(GOLD.brighter());

			} else {

				endTurnSel = false;

				g.setColor(GOLD);
			}

			g.fillRect((engine.getWidth() / 2)
					- (g.getFontMetrics().stringWidth(endTurn) / 2) - 20, 2, g
					.getFontMetrics().stringWidth(endTurn) + 40, 34);

			g.setColor(g.getColor().darker());

			g.drawRect((engine.getWidth() / 2)
					- (g.getFontMetrics().stringWidth(endTurn) / 2) - 20, 2, g
					.getFontMetrics().stringWidth(endTurn) + 40, 34);

			g.setColor(Color.BLACK);

			g.drawString(endTurn, (engine.getWidth() / 2)
					- (g.getFontMetrics().stringWidth(endTurn) / 2), 20 + (g
					.getFontMetrics().getHeight() / 4));

			// RESET

			String reset = "RESET";

			xL = 4;
			xR = xL + (g.getFontMetrics().stringWidth(reset)) + 40;

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

				resetSel = true;

				g.setColor(GOLD.brighter());

			} else {

				resetSel = true;

				g.setColor(GOLD);
			}

			g.fillRect(xL, 2, g.getFontMetrics().stringWidth(reset) + 40, 34);

			g.setColor(g.getColor().darker());

			g.drawRect(xL, 2, g.getFontMetrics().stringWidth(reset) + 40, 34);

			g.setColor(Color.BLACK);

			g.drawString(reset, xL
					+ (g.getFontMetrics().stringWidth(reset) / 2) - 12, 20 + (g
					.getFontMetrics().getHeight() / 4));

			// UNDO MOVE

			String undo = "UNDO";

			xL = xR + 4;
			xR = xL + (g.getFontMetrics().stringWidth(undo)) + 40;

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

				undoSel = true;

				g.setColor(GOLD.brighter());

			} else {

				undoSel = false;

				g.setColor(GOLD);
			}

			g.fillRect(xL, 2, g.getFontMetrics().stringWidth(undo) + 40, 34);

			g.setColor(g.getColor().darker());

			g.drawRect(xL, 2, g.getFontMetrics().stringWidth(undo) + 40, 34);

			g.setColor(Color.BLACK);

			g.drawString(undo, xL + (g.getFontMetrics().stringWidth(undo) / 2)
					- 12, 20 + (g.getFontMetrics().getHeight() / 4));

			// INSTRUCTIONS

			String instructions = "INSTRUCTIONS";

			xL = engine.getWidth()
					- (g.getFontMetrics().stringWidth(instructions) + 44);
			xR = engine.getWidth() - 4;

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

				instructionsSel = true;

				g.setColor(GOLD.brighter());

			} else {

				instructionsSel = true;

				g.setColor(GOLD);
			}

			g.fillRect(xL, 2,
					g.getFontMetrics().stringWidth(instructions) + 40, 34);

			g.setColor(g.getColor().darker());

			g.drawRect(xL, 2,
					g.getFontMetrics().stringWidth(instructions) + 40, 34);

			g.setColor(Color.BLACK);

			g.drawString(instructions,
					xL + (g.getFontMetrics().stringWidth(instructions) / 2)
							- 54, 20 + (g.getFontMetrics().getHeight() / 4));

			// GAME BOARD

			g.setColor(Color.GREEN);

			g.fillRect((engine.getWidth() / 2)
					- ((engine.getHeight() - buttonBarHeight) / 2),
					buttonBarHeight, (engine.getHeight() - buttonBarHeight),
					(engine.getHeight() - buttonBarHeight));

			break;
		}
	}
}
