package com.lutz.engine.resources;

import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.lutz.engine.core.LutzEngine;

public class FontResource {

	private static Map<String, Font> internal = new HashMap<String, Font>();

	private static Map<String, Font> external = new HashMap<String, Font>();

	public static Font getInternalFont(String path) {

		if (internal.containsKey(path)) {

			return internal.get(path);

		} else {

			InputStream fontStream = LutzEngine.class.getResourceAsStream(path);

			Font result;

			if (fontStream != null) {

				try {

					result = Font.createFont(Font.TRUETYPE_FONT, fontStream);

				} catch (Exception e) {

					LutzEngine.getEngineLogger().warn(
							"An error occurred while loading an internal font ('"
									+ path + "')!");

					LutzEngine.getEngineLogger().logException(e);

					result = null;
				}

			} else {

				LutzEngine.getEngineLogger().warn(
						"No internal font was found at '" + path + "'!");

				result = null;
			}

			internal.put(path, result);

			return result;
		}
	}

	public static Font getInternalFontIgnoreCache(String path) {

		if (internal.containsKey(path)) {

			internal.remove(path);
		}

		return getInternalFont(path);
	}

	public static Font getExternalFont(String path) {

		if (external.containsKey(path)) {

			return external.get(path);

		} else {

			File fontFile = new File(path);

			Font result;

			if (fontFile.exists()) {

				try {

					result = Font.createFont(Font.TRUETYPE_FONT, fontFile);

				} catch (Exception e) {

					LutzEngine.getEngineLogger().warn(
							"An error occurred while loading an external font ('"
									+ path + "')!");

					LutzEngine.getEngineLogger().logException(e);

					result = null;
				}

			} else {

				LutzEngine.getEngineLogger().warn(
						"No external font was found at '" + path + "'!");

				result = null;
			}

			external.put(path, result);

			return result;
		}
	}

	public static Font getExternalFontIgnoreCache(String path) {

		if (external.containsKey(path)) {

			external.remove(path);
		}

		return getExternalFont(path);
	}
}
