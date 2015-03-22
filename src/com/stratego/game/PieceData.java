package com.stratego.game;

import java.util.HashMap;
import java.util.Map;

import com.lutz.engine.game.GameManager;

public class PieceData {

	private static Map<Integer, Integer> pieceAmounts0 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> pieceAmounts1 = new HashMap<Integer, Integer>();

	private static Map<Integer, Integer> pieceAmountsDead0 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> pieceAmountsDead1 = new HashMap<Integer, Integer>();
	
	public static void setupAmounts() {

		GameManager.getLogger().log("Setting up piece amounts...");

		pieceAmounts0.put(1, 1);
		pieceAmounts0.put(2, 1);
		pieceAmounts0.put(3, 2);
		pieceAmounts0.put(4, 3);
		pieceAmounts0.put(5, 4);
		pieceAmounts0.put(6, 4);
		pieceAmounts0.put(7, 4);
		pieceAmounts0.put(8, 5);
		pieceAmounts0.put(9, 8);
		pieceAmounts0.put(10, 1);
		pieceAmounts0.put(11, 6);
		pieceAmounts0.put(12, 1);
		pieceAmounts1.put(1, 1);
		pieceAmounts1.put(2, 1);
		pieceAmounts1.put(3, 2);
		pieceAmounts1.put(4, 3);
		pieceAmounts1.put(5, 4);
		pieceAmounts1.put(6, 4);
		pieceAmounts1.put(7, 4);
		pieceAmounts1.put(8, 5);
		pieceAmounts1.put(9, 8);
		pieceAmounts1.put(10, 1);
		pieceAmounts1.put(11, 6);
		pieceAmounts1.put(12, 1);
		pieceAmountsDead0.clear();
		pieceAmountsDead1.clear();
	}

	public static int getPieceAmount(int side, int value) {

		if (side == 0) {

			if (pieceAmounts0.containsKey(value)) {

				return pieceAmounts0.get(value);
			}

		} else if (side == 1) {

			if (pieceAmounts1.containsKey(value)) {

				return pieceAmounts1.get(value);
			}
		}

		return 0;
	}
	
	public static int getDeadAmount(int side, int value) {

		if (side == 0) {

			if (pieceAmountsDead0.containsKey(value)) {

				return pieceAmountsDead0.get(value);
			}

		} else if (side == 1) {

			if (pieceAmountsDead1.containsKey(value)) {

				return pieceAmountsDead1.get(value);
			}
		}

		return 0;
	}

	public static void removePieceFromWell(int side, int value) {

		if (side == 0) {

			if (pieceAmounts0.containsKey(value)) {

				if (pieceAmounts0.get(value) > 0) {

					pieceAmounts0.put(value, pieceAmounts0.get(value) - 1);
				}
			}

		} else if (side == 1) {

			if (pieceAmounts1.containsKey(value)) {

				if (pieceAmounts1.get(value) > 0) {

					pieceAmounts1.put(value, pieceAmounts1.get(value) - 1);
				}
			}
		}
	}

	public static void addPieceToWell(int side, int value) {

		if (side == 0) {

			if (pieceAmounts0.containsKey(value)) {

				pieceAmounts0.put(value, pieceAmounts0.get(value) + 1);

			} else {

				pieceAmounts0.put(value, 1);
			}

		} else if (side == 1) {

			if (pieceAmounts1.containsKey(value)) {

				pieceAmounts1.put(value, pieceAmounts1.get(value) + 1);

			} else {

				pieceAmounts1.put(value, 1);
			}
		}
	}
	
	public static void killPiece(int side, int value) {

		if (side == 0) {

			if (pieceAmountsDead0.containsKey(value)) {

				pieceAmountsDead0.put(value, pieceAmountsDead0.get(value) + 1);

			} else {

				pieceAmountsDead0.put(value, 1);
			}

		} else if (side == 1) {

			if (pieceAmountsDead1.containsKey(value)) {

				pieceAmountsDead1.put(value, pieceAmountsDead1.get(value) + 1);

			} else {

				pieceAmountsDead1.put(value, 1);
			}
		}
	}

	public static void reset() {

		pieceAmounts0.clear();
		pieceAmounts1.clear();
		pieceAmountsDead0.clear();
		pieceAmountsDead1.clear();
	}
}
