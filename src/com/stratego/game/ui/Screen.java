package com.stratego.game.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.lutz.engine.resources.FontResource;
import com.lutz.engine.ui.graphics.GraphicsEngine;
import com.stratego.game.MainGame;
import com.stratego.game.Movement;
import com.stratego.game.Tile;

public class Screen {

	/*
	 * Screen value for shown screen
	 * 
	 * 0 - Main menu 1 - Instructions screen 2 - Game screen
	 */
	private static int screenModeValue = 2, mouseX = 0, mouseY = 0,
			gridMouseX = -1, gridMouseY = -1, selectX = -1, selectY = -1;

	private static boolean pieceSelected = false;

	private static boolean startSel = false, loadSel = false, quitSel = false,
			resetSel = false, undoSel = false, endTurnSel = false,
			instructionsSel = false, mouseLockedToOverlay = false,
			instructionsCloseSel = false, instructionsShown = false;

	/**
	 * Retrieves the grid x/y value where the mouse is located (0-9)<br>
	 * If both values are -1 (there should never be a time where only one is),
	 * the mouse is not on the grid
	 */
	public static Point getMouseLocationOnGrid() {

		return new Point(gridMouseX, gridMouseY);
	}

	public static MouseListener getMouseListener() {

		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				switch (screenModeValue) {

				case 2:

					if (!mouseLockedToOverlay) {

						if (resetSel) {

						} else if (undoSel) {

						} else if (endTurnSel) {

						} else if (instructionsSel) {

							mouseLockedToOverlay = true;
							instructionsShown = true;

						} else if (gridMouseX >= 0 && gridMouseY >= 0) {

							if (MainGame.gameBoard[gridMouseX][gridMouseY].moveable
									&& MainGame.gameBoard[gridMouseX][gridMouseY].piece != null) {

								pieceSelected = true;
								selectX = gridMouseX;
								selectY = gridMouseY;
							}
						}

					} else if (instructionsShown) {

						if (instructionsCloseSel) {

							mouseLockedToOverlay = false;
							instructionsShown = false;
						}
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
					0, 0), PARCHMENT = new Color(255, 253, 150),
			RED_BUTTON_TOP = new Color(219, 32, 32),
			RED_BUTTON_BOTTOM = new Color(198, 22, 22), WOOD = new Color(60,
					61, 3), GRASS = Color.GREEN, WATER = Color.BLUE;

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

			// TODO: Make wooden pole next to banners

			// QUIT

			int xLeft = 20;
			int xRight = 400;
			int yTop = engine.getHeight() - 100;
			int yBottom = engine.getHeight() - 40;

			if (mX >= xLeft && mX <= xRight && mY >= yTop && mY <= yBottom) {

				quitSel = true;

				g.setColor(RED_BUTTON_BOTTOM.brighter());

			} else {

				quitSel = false;

				g.setColor(RED_BUTTON_BOTTOM);
			}

			g.fillPolygon(new int[] { 20, 20, 350, 400 },
					new int[] { engine.getHeight() - 40,
							engine.getHeight() - 70, engine.getHeight() - 70,
							engine.getHeight() - 40 }, 4);

			if (mX >= xLeft && mX <= xRight && mY >= yTop && mY <= yBottom) {

				quitSel = true;

				g.setColor(RED_BUTTON_TOP.brighter());

			} else {

				quitSel = false;

				g.setColor(RED_BUTTON_TOP);
			}

			g.fillPolygon(new int[] { 20, 20, 400, 350 },
					new int[] { engine.getHeight() - 70,
							engine.getHeight() - 100, engine.getHeight() - 100,
							engine.getHeight() - 70 }, 4);

			// LOAD

			yTop = engine.getHeight() - 160;
			yBottom = engine.getHeight() - 100;

			if (mX >= xLeft && mX <= xRight && mY >= yTop && mY < yBottom) {

				loadSel = true;

				g.setColor(RED_BUTTON_BOTTOM.brighter());

			} else {

				loadSel = false;

				g.setColor(RED_BUTTON_BOTTOM);
			}

			g.fillPolygon(new int[] { 20, 20, 350, 400 },
					new int[] { engine.getHeight() - 100,
							engine.getHeight() - 130, engine.getHeight() - 130,
							engine.getHeight() - 100 }, 4);

			if (mX >= xLeft && mX <= xRight && mY >= yTop && mY <= yBottom) {

				quitSel = true;

				g.setColor(RED_BUTTON_TOP.brighter());

			} else {

				quitSel = false;

				g.setColor(RED_BUTTON_TOP);
			}

			g.fillPolygon(new int[] { 20, 20, 400, 350 },
					new int[] { engine.getHeight() - 130,
							engine.getHeight() - 160, engine.getHeight() - 160,
							engine.getHeight() - 130 }, 4);

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

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB
					&& !mouseLockedToOverlay) {

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

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB
					&& !mouseLockedToOverlay) {

				resetSel = true;

				g.setColor(GOLD.brighter());

			} else {

				resetSel = false;

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

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB
					&& !mouseLockedToOverlay) {

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

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB
					&& !mouseLockedToOverlay) {

				instructionsSel = true;

				g.setColor(GOLD.brighter());

			} else {

				instructionsSel = false;

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

			// ==========
			// GAME BOARD
			// ==========

			// GRID

			int gridSquareSize = (int) Math
					.ceil(((double) engine.getHeight() - buttonBarHeight) / 10);

			int gXL = (engine.getWidth() / 2)
					- ((engine.getHeight() - buttonBarHeight) / 2);
			int gXR = (engine.getWidth() / 2)
					+ ((engine.getHeight() - buttonBarHeight) / 2);
			int gYT = buttonBarHeight;
			int gYB = engine.getHeight();

			for (int x = 0; x < 10; x++) {

				for (int y = 0; y < 10; y++) {

					Tile t = MainGame.gameBoard[x][y];

					if (t.moveable) {

						g.setColor(GRASS);

					} else {

						g.setColor(WATER);
					}

					g.fillRect(gXL + (gridSquareSize * x), gYT
							+ (gridSquareSize * y), gridSquareSize + 1,
							gridSquareSize + 1);
				}
			}

			// SHOW POSSIBLE MOVES

			if (pieceSelected) {

				Point[] possible = Movement
						.getMoveRange(MainGame.gameBoard[selectX][selectY].piece);

				for (Point p : possible) {

					if (MainGame.gameBoard[p.x][p.y].piece == null
							&& MainGame.gameBoard[p.x][p.y].moveable) {

						g.setColor(Color.GRAY);

					} else {

						g.setColor(Color.RED);
					}

					g.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, 0.3f));

					g.fillRect(gXL + (gridSquareSize * p.x), gYT
							+ (gridSquareSize * p.y), gridSquareSize,
							gridSquareSize);

					g.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, 0.6f));

					g.drawRect(gXL + (gridSquareSize * p.x), gYT
							+ (gridSquareSize * p.y), gridSquareSize,
							gridSquareSize);
				}
			}

			if (mX >= gXL && mX <= gXR && mY >= gYT && mY <= gYB
					&& !mouseLockedToOverlay) {

				for (int i = 0; i < 10; i++) {

					int xMin, xMax;

					xMin = gridSquareSize * i;
					xMax = gridSquareSize * (i + 1) - 1;

					if (mX >= (gXL + xMin) && mX <= (gXL + xMax)) {

						gridMouseX = i;
					}
				}

				for (int i = 0; i < 10; i++) {

					int yMin, yMax;

					yMin = gridSquareSize * i;
					yMax = gridSquareSize * (i + 1) - 1;

					if (mY >= (gYT + yMin) && mY <= (gYT + yMax)) {

						gridMouseY = i;
					}
				}

			} else {

				gridMouseX = -1;
				gridMouseY = -1;
			}

			if (gridMouseX >= 0 && gridMouseX < 10 && gridMouseY >= 0
					&& gridMouseY < 10) {

				if ((MainGame.gameBoard[gridMouseX][gridMouseY].moveable
						&& (pieceSelected && (MainGame.gameBoard[gridMouseX][gridMouseY].piece == null)) || !pieceSelected)) {

					g.setColor(Color.BLACK);

				} else {

					g.setColor(Color.RED);
				}

				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.8f));
				g.drawRect(gXL + (gridMouseX * gridSquareSize), gYT
						+ (gridMouseY * gridSquareSize), gridSquareSize - 1,
						gridSquareSize - 1);
			}

			// =========
			// TOP LEVEL
			// =========

			// INSTRUCTIONS

			if (instructionsShown) {

				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.6f));
				g.fillRect(0, 0, engine.getWidth(), engine.getHeight());

				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));

				g.setColor(PARCHMENT);
				int instHeight = engine.getHeight() - 100;
				g.fillRect((engine.getWidth() / 2) - (instHeight / 2), 50,
						instHeight, instHeight);
				g.setColor(PARCHMENT.darker());
				g.drawRect((engine.getWidth() / 2) - (instHeight / 2), 50,
						instHeight, instHeight);

				// INSTRUCTIONS CLOSE

				String instClose = "CLOSE";

				xL = (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(instClose) - 20);
				xR = (engine.getWidth() / 2)
						+ (g.getFontMetrics().stringWidth(instClose) + 20);
				yT = (engine.getHeight() / 2) + (instHeight / 2) - 42;
				yB = yT + 34;

				if (mX >= xL && mX <= xR && mY >= yT && mY <= yB
						&& instructionsShown) {

					instructionsCloseSel = true;

					g.setColor(GOLD.brighter());

				} else {

					instructionsCloseSel = false;

					g.setColor(GOLD);
				}

				g.fillRect(xL, yT,
						g.getFontMetrics().stringWidth(instClose) + 40, 34);

				g.setColor(g.getColor().darker());

				g.drawRect(xL, yT,
						g.getFontMetrics().stringWidth(instClose) + 40, 34);

				g.setColor(Color.BLACK);

				g.drawString(instClose,
						xL + 5 + (g.getFontMetrics().stringWidth(instClose))
								- 54, yT + 17
								+ (g.getFontMetrics().getHeight() / 4));
			}

			break;
		}
	}
}
