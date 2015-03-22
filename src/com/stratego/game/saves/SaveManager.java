package com.stratego.game.saves;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.lutz.engine.game.GameManager;
import com.stratego.game.Piece;
import com.stratego.game.PieceData;
import com.stratego.game.ui.Screen;

public class SaveManager {

	public static void saveGame(Piece[] pieces, int turn, boolean hasMoved,
			boolean isInSetup, int timesSetup, boolean setupSideCompl) {

		GameManager.getLogger().log("Saving...");

		String toWrite = "";

		for (Piece p : pieces) {

			toWrite += "p:" + p.x + "," + p.y + "," + p.soldierRank + ","
					+ p.soldierSide + "|";
		}

		for (int side = 0; side <= 1; side++) {

			for (int rank = 1; rank <= 12; rank++) {

				for (int times = PieceData.getPieceAmount(side, rank); times > 0; times--) {

					toWrite += "wp:" + rank + "," + side + "|";
				}
			}
		}

		for (int side = 0; side <= 1; side++) {

			for (int rank = 1; rank <= 12; rank++) {

				for (int times = PieceData.getDeadAmount(side, rank); times > 0; times--) {

					toWrite += "dp:" + rank + "," + side + "|";
				}
			}
		}

		toWrite += "t:" + turn + "|";

		toWrite += "h:" + hasMoved + "|";

		toWrite += "s:" + isInSetup + "|";

		toWrite += "ts:" + timesSetup + "|";
		
		toWrite += "sc:" + setupSideCompl;

		File saveFile = new File("stratego.sav");

		try {

			saveFile.createNewFile();

			PrintStream ps = new PrintStream(saveFile);

			ps.println(toWrite);

			ps.close();

		} catch (Exception e) {

			GameManager.getLogger().warn("Unable to save the game!");
			GameManager.getLogger().logException(e);
		}
	}

	public static void loadGame() {

		GameManager.getLogger().log("Loading...");

		PieceData.reset();

		File saveFile = new File("stratego.sav");

		if (saveFile.exists()) {

			try {

				Scanner sc = new Scanner(saveFile);

				String read = "";

				while (sc.hasNextLine()) {

					read += sc.nextLine();
				}

				sc.close();

				String[] parts = read.split("\\|");

				List<Piece> pieces = new ArrayList<Piece>();
				int turnSide = 0;
				boolean hasMoved = false;
				boolean isInSetup = false;
				int timesSetup = 0;
				boolean setupSideCompl = false;

				for (String str : parts) {

					String[] strParts = str.split(":", 2);

					switch (strParts[0].toLowerCase()) {

					case "p":

						String[] data = strParts[1].split(",");

						if (data.length == 4) {

							int x, y, rank, side;

							try {

								x = Integer.parseInt(data[0]);
								y = Integer.parseInt(data[1]);
								rank = Integer.parseInt(data[2]);
								side = Integer.parseInt(data[3]);

								if (x >= 0 && x < 10 && y >= 0 && y < 10
										&& rank > 0 && rank <= 12
										&& (side == 0 || side == 1)) {

									pieces.add(new Piece(x, y, rank, side));
								}

							} catch (Exception e) {
							}
						}

						break;

					case "wp":

						String[] wellData = strParts[1].split(",");

						if (wellData.length == 2) {

							int rank, side;

							try {

								rank = Integer.parseInt(wellData[0]);
								side = Integer.parseInt(wellData[1]);

								if (rank > 0 && rank <= 12
										&& (side == 0 || side == 1)) {

									PieceData.addPieceToWell(side, rank);
								}

							} catch (Exception e) {
							}
						}

						break;

					case "dp":

						String[] deadData = strParts[1].split(",");

						if (deadData.length == 2) {

							int rank, side;

							try {

								rank = Integer.parseInt(deadData[0]);
								side = Integer.parseInt(deadData[1]);

								if (rank > 0 && rank <= 12
										&& (side == 0 || side == 1)) {

									PieceData.killPiece(side, rank);
								}

							} catch (Exception e) {
							}
						}

						break;

					case "t":

						try {

							int turn = Integer.parseInt(strParts[1]);

							if (turn == 0 || turn == 1) {

								turnSide = turn;
							}

						} catch (Exception e) {
						}

						break;

					case "h":

						try {

							boolean is = Boolean.parseBoolean(strParts[1]);

							hasMoved = is;

						} catch (Exception e) {
						}

						break;

					case "s":

						try {

							boolean has = Boolean.parseBoolean(strParts[1]);

							isInSetup = has;

						} catch (Exception e) {
						}

						break;

					case "ts":

						try {

							int times = Integer.parseInt(strParts[1]);

							if (times >= 0 && times <= 2) {

								timesSetup = times;
							}

						} catch (Exception e) {
						}

						break;

					case "sc":

						try {

							boolean is = Boolean.parseBoolean(strParts[1]);

							setupSideCompl = is;

						} catch (Exception e) {
						}

						break;
					}
				}

				Screen.setupSavegame(pieces.toArray(new Piece[] {}), turnSide,
						hasMoved, isInSetup, timesSetup, setupSideCompl);

			} catch (Exception e) {

				GameManager.getLogger().warn("Unable to load the game!");
				GameManager.getLogger().logException(e);

				Screen.startGame();
			}
		}
	}
}
