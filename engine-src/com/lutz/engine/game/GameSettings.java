package com.lutz.engine.game;

import com.lutz.engine.data.PersistentDataManager;

public class GameSettings {

	public static class PersistentDataEntries {

		public static final String ALLOWED_LAYERS = "game:allowedLayers";
	}

	private static int allowedLayers = -1;

	public static void setup() {

		allowedLayers = PersistentDataManager.getDataAsInt(
				PersistentDataEntries.ALLOWED_LAYERS, -1);
	}

	public static void setAllowedLayers(int allowedLayers) {

		GameSettings.allowedLayers = allowedLayers;

		PersistentDataManager.putData("game:allowedLayers", allowedLayers);
	}

	public int getAllowedLayers() {

		return allowedLayers;
	}
}
