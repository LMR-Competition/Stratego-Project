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
import java.util.ArrayList;
import java.util.List;

import com.lutz.engine.game.GameManager;
import com.lutz.engine.resources.FontResource;
import com.lutz.engine.ui.graphics.GraphicsEngine;
import com.stratego.game.Combat;
import com.stratego.game.MainGame;
import com.stratego.game.Movement;
import com.stratego.game.Piece;
import com.stratego.game.PieceData;
import com.stratego.game.Tile;
import com.stratego.game.saves.SaveManager;

public class Screen {

	/*
	 * Screen value for shown screen
	 * 
	 * 0 - Main menu 1 - ???? 2 - Game screen
	 */
	private static int screenModeValue = 0, mouseX = 0, mouseY = 0,
			gridMouseX = -1, gridMouseY = -1, selectX = -1, selectY = -1,
			turnSide = 0, prevTurn = 1;

	private static boolean hasMoved = false, isInSetup = true,
			setupSideCompleted = false;

	public static boolean won = false;

	private static int startX = -1, startY = -1, movedX = -1, movedY = -1,
			prevSX = -1, prevSY = -1, prevMX = -1, prevMY = -1;

	private static int wellSideSelected = -1, wellPieceSelected = -1,
			wellSideClicked = -1, wellPieceClicked = -1, timesSetup = 0;

	private static int placeholderTurnSide = 0;

	private static Piece wellPiece;

	private static boolean pieceSelected = false, wellSelected = false,
			wellClicked = false;

	private static boolean startSel = false, loadSel = false, quitSel = false,
			newSel = false, saveSel = false, endTurnSel = false,
			endTurnCancel = false, instructionsSel = false,
			mouseLockedToOverlay = false, instructionsCloseSel = false,
			switchSel = false, loadContSel = false, winMenuSel = false,
			newYesSel = false, newNoSel = false;

	private static boolean instructionsShown = false,
			betweenTurnsShown = false, loadShown = false, newShown = false,
			wonShown = false;

	public static void startGame() {

		turnSide = 0;
		won = false;
		hasMoved = false;
		timesSetup = 0;
		pieceSelected = false;
		clearGame();
		PieceData.setupAmounts();
		isInSetup = true;
		screenModeValue = 2;
	}

	public static void clearGame() {

		for (Tile[] t1 : MainGame.gameBoard) {

			for (Tile t2 : t1) {

				t2.piece = null;
			}
		}
	}

	public static void setupSavegame(Piece[] pieces, int turn,
			boolean hasMoved, boolean inSetup, int timesSetup,
			boolean setupSideCompl) {

		clearGame();

		for (Piece p : pieces) {

			MainGame.gameBoard[p.x][p.y].piece = p;
		}

		Screen.placeholderTurnSide = turn;
		Screen.hasMoved = hasMoved;
		Screen.isInSetup = inSetup;
		Screen.timesSetup = timesSetup;
		Screen.setupSideCompleted = setupSideCompl;
		turnSide = 2;
		mouseLockedToOverlay = true;
		loadShown = true;
	}

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

				case 0:

					if (startSel) {

						startGame();

					} else if (loadSel) {

						SaveManager.loadGame();
						screenModeValue = 2;

					} else if (quitSel) {

						com.lutz.engine.ui.Screen.closeScreen();
					}

				case 2:

					if (!mouseLockedToOverlay) {

						if (newSel) {

							startGame();

						} else if (saveSel) {

							List<Piece> pieces = new ArrayList<Piece>();

							for (Tile[] t1 : MainGame.gameBoard) {

								for (Tile t2 : t1) {

									if (t2.piece != null) {

										pieces.add(t2.piece);
									}
								}
							}

							SaveManager.saveGame(
									pieces.toArray(new Piece[] {}), turnSide,
									hasMoved, isInSetup, timesSetup,
									setupSideCompleted);

						} else if (endTurnSel) {

							if (!isInSetup || (isInSetup && setupSideCompleted)) {

								if (timesSetup < 2) {

									timesSetup++;
								}

								if (timesSetup == 2) {

									isInSetup = false;
								}

								prevTurn = turnSide;
								turnSide = 2;
								pieceSelected = false;
								mouseLockedToOverlay = true;
								betweenTurnsShown = true;

								for (int x = 0; x < 10; x++) {

									for (int y = 0; y < 10; y++) {

										if (MainGame.gameBoard[x][y].piece != null) {

											if (MainGame.gameBoard[x][y].piece.jAFlag) {

												MainGame.gameBoard[x][y].piece.jAFlag = false;
												MainGame.gameBoard[x][y].piece.justAttacked = false;

											} else if (MainGame.gameBoard[x][y].piece.justAttacked) {

												MainGame.gameBoard[x][y].piece.jAFlag = true;
											}
										}
									}
								}
							}

						} else if (instructionsSel) {

							mouseLockedToOverlay = true;
							instructionsShown = true;

						} else if (wellSelected) {

							if (PieceData.getPieceAmount(wellSideSelected,
									wellPieceSelected) > 0) {

								wellClicked = true;
								wellSideClicked = wellSideSelected;
								wellPieceClicked = wellPieceSelected;
								wellPiece = new Piece(-1, -1, wellPieceClicked,
										wellSideClicked);

							} else {

								wellClicked = false;
							}

						} else if (gridMouseX >= 0 && gridMouseY >= 0
								&& !mouseLockedToOverlay) {

							if (isInSetup
									&& e.getButton() == MouseEvent.BUTTON3) {

								Piece p = MainGame.gameBoard[gridMouseX][gridMouseY].piece;

								if (p != null) {

									if (p.soldierSide == turnSide) {

										PieceData.addPieceToWell(p.soldierSide,
												p.soldierRank);
										MainGame.gameBoard[gridMouseX][gridMouseY].piece = null;

										boolean setupCompl = true;

										for (int rank = 1; rank <= 12; rank++) {

											if (PieceData.getPieceAmount(
													turnSide, rank) != 0) {

												setupCompl = false;
											}
										}

										setupSideCompleted = setupCompl;
									}
								}

							} else if (pieceSelected) {

								if (MainGame.gameBoard[gridMouseX][gridMouseY].piece == null) {

									if (Movement
											.canMoveHere(
													gridMouseX,
													gridMouseY,
													MainGame.gameBoard[selectX][selectY].piece,
													hasMoved, isInSetup)) {

										Movement.preparePiece(
												gridMouseX,
												gridMouseY,
												MainGame.gameBoard[selectX][selectY].piece.soldierRank,
												MainGame.gameBoard[selectX][selectY].piece.soldierSide);
										MainGame.gameBoard[selectX][selectY].piece = null;

										if (!isInSetup) {

											hasMoved = true;
											startX = selectX;
											startY = selectY;
											movedX = gridMouseX;
											movedY = gridMouseY;
										}

										pieceSelected = false;
										selectX = -1;
										selectY = -1;

									} else {

										pieceSelected = false;
										selectX = -1;
										selectY = -1;
									}

								} else if (MainGame.gameBoard[gridMouseX][gridMouseY].piece.soldierSide != turnSide) {

									if (Movement
											.canMoveHere(
													gridMouseX,
													gridMouseY,
													MainGame.gameBoard[selectX][selectY].piece,
													hasMoved, isInSetup)) {

										Combat.engage(
												MainGame.gameBoard[selectX][selectY].piece,
												MainGame.gameBoard[gridMouseX][gridMouseY].piece);

										if (won) {

											mouseLockedToOverlay = true;
											wonShown = true;
										}

										hasMoved = true;
										startX = selectX;
										startY = selectY;
										movedX = gridMouseX;
										movedY = gridMouseY;

										pieceSelected = false;
										selectX = -1;
										selectY = -1;

									} else {

										pieceSelected = false;
										selectX = -1;
										selectY = -1;
									}

								} else if (MainGame.gameBoard[gridMouseX][gridMouseY].piece.soldierSide == turnSide) {

									pieceSelected = true;
									selectX = gridMouseX;
									selectY = gridMouseY;
								}

							} else if (MainGame.gameBoard[gridMouseX][gridMouseY].moveable
									&& MainGame.gameBoard[gridMouseX][gridMouseY].piece != null) {

								if (MainGame.gameBoard[gridMouseX][gridMouseY].piece.soldierSide == turnSide) {

									pieceSelected = true;
									selectX = gridMouseX;
									selectY = gridMouseY;

								}

							} else if (isInSetup && wellClicked
									&& wellPiece != null) {

								if (MainGame.gameBoard[gridMouseX][gridMouseY].piece == null) {

									if (Movement.canMoveHere(gridMouseX,
											gridMouseY, wellPiece, hasMoved,
											isInSetup)) {

										Movement.preparePiece(gridMouseX,
												gridMouseY,
												wellPiece.soldierRank,
												wellPiece.soldierSide);

										PieceData.removePieceFromWell(
												wellSideClicked,
												wellPieceClicked);

										boolean setupCompl = true;

										for (int rank = 1; rank <= 12; rank++) {

											if (PieceData.getPieceAmount(
													turnSide, rank) != 0) {

												setupCompl = false;
											}
										}

										setupSideCompleted = setupCompl;

										wellClicked = false;
									}
								}
							}

						} else {

							wellClicked = false;
						}

					} else if (instructionsShown) {

						if (instructionsCloseSel) {

							mouseLockedToOverlay = false;
							instructionsShown = false;
						}

					} else if (betweenTurnsShown) {

						if (switchSel) {

							if (prevTurn == 0) {

								turnSide = 1;

							} else {

								turnSide = 0;
							}

							mouseLockedToOverlay = false;
							betweenTurnsShown = false;
							prevSX = startX;
							prevSY = startY;
							prevMX = movedX;
							prevMY = movedY;
							hasMoved = false;
						}

					} else if (loadShown) {

						if (loadContSel) {

							turnSide = placeholderTurnSide;
							loadShown = false;
							mouseLockedToOverlay = false;
						}

					} else if (wonShown) {

						if (winMenuSel) {

							wonShown = false;
							mouseLockedToOverlay = false;
							clearGame();
							screenModeValue = 0;
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

				if (e.isShiftDown() && e.isControlDown() && e.isAltDown()) {

					if (e.getKeyCode() == KeyEvent.VK_W) {

						GameManager.getLogger().log(
								"Cheat activated! (WIN_AUTO)");

						wonShown = true;
						mouseLockedToOverlay = true;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		};
	}

	public static final Color GOLD = new Color(255, 199, 0),
			DARK_RED = new Color(150, 20, 0), DARK_RED_BORDER = new Color(120,
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

			// WOOD POLE

			g.setColor(WOOD);

			g.fillRoundRect(0, engine.getHeight() / 2 - 60, 20,
					engine.getHeight() / 2 + 80, 20, 20);

			// QUIT

			int xLeft = 20;
			int xRight = 400;
			int yTop = engine.getHeight() - 200;
			int yBottom = engine.getHeight() - 140;

			if (mX >= xLeft && mX <= xRight && mY >= yTop && mY <= yBottom) {

				quitSel = true;

				g.setColor(RED_BUTTON_BOTTOM.brighter());

			} else {

				quitSel = false;

				g.setColor(RED_BUTTON_BOTTOM);
			}

			g.fillPolygon(new int[] { 20, 20, 350, 400 },
					new int[] { engine.getHeight() - 140,
							engine.getHeight() - 170, engine.getHeight() - 170,
							engine.getHeight() - 140 }, 4);

			if (quitSel) {

				g.setColor(RED_BUTTON_TOP.brighter());

			} else {

				g.setColor(RED_BUTTON_TOP);
			}

			g.fillPolygon(new int[] { 20, 20, 400, 350 },
					new int[] { engine.getHeight() - 170,
							engine.getHeight() - 200, engine.getHeight() - 200,
							engine.getHeight() - 170 }, 4);

			Font buttonFont = new Font("Times New Roman", Font.PLAIN, 2);
			buttonFont = buttonFont.deriveFont((float) engine
					.percentageFontSize(buttonFont, 0.06f));
			g.setFont(buttonFont);

			if (quitSel) {

				g.setColor(RED_BUTTON_BOTTOM.darker().darker());

			} else {

				g.setColor(RED_BUTTON_BOTTOM.darker().darker().darker());
			}

			g.drawString("QUIT", 40, engine.getHeight() - 140
					- (g.getFontMetrics().getHeight() / 4));

			// LOAD

			yTop = engine.getHeight() - 310;
			yBottom = engine.getHeight() - 250;

			if (mX >= xLeft && mX <= xRight && mY >= yTop && mY < yBottom) {

				loadSel = true;

				g.setColor(RED_BUTTON_BOTTOM.brighter());

			} else {

				loadSel = false;

				g.setColor(RED_BUTTON_BOTTOM);
			}

			g.fillPolygon(new int[] { 20, 20, 350, 400 },
					new int[] { engine.getHeight() - 250,
							engine.getHeight() - 280, engine.getHeight() - 280,
							engine.getHeight() - 250 }, 4);

			if (loadSel) {

				g.setColor(RED_BUTTON_TOP.brighter());

			} else {

				g.setColor(RED_BUTTON_TOP);
			}

			g.fillPolygon(new int[] { 20, 20, 400, 350 },
					new int[] { engine.getHeight() - 280,
							engine.getHeight() - 310, engine.getHeight() - 310,
							engine.getHeight() - 280 }, 4);

			if (loadSel) {

				g.setColor(RED_BUTTON_BOTTOM.darker().darker());

			} else {

				g.setColor(RED_BUTTON_BOTTOM.darker().darker().darker());
			}

			g.drawString("RESUME", 40, engine.getHeight() - 250
					- (g.getFontMetrics().getHeight() / 4));

			// START

			yTop = engine.getHeight() - 420;
			yBottom = engine.getHeight() - 360;

			if (mX >= xLeft && mX <= xRight && mY >= yTop && mY < yBottom) {

				startSel = true;

				g.setColor(RED_BUTTON_BOTTOM.brighter());

			} else {

				startSel = false;

				g.setColor(RED_BUTTON_BOTTOM);
			}

			g.fillPolygon(new int[] { 20, 20, 350, 400 },
					new int[] { engine.getHeight() - 360,
							engine.getHeight() - 390, engine.getHeight() - 390,
							engine.getHeight() - 360 }, 4);

			if (startSel) {

				g.setColor(RED_BUTTON_TOP.brighter());

			} else {

				g.setColor(RED_BUTTON_TOP);
			}

			g.fillPolygon(new int[] { 20, 20, 400, 350 },
					new int[] { engine.getHeight() - 390,
							engine.getHeight() - 420, engine.getHeight() - 420,
							engine.getHeight() - 390 }, 4);

			if (startSel) {

				g.setColor(RED_BUTTON_BOTTOM.darker().darker());

			} else {

				g.setColor(RED_BUTTON_BOTTOM.darker().darker().darker());
			}

			g.drawString("NEW GAME", 40, engine.getHeight() - 360
					- (g.getFontMetrics().getHeight() / 4));

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

			if (!isInSetup || (isInSetup && setupSideCompleted)) {

				if (mX >= xL && mX <= xR && mY >= yT && mY <= yB
						&& !mouseLockedToOverlay) {

					endTurnSel = true;

					g.setColor(GOLD.brighter());

				} else {

					endTurnSel = false;

					g.setColor(GOLD);
				}

			} else {

				endTurnSel = false;

				g.setColor(makeBlackAndWhite(GOLD));
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

			String newGame = "NEW GAME";

			xL = 4;
			xR = xL + (g.getFontMetrics().stringWidth(newGame)) + 40;

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB
					&& !mouseLockedToOverlay) {

				newSel = true;

				g.setColor(GOLD.brighter());

			} else {

				newSel = false;

				g.setColor(GOLD);
			}

			g.fillRect(xL, 2, g.getFontMetrics().stringWidth(newGame) + 40, 34);

			g.setColor(g.getColor().darker());

			g.drawRect(xL, 2, g.getFontMetrics().stringWidth(newGame) + 40, 34);

			g.setColor(Color.BLACK);

			g.drawString(newGame, xL
					+ (g.getFontMetrics().stringWidth(newGame) / 2) - 38,
					20 + (g.getFontMetrics().getHeight() / 4));

			// SAVE GAME

			String save = "SAVE GAME";

			xL = xR + 4;
			xR = xL + (g.getFontMetrics().stringWidth(save)) + 40;

			if (mX >= xL && mX <= xR && mY >= yT && mY <= yB
					&& !mouseLockedToOverlay) {

				saveSel = true;

				g.setColor(GOLD.brighter());

			} else {

				saveSel = false;

				g.setColor(GOLD);
			}

			g.fillRect(xL, 2, g.getFontMetrics().stringWidth(save) + 40, 34);

			g.setColor(g.getColor().darker());

			g.drawRect(xL, 2, g.getFontMetrics().stringWidth(save) + 40, 34);

			g.setColor(Color.BLACK);

			g.drawString(save, xL + (g.getFontMetrics().stringWidth(save) / 2)
					- 42, 20 + (g.getFontMetrics().getHeight() / 4));

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

					if (t.moveable) {

						g.setColor(Color.GRAY);
						g.setComposite(AlphaComposite.getInstance(
								AlphaComposite.SRC_OVER, 0.5f));

						g.drawRect(gXL + (gridSquareSize * x), gYT
								+ (gridSquareSize * y), gridSquareSize + 1,
								gridSquareSize + 1);

						g.setComposite(AlphaComposite.getInstance(
								AlphaComposite.SRC_OVER, 1f));
					}

					if ((prevSX == x && prevSY == y)
							|| (prevMX == x && prevMY == y)) {

						g.setColor(GOLD);

						g.setComposite(AlphaComposite.getInstance(
								AlphaComposite.SRC_OVER, 0.6f));

						g.fillRect(gXL + (gridSquareSize * x), gYT
								+ (gridSquareSize * y), gridSquareSize,
								gridSquareSize);

						g.setComposite(AlphaComposite.getInstance(
								AlphaComposite.SRC_OVER, 1f));

						g.drawRect(gXL + (gridSquareSize * x), gYT
								+ (gridSquareSize * y), gridSquareSize,
								gridSquareSize);

						g.setComposite(AlphaComposite.getInstance(
								AlphaComposite.SRC_OVER, 1f));
					}

					if (MainGame.gameBoard[x][y].piece != null) {

						Piece p = MainGame.gameBoard[x][y].piece;

						PieceDrawer.drawPiece(engine, p.soldierRank,
								p.soldierSide, gXL + (gridSquareSize * x), gYT
										+ (gridSquareSize * y), gridSquareSize,
								gridSquareSize, turnSide, x, y, false);
					}
				}
			}

			// ===========
			// PIECE WELLS
			// ===========

			int wellSize = ((engine.getWidth() / 2) - (gridSquareSize * 5) - 50) / 3;

			if (!mouseLockedToOverlay
					&& (mX >= 0 && mX <= (wellSize * 3) && mY >= 70 && mY <= 70 + (wellSize * 4))
					|| (mX >= engine.getWidth() - (wellSize * 3)
							&& mX <= engine.getWidth() && mY >= 70 && mY <= 70 + (wellSize * 4))) {

				wellSelected = true;

			} else {

				wellSelected = false;
				wellSideSelected = -1;
				wellPieceSelected = -1;
			}

			int currentSide = 0;
			int currentPiece = 1;

			for (int y = 0; y < 4; y++) {

				for (int x = 0; x < 3; x++) {

					if (turnSide == currentSide) {

						if (mX >= (x * wellSize)
								&& mX <= (wellSize * (x + 1)) - 1
								&& mY >= (wellSize * y + 70)
								&& mY <= (wellSize * (y + 1) + 70) - 1) {

							wellSideSelected = currentSide;
							wellPieceSelected = currentPiece;
						}
					}

					if (((wellSelected && wellSideSelected == currentSide && wellPieceSelected == currentPiece) || (wellClicked
							&& wellSideClicked == currentSide && wellPieceClicked == currentPiece))
							&& PieceData.getPieceAmount(currentSide,
									currentPiece) > 0) {

						g.setColor(PieceData.getPieceAmount(currentSide,
								currentPiece) > 0 ? PARCHMENT.brighter()
								: makeBlackAndWhite(PARCHMENT.brighter()));

					} else {

						g.setColor(PieceData.getPieceAmount(currentSide,
								currentPiece) > 0 ? PARCHMENT
								: makeBlackAndWhite(PARCHMENT));
					}

					g.fillRect(wellSize * x, wellSize * y + 70, wellSize + 1,
							wellSize + 1);

					g.setColor(PieceData.getPieceAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.darker()
							: makeBlackAndWhite(PARCHMENT.darker()));

					g.drawRect(wellSize * x, wellSize * y + 70, wellSize + 1,
							wellSize + 1);
					PieceDrawer
							.drawPiece(engine, currentPiece, currentSide,
									wellSize * x + 8, wellSize * y + 78,
									wellSize - 16, wellSize - 16, -81, -1, -1,
									PieceData.getPieceAmount(currentSide,
											currentPiece) == 0);

					if (wellSelected
							&& wellSideSelected == currentSide
							&& wellPieceSelected == currentPiece
							&& PieceData.getPieceAmount(currentSide,
									currentPiece) > 0) {

						g.setColor(PieceData.getPieceAmount(currentSide,
								currentPiece) > 0 ? PARCHMENT.brighter()
								.brighter() : makeBlackAndWhite(PARCHMENT
								.brighter().brighter()));

					} else {

						g.setColor(PieceData.getPieceAmount(currentSide,
								currentPiece) > 0 ? PARCHMENT.brighter()
								: makeBlackAndWhite(PARCHMENT.brighter()));
					}

					g.fillOval((wellSize * (x + 1)) - 25,
							(wellSize * (y + 1)) + 45, 22, 22);

					g.setColor(PieceData.getPieceAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.darker().darker()
							.darker() : makeBlackAndWhite(PARCHMENT.darker()
							.darker().darker()));

					Font amountFont = new Font("Times New Roman", Font.BOLD, 2);
					amountFont = amountFont.deriveFont((float) engine
							.percentageFontSize(amountFont, 0.02f));
					g.setFont(amountFont);

					g.drawString(
							Integer.toString(PieceData.getPieceAmount(
									currentSide, currentPiece)),
							(wellSize * (x + 1))
									- g.getFontMetrics().stringWidth(
											Integer.toString(PieceData
													.getPieceAmount(
															currentSide,
															currentPiece)))
									- 10, (wellSize * (y + 1))
									- g.getFontMetrics().getHeight() + 83);

					currentPiece++;
				}
			}

			if (turnSide != currentSide) {

				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.4f));

				g.fillRect(0, buttonBarHeight, (engine.getWidth() / 2)
						- (gridSquareSize * 5), engine.getHeight()
						- buttonBarHeight);

				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));
			}

			// RIGHT SIDE

			currentSide = 1;
			currentPiece = 1;

			for (int y = 0; y < 4; y++) {

				for (int x = 2; x >= 0; x--) {

					if (turnSide == currentSide) {

						if (mX >= engine.getWidth() - ((x + 1) * wellSize)
								&& mX <= engine.getWidth() - (wellSize * (x))
										- 1 && mY >= (wellSize * y + 70)
								&& mY <= (wellSize * (y + 1) + 70) - 1) {

							wellSideSelected = currentSide;
							wellPieceSelected = currentPiece;
						}
					}

					if (((wellSelected && wellSideSelected == currentSide && wellPieceSelected == currentPiece) || (wellClicked
							&& wellSideClicked == currentSide && wellPieceClicked == currentPiece))
							&& PieceData.getPieceAmount(currentSide,
									currentPiece) > 0) {

						g.setColor(PieceData.getPieceAmount(currentSide,
								currentPiece) > 0 ? PARCHMENT.brighter()
								: makeBlackAndWhite(PARCHMENT.brighter()));

					} else {

						g.setColor(PieceData.getPieceAmount(currentSide,
								currentPiece) > 0 ? PARCHMENT
								: makeBlackAndWhite(PARCHMENT));
					}

					g.fillRect(engine.getWidth() - (wellSize * (x + 1)),
							wellSize * y + 70, wellSize + 1, wellSize + 1);

					g.setColor(PieceData.getPieceAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.darker()
							: makeBlackAndWhite(PARCHMENT.darker()));

					g.drawRect(engine.getWidth() - (wellSize * (x + 1)),
							wellSize * y + 70, wellSize + 1, wellSize + 1);

					PieceDrawer
							.drawPiece(engine, currentPiece, currentSide,
									engine.getWidth() - (wellSize * (x + 1))
											+ 8, wellSize * y + 78,
									wellSize - 16, wellSize - 16, -81, -1, -1,
									PieceData.getPieceAmount(currentSide,
											currentPiece) == 0);

					if (wellSelected
							&& wellSideSelected == currentSide
							&& wellPieceSelected == currentPiece
							&& PieceData.getPieceAmount(currentSide,
									currentPiece) > 0) {

						g.setColor(PieceData.getPieceAmount(currentSide,
								currentPiece) > 0 ? PARCHMENT.brighter()
								.brighter() : makeBlackAndWhite(PARCHMENT
								.brighter().brighter()));

					} else {

						g.setColor(PieceData.getPieceAmount(currentSide,
								currentPiece) > 0 ? PARCHMENT.brighter()
								: makeBlackAndWhite(PARCHMENT.brighter()));
					}

					g.fillOval(engine.getWidth() - (wellSize * x) - 25,
							(wellSize * (y + 1)) + 45, 22, 22);

					g.setColor(PieceData.getPieceAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.darker().darker()
							.darker() : makeBlackAndWhite(PARCHMENT.darker()
							.darker().darker()));

					Font amountFont = new Font("Times New Roman", Font.BOLD, 2);
					amountFont = amountFont.deriveFont((float) engine
							.percentageFontSize(amountFont, 0.02f));
					g.setFont(amountFont);

					g.drawString(
							Integer.toString(PieceData.getPieceAmount(
									currentSide, currentPiece)),
							engine.getWidth()
									- (wellSize * x)
									- g.getFontMetrics().stringWidth(
											Integer.toString(PieceData
													.getPieceAmount(
															currentSide,
															currentPiece)))
									- 10, (wellSize * (y + 1))
									- g.getFontMetrics().getHeight() + 83);

					currentPiece++;
				}
			}

			if (turnSide != currentSide) {

				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.4f));

				g.fillRect((engine.getWidth() / 2) + (gridSquareSize * 5),
						buttonBarHeight, (engine.getWidth() / 2)
								- (gridSquareSize * 5), engine.getHeight()
								- buttonBarHeight);

				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));
			}

			// GRAVEYARD LEFT
			int graveSize = ((engine.getWidth() / 2) - (gridSquareSize * 5) - 50) / 4;

			currentSide = 0;
			currentPiece = 1;

			Font graveFont = new Font("Times New Roman", Font.PLAIN, 2);
			graveFont = graveFont.deriveFont((float) engine.percentageFontSize(
					graveFont, 0.025f));
			g.setFont(graveFont);
			g.setColor(PARCHMENT);

			g.fillRect(
					0,
					engine.getHeight() - (graveSize * 3)
							- (g.getFontMetrics().getHeight()) - 10,
					graveSize * 4, (g.getFontMetrics().getHeight() / 2) + 20);
			g.setColor(PARCHMENT.darker());
			g.drawRect(
					0,
					engine.getHeight() - (graveSize * 3)
							- (g.getFontMetrics().getHeight()) - 10,
					graveSize * 4, (g.getFontMetrics().getHeight() / 2) + 20);

			g.setColor(PARCHMENT.darker().darker().darker());
			g.drawString("GRAVEYARD", 5, engine.getHeight() - (graveSize * 3)
					- (g.getFontMetrics().getHeight() / 2));

			g.setColor(PARCHMENT);

			g.fillRect(engine.getWidth() - (graveSize * 4), engine.getHeight()
					- (graveSize * 3) - (g.getFontMetrics().getHeight()) - 10,
					graveSize * 4, (g.getFontMetrics().getHeight() / 2) + 20);
			g.setColor(PARCHMENT.darker());
			g.drawRect(engine.getWidth() - (graveSize * 4), engine.getHeight()
					- (graveSize * 3) - (g.getFontMetrics().getHeight()) - 10,
					graveSize * 4, (g.getFontMetrics().getHeight() / 2) + 20);

			g.setColor(PARCHMENT.darker().darker().darker());
			g.drawString("GRAVEYARD",
					(engine.getWidth() - (graveSize * 4)) + 5,
					engine.getHeight() - (graveSize * 3)
							- (g.getFontMetrics().getHeight() / 2));

			for (int y = 2; y >= 0; y--) {

				for (int x = 0; x < 4; x++) {

					g.setColor(PieceData.getDeadAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT
							: makeBlackAndWhite(PARCHMENT));

					g.fillRect(graveSize * x, engine.getHeight()
							- (graveSize * (y + 2)) + 70, graveSize + 1,
							graveSize + 1);

					g.setColor(PieceData.getDeadAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.darker()
							: makeBlackAndWhite(PARCHMENT.darker()));

					g.drawRect(graveSize * x, engine.getHeight() - graveSize
							* (y + 2) + 70, graveSize + 1, graveSize + 1);
					PieceDrawer
							.drawPiece(engine, currentPiece, currentSide,
									graveSize * x + 8, engine.getHeight()
											- graveSize * (y + 2) + 78,
									graveSize - 16, graveSize - 16, -81, -1,
									-1, PieceData.getDeadAmount(currentSide,
											currentPiece) == 0);

					g.setColor(PieceData.getDeadAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.brighter()
							: makeBlackAndWhite(PARCHMENT.brighter()));

					g.fillOval((graveSize * (x + 1)) - 19, engine.getHeight()
							- (graveSize * (y + 1)) + 51, 16, 16);

					g.setColor(PieceData.getDeadAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.darker().darker()
							.darker() : makeBlackAndWhite(PARCHMENT.darker()
							.darker().darker()));

					Font amountFont = new Font("Times New Roman", Font.BOLD, 2);
					amountFont = amountFont.deriveFont((float) engine
							.percentageFontSize(amountFont, 0.015f));
					g.setFont(amountFont);

					g.drawString(
							Integer.toString(PieceData.getDeadAmount(
									currentSide, currentPiece)),
							(graveSize * (x + 1))
									- g.getFontMetrics().stringWidth(
											Integer.toString(PieceData
													.getDeadAmount(currentSide,
															currentPiece))) - 6,
							engine.getHeight() - (graveSize * (y + 1))
									- g.getFontMetrics().getHeight() + 81);

					currentPiece++;
				}
			}

			// GRAVEYARD RIGHT

			currentSide = 1;
			currentPiece = 1;

			for (int y = 2; y >= 0; y--) {

				for (int x = 3; x >= 0; x--) {

					g.setColor(PieceData.getDeadAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT
							: makeBlackAndWhite(PARCHMENT));

					g.fillRect(engine.getWidth() - (graveSize * (x + 1)),
							engine.getHeight() - (graveSize * (y + 2)) + 70,
							graveSize + 1, graveSize + 1);

					g.setColor(PieceData.getDeadAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.darker()
							: makeBlackAndWhite(PARCHMENT.darker()));

					g.drawRect(engine.getWidth() - (graveSize * (x + 1)),
							engine.getHeight() - graveSize * (y + 2) + 70,
							graveSize + 1, graveSize + 1);
					PieceDrawer
							.drawPiece(
									engine,
									currentPiece,
									currentSide,
									engine.getWidth() - graveSize * (x + 1) + 8,
									engine.getHeight() - graveSize * (y + 2)
											+ 78, graveSize - 16,
									graveSize - 16, -81, -1, -1, PieceData
											.getDeadAmount(currentSide,
													currentPiece) == 0);

					g.setColor(PieceData.getDeadAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.brighter()
							: makeBlackAndWhite(PARCHMENT.brighter()));

					g.fillOval(engine.getWidth() - (graveSize * (x)) - 19,
							engine.getHeight() - (graveSize * (y + 1)) + 51,
							16, 16);

					g.setColor(PieceData.getDeadAmount(currentSide,
							currentPiece) > 0 ? PARCHMENT.darker().darker()
							.darker() : makeBlackAndWhite(PARCHMENT.darker()
							.darker().darker()));

					Font amountFont = new Font("Times New Roman", Font.BOLD, 2);
					amountFont = amountFont.deriveFont((float) engine
							.percentageFontSize(amountFont, 0.015f));
					g.setFont(amountFont);

					g.drawString(
							Integer.toString(PieceData.getDeadAmount(
									currentSide, currentPiece)),
							engine.getWidth()
									- (graveSize * (x))
									- g.getFontMetrics().stringWidth(
											Integer.toString(PieceData
													.getDeadAmount(currentSide,
															currentPiece))) - 6,
							engine.getHeight() - (graveSize * (y + 1))
									- g.getFontMetrics().getHeight() + 81);

					currentPiece++;
				}
			}

			// SHOW POSSIBLE MOVES

			if (pieceSelected) {

				if (MainGame.gameBoard[selectX][selectY].piece != null) {

					Point[] possible = Movement.getMoveRange(
							MainGame.gameBoard[selectX][selectY].piece,
							hasMoved, isInSetup);

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

			} else if (isInSetup && wellClicked && wellPiece != null) {

				Point[] possible = Movement.getMoveRange(wellPiece, hasMoved,
						isInSetup);

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

			// MOUSE BOXES

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
					&& gridMouseY < 10
					&& MainGame.gameBoard[gridMouseX][gridMouseY].moveable) {

				g.setColor(Color.BLACK);

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
				int instHeight = 600;
				g.fillRect((engine.getWidth() / 2) - (instHeight / 2),
						(engine.getHeight() / 2) - (instHeight / 2),
						instHeight, instHeight);
				g.setColor(PARCHMENT.darker());
				g.drawRect((engine.getWidth() / 2) - (instHeight / 2),
						(engine.getHeight() / 2) - (instHeight / 2),
						instHeight, instHeight);

				// INSTRUCTIONS CLOSE

				g.setFont(font);

				String instClose = "CLOSE";

				xL = (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(instClose) - 20);
				xR = (engine.getWidth() / 2)
						+ (g.getFontMetrics().stringWidth(instClose) - 7);
				yT = (engine.getHeight() / 2) + (instHeight / 2) - 42;
				yB = yT + 34;

				if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

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

			// BETWEEN TURNS

			if (betweenTurnsShown) {

				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.6f));
				g.fillRect(0, 0, engine.getWidth(), engine.getHeight());

				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));

				g.setColor(PARCHMENT);
				int switchWidth = 700;
				int switchHeight = 200;
				g.fillRect((engine.getWidth() / 2) - (switchWidth / 2),
						(engine.getHeight() / 2) - (switchHeight / 2),
						switchWidth, switchHeight);
				g.setColor(PARCHMENT.darker());
				g.drawRect((engine.getWidth() / 2) - (switchWidth / 2),
						(engine.getHeight() / 2) - (switchHeight / 2),
						switchWidth, switchHeight);

				Font messageFont = new Font("Times New Roman", Font.PLAIN, 2);
				messageFont = messageFont.deriveFont((float) engine
						.percentageFontSize(messageFont, 0.06f));
				g.setFont(messageFont);

				g.setColor(PARCHMENT.darker().darker().darker());

				String switchMessage = "Switch players...";
				g.drawString(switchMessage, (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(switchMessage) / 2),
						(engine.getHeight() / 2)
								- (g.getFontMetrics().getHeight() / 4));

				// BETWEEN TURNS CONTINUE

				g.setFont(font);

				String switchClose = "CONTINUE";

				xL = (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(switchClose) - 20);
				xR = (engine.getWidth() / 2)
						+ (g.getFontMetrics().stringWidth(switchClose) - 7);
				yT = (engine.getHeight() / 2) + (switchHeight / 2) - 42;
				yB = yT + 34;

				if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

					switchSel = true;

					g.setColor(GOLD.brighter());

				} else {

					switchSel = false;

					g.setColor(GOLD);
				}

				g.fillRect(xL, yT,
						g.getFontMetrics().stringWidth(switchClose) + 40, 34);

				g.setColor(g.getColor().darker());

				g.drawRect(xL, yT,
						g.getFontMetrics().stringWidth(switchClose) + 40, 34);

				g.setColor(Color.BLACK);

				g.drawString(switchClose,
						xL + 5 + (g.getFontMetrics().stringWidth(switchClose))
								- 94, yT + 17
								+ (g.getFontMetrics().getHeight() / 4));
			}

			// LOAD GAME

			if (loadShown) {

				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.6f));
				g.fillRect(0, 0, engine.getWidth(), engine.getHeight());

				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));

				g.setColor(PARCHMENT);
				int switchWidth = 700;
				int switchHeight = 200;
				g.fillRect((engine.getWidth() / 2) - (switchWidth / 2),
						(engine.getHeight() / 2) - (switchHeight / 2),
						switchWidth, switchHeight);
				g.setColor(PARCHMENT.darker());
				g.drawRect((engine.getWidth() / 2) - (switchWidth / 2),
						(engine.getHeight() / 2) - (switchHeight / 2),
						switchWidth, switchHeight);

				Font messageFont = new Font("Times New Roman", Font.PLAIN, 2);
				messageFont = messageFont.deriveFont((float) engine
						.percentageFontSize(messageFont, 0.06f));
				g.setFont(messageFont);

				g.setColor(PARCHMENT.darker().darker().darker());

				String switchMessage = "'s turn...";

				if (placeholderTurnSide == 0) {

					switchMessage = "Blue" + switchMessage;

				} else {

					switchMessage = "Red" + switchMessage;
				}

				g.drawString(switchMessage, (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(switchMessage) / 2),
						(engine.getHeight() / 2)
								- (g.getFontMetrics().getHeight() / 4));

				// BETWEEN TURNS CONTINUE

				g.setFont(font);

				String switchClose = "CONTINUE";

				xL = (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(switchClose) - 20);
				xR = (engine.getWidth() / 2)
						+ (g.getFontMetrics().stringWidth(switchClose) - 7);
				yT = (engine.getHeight() / 2) + (switchHeight / 2) - 42;
				yB = yT + 34;

				if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

					loadContSel = true;

					g.setColor(GOLD.brighter());

				} else {

					loadContSel = false;

					g.setColor(GOLD);
				}

				g.fillRect(xL, yT,
						g.getFontMetrics().stringWidth(switchClose) + 40, 34);

				g.setColor(g.getColor().darker());

				g.drawRect(xL, yT,
						g.getFontMetrics().stringWidth(switchClose) + 40, 34);

				g.setColor(Color.BLACK);

				g.drawString(switchClose,
						xL + 5 + (g.getFontMetrics().stringWidth(switchClose))
								- 94, yT + 17
								+ (g.getFontMetrics().getHeight() / 4));
			}

			// CHECK NEW GAME

			if (newShown) {

				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.6f));
				g.fillRect(0, 0, engine.getWidth(), engine.getHeight());

				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));

				g.setColor(PARCHMENT);
				int switchWidth = 700;
				int switchHeight = 200;
				g.fillRect((engine.getWidth() / 2) - (switchWidth / 2),
						(engine.getHeight() / 2) - (switchHeight / 2),
						switchWidth, switchHeight);
				g.setColor(PARCHMENT.darker());
				g.drawRect((engine.getWidth() / 2) - (switchWidth / 2),
						(engine.getHeight() / 2) - (switchHeight / 2),
						switchWidth, switchHeight);

				Font messageFont = new Font("Times New Roman", Font.PLAIN, 2);
				messageFont = messageFont.deriveFont((float) engine
						.percentageFontSize(messageFont, 0.06f));
				g.setFont(messageFont);

				g.setColor(PARCHMENT.darker().darker().darker());

				String switchMessage = "Are you sure?";

				g.drawString(switchMessage, (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(switchMessage) / 2),
						(engine.getHeight() / 2)
								- (g.getFontMetrics().getHeight() / 4));

				// BETWEEN TURNS CONTINUE

				g.setFont(font);

				String yesButton = "YES";
				String noButton = "NO";

				xL = (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(yesButton) - 20);
				xR = (engine.getWidth() / 2)
						+ (g.getFontMetrics().stringWidth(noButton) - 7);
				yT = (engine.getHeight() / 2) + (switchHeight / 2) - 42;
				yB = yT + 34;

				if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

					loadContSel = true;

					g.setColor(GOLD.brighter());

				} else {

					loadContSel = false;

					g.setColor(GOLD);
				}

				g.fillRect(xL, yT,
						g.getFontMetrics().stringWidth(yesButton) + 40, 34);

				g.setColor(g.getColor().darker());

				g.drawRect(xL, yT,
						g.getFontMetrics().stringWidth(yesButton) + 40, 34);

				g.setColor(Color.BLACK);

				g.drawString(yesButton,
						xL + 5 + (g.getFontMetrics().stringWidth(yesButton))
								- 94, yT + 17
								+ (g.getFontMetrics().getHeight() / 4));
			}

			// WIN SCREEN

			if (wonShown) {

				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.6f));
				g.fillRect(0, 0, engine.getWidth(), engine.getHeight());

				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));

				g.setColor(PARCHMENT);
				int switchWidth = 700;
				int switchHeight = 200;
				g.fillRect((engine.getWidth() / 2) - (switchWidth / 2),
						(engine.getHeight() / 2) - (switchHeight / 2),
						switchWidth, switchHeight);
				g.setColor(PARCHMENT.darker());
				g.drawRect((engine.getWidth() / 2) - (switchWidth / 2),
						(engine.getHeight() / 2) - (switchHeight / 2),
						switchWidth, switchHeight);

				Font messageFont = new Font("Times New Roman", Font.PLAIN, 2);
				messageFont = messageFont.deriveFont((float) engine
						.percentageFontSize(messageFont, 0.06f));
				g.setFont(messageFont);

				g.setColor(PARCHMENT.darker().darker().darker());

				String switchMessage = " wins!";

				if (turnSide == 0) {

					switchMessage = "Blue " + switchMessage;

				} else {

					switchMessage = "Red " + switchMessage;
				}

				g.drawString(switchMessage, (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(switchMessage) / 2),
						(engine.getHeight() / 2)
								- (g.getFontMetrics().getHeight() / 4));

				// BETWEEN TURNS CONTINUE

				g.setFont(font);

				String switchClose = "MENU";

				xL = (engine.getWidth() / 2)
						- (g.getFontMetrics().stringWidth(switchClose) - 20);
				xR = (engine.getWidth() / 2)
						+ (g.getFontMetrics().stringWidth(switchClose) - 7);
				yT = (engine.getHeight() / 2) + (switchHeight / 2) - 42;
				yB = yT + 34;

				if (mX >= xL && mX <= xR && mY >= yT && mY <= yB) {

					winMenuSel = true;

					g.setColor(GOLD.brighter());

				} else {

					winMenuSel = false;

					g.setColor(GOLD);
				}

				g.fillRect(xL, yT,
						g.getFontMetrics().stringWidth(switchClose) + 40, 34);

				g.setColor(g.getColor().darker());

				g.drawRect(xL, yT,
						g.getFontMetrics().stringWidth(switchClose) + 40, 34);

				g.setColor(Color.BLACK);

				g.drawString(switchClose,
						xL + 5 + (g.getFontMetrics().stringWidth(switchClose))
								- 48, yT + 17
								+ (g.getFontMetrics().getHeight() / 4));
			}

			break;
		}
	}

	public static Color makeBlackAndWhite(Color color) {

		int cAv = (color.getRed() + color.getBlue() + color.getGreen()) / 3;

		return new Color(cAv, cAv, cAv);
	}
}
