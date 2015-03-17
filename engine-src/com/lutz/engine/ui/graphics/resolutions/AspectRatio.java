package com.lutz.engine.ui.graphics.resolutions;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class AspectRatio {

	private static List<AspectRatio> supported = new ArrayList<AspectRatio>();

	private int width, height;

	public AspectRatio(int width, int height) {

		this.width = width;
		this.height = height;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}

	public static void addSupportedAspectRatio(int width, int height) {

		supported.add(new AspectRatio(width, height));
	}

	public static void addSupportedAspectRatio(AspectRatio ratio) {

		supported.add(ratio);
	}

	public static AspectRatio[] getSupportedAspectRatios() {

		return supported.toArray(new AspectRatio[] {});
	}

	public static AspectRatio getClosestAspectRatio(ScreenResolution res) {

		return getClosestAspectRatio(res.getWidth(), res.getHeight());
	}

	public static AspectRatio getClosestAspectRatio(int width, int height) {

		AspectRatio closest = new AspectRatio(0, 0);

		double closestDif = -1;

		for (AspectRatio r : getSupportedAspectRatios()) {

			double wDif = width / (double) r.getWidth();
			double hDif = height / (double) r.getHeight();

			double totDif = Math.abs(wDif - hDif);

			if (totDif < closestDif || closestDif < 1) {

				closestDif = totDif;
				closest = r;
			}
		}

		return closest;
	}

	public Dimension getUsableArea(ScreenResolution res) {

		int width = res.getWidth();
		int height = res.getHeight();

		double wDif = (double) width / (double) this.getWidth();
		double hDif = (double) height / (double) this.getHeight();

		if (wDif < hDif) {

			int newHeight = (int) Math.ceil(wDif * getHeight());

			return new Dimension(width, newHeight);

		} else if (wDif > hDif) {

			int newWidth = (int) Math.ceil(hDif * getWidth());

			return new Dimension(newWidth, height);

		} else {

			return new Dimension(width, height);
		}
	}
}
