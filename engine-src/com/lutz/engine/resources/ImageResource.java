package com.lutz.engine.resources;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.lutz.engine.core.LutzEngine;

public class ImageResource {

	private static Map<String, ImageIcon> internal = new HashMap<String, ImageIcon>();

	private static Map<String, ImageIcon> external = new HashMap<String, ImageIcon>();

	public static ImageIcon getInternalImage(String path) {

		if (internal.containsKey(path)) {

			return internal.get(path);

		} else {

			InputStream imageStream = LutzEngine.class
					.getResourceAsStream(path);

			ImageIcon result;

			if (imageStream != null) {

				try {

					result = new ImageIcon(ImageIO.read(imageStream));

				} catch (Exception e) {

					LutzEngine.getEngineLogger().warn(
							"An error occurred while loading an internal image ('"
									+ path + "')!");

					LutzEngine.getEngineLogger().logException(e);

					result = null;
				}

			} else {

				LutzEngine.getEngineLogger().warn(
						"No internal image was found at '" + path + "'!");

				result = null;
			}

			internal.put(path, result);

			return result;
		}
	}

	public static ImageIcon getInternalImageIgnoreCache(String path) {

		if (internal.containsKey(path)) {

			internal.remove(path);
		}

		return getInternalImage(path);
	}

	public static ImageIcon getExternalImage(String path) {

		if (external.containsKey(path)) {

			return external.get(path);

		} else {

			File imageFile = new File(path);

			ImageIcon result;

			if (imageFile.exists()) {

				try {

					result = new ImageIcon(ImageIO.read(imageFile));

				} catch (Exception e) {

					LutzEngine.getEngineLogger().warn(
							"An error occurred while loading an external image ('"
									+ path + "')!");

					LutzEngine.getEngineLogger().logException(e);

					result = null;
				}

			} else {

				LutzEngine.getEngineLogger().warn(
						"No external image was found at '" + path + "'!");

				result = null;
			}

			external.put(path, result);

			return result;
		}
	}

	public static ImageIcon getExternalImageIgnoreCache(String path) {

		if (external.containsKey(path)) {

			external.remove(path);
		}

		return getExternalImage(path);
	}
}
