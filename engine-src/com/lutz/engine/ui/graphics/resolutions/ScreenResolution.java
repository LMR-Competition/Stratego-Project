package com.lutz.engine.ui.graphics.resolutions;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

public class ScreenResolution {

	private static ScreenResolution[] supportedResolutions = null;

	private static ScreenResolution defaultResolution = null;

	private int width, height;

	private ScreenResolution(int width, int height) {

		this.width = width;
		this.height = height;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}

	private static class ResolutionList {

		private List<ScreenResolution> res = new ArrayList<ScreenResolution>();

		public void addResolution(ScreenResolution resolution) {

			boolean duplicate = false;

			for (ScreenResolution r : res) {

				if (r.getWidth() == resolution.getWidth()
						&& r.getHeight() == resolution.getHeight()) {

					duplicate = true;

					break;
				}
			}

			if (!duplicate) {

				res.add(resolution);
			}
		}

		public ScreenResolution[] getResolutions() {

			return res.toArray(new ScreenResolution[] {});
		}
	}

	public static void setup() {

		ResolutionList res = new ResolutionList();

		GraphicsDevice device = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		for (DisplayMode mode : device.getDisplayModes()) {

			res.addResolution(new ScreenResolution(mode.getWidth(), mode
					.getHeight()));
		}

		supportedResolutions = res.getResolutions();

		DisplayMode defaultMode = device.getDisplayMode();

		defaultResolution = new ScreenResolution(defaultMode.getWidth(),
				defaultMode.getHeight());
	}

	public static ScreenResolution[] getSupportedResolutions() {

		return supportedResolutions;
	}

	public static ScreenResolution getDefaultResolution() {

		return defaultResolution;
	}

	public static ScreenResolution getScreenResolutionForDimensions(int width,
			int height) {

		for (ScreenResolution r : getSupportedResolutions()) {

			if (r.getWidth() == width && r.getHeight() == height) {

				return r;
			}
		}

		return getDefaultResolution();
	}

	public DisplayMode getModeForResolution() {

		List<DisplayMode> matches = new ArrayList<DisplayMode>();

		for (DisplayMode mode : GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDisplayModes()) {

			if (mode.getWidth() == this.getWidth()
					&& mode.getHeight() == this.getHeight()) {

				matches.add(mode);
			}
		}

		int bitDepth = 0, refreshRate = 0;

		for (DisplayMode mode : matches) {

			if (mode.getBitDepth() > bitDepth) {

				bitDepth = mode.getBitDepth();
			}

			if (mode.getRefreshRate() > refreshRate) {

				refreshRate = mode.getRefreshRate();
			}
		}

		return new DisplayMode(this.getWidth(), this.getHeight(), bitDepth,
				refreshRate);
	}
}
