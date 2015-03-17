package com.lutz.engine.ui;

import com.lutz.engine.data.PersistentDataManager;
import com.lutz.engine.ui.graphics.resolutions.ScreenResolution;

public class UISettings {

	public static class PersistentDataEntries {

		public static final String RESOLUTION = "game-ui:resolution";
		public static final String TARGET_FPS = "game-ui:targetFPS";
		public static final String ANTI_ALIAS = "game-ui:antialias";
	}

	private static ScreenResolution currentResolution = ScreenResolution
			.getDefaultResolution();

	private static int targetFPS = 30;

	private static boolean antiAlias = true;

	public static void setup() {

		Object o = PersistentDataManager
				.getData(PersistentDataEntries.RESOLUTION);

		if (o != null) {

			if (o instanceof ScreenResolution) {

				currentResolution = (ScreenResolution) o;
			}
		}

		targetFPS = PersistentDataManager.getDataAsInt(
				PersistentDataEntries.TARGET_FPS, 30);

		antiAlias = PersistentDataManager.getDataAsBoolean(
				PersistentDataEntries.ANTI_ALIAS, true);
	}

	public static void setResolution(ScreenResolution resolution) {

		currentResolution = resolution;

		PersistentDataManager.putData(PersistentDataEntries.RESOLUTION,
				resolution);
	}

	public static ScreenResolution getResolution() {

		return currentResolution;
	}

	public static void setTargetFPS(int target) {

		targetFPS = target;

		PersistentDataManager.putData(PersistentDataEntries.TARGET_FPS, target);
	}

	public static int getTargetFPS() {

		return targetFPS;
	}

	public static void setAntiAlias(boolean antiAlias) {

		UISettings.antiAlias = antiAlias;

		PersistentDataManager.putData(PersistentDataEntries.ANTI_ALIAS,
				antiAlias);
	}

	public static boolean getAntiAlias() {

		return antiAlias;
	}
}
