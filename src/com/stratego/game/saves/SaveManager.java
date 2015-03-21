package com.stratego.game.saves;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.lutz.engine.game.GameManager;
import com.stratego.game.Piece;
import com.stratego.game.ui.Screen;

public class SaveManager {

	public static void saveGame(Piece[] pieces, int turn, boolean hasMoved) {

		GameManager.getLogger().log("Saving...");

		String toWrite = "";

		for (Piece p : pieces) {

			toWrite += "p:" + p.x + "," + p.y + "," + p.soldierRank + ","
					+ p.soldierSide + "|";
		}

		toWrite += "t:" + turn + "|";

		toWrite += "h:" + hasMoved;

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

		File saveFile = new File("stratego.sav");

		if (saveFile.exists()) {

			try {

				Scanner sc = new Scanner(saveFile);

				String read = "";

				while (sc.hasNextLine()) {

					read += sc.nextLine();
				}

				String[] parts = read.split("\\|");

				List<Piece> pieces = new ArrayList<Piece>();
				int turnSide = 0;
				boolean hasMoved = false;

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

							boolean has = Boolean.parseBoolean(strParts[1]);

							hasMoved = has;

						} catch (Exception e) {
						}

						break;
					}
				}

				Screen.setupSavegame(pieces.toArray(new Piece[] {}), turnSide,
						hasMoved);

			} catch (Exception e) {

				GameManager.getLogger().warn("Unable to load the game!");
				GameManager.getLogger().logException(e);

				Screen.startGame();
			}
		}
	}
}
