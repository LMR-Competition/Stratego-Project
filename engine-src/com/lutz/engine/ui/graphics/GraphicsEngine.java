package com.lutz.engine.ui.graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;

import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.plugins.PluginWrapper;
import com.lutz.engine.ui.UISettings;
import com.lutz.engine.ui.graphics.resolutions.AspectRatio;
import com.lutz.engine.util.ExtendedMap;

public class GraphicsEngine {

	private static Map<Dimension, Double> scaling = new HashMap<Dimension, Double>();

	private Graphics2D graphicsStandard;

	private AspectRatio ratio;

	private Dimension usableSpace, windowDim;

	private double scaleValue;

	private Graphics2D currentGraphics;

	private Drawable current;
	
	private ImageObserver obs;

	public GraphicsEngine(Graphics graphics, Dimension windowDim,
			AspectRatio ratio, ImageObserver obs) {

		this.graphicsStandard = (Graphics2D) graphics;
		this.ratio = ratio;
		this.windowDim = windowDim;
		this.obs = obs;

		usableSpace = ratio.getUsableArea(UISettings.getResolution());

		double scaleValue;

		if (scaling.containsKey(windowDim)) {

			scaleValue = scaling.get(windowDim);

		} else {

			scaleValue = getScaling(windowDim, usableSpace);

			scaling.put(windowDim, scaleValue);
		}

		this.scaleValue = scaleValue;

		graphicsStandard.translate(getTranslationX(), getTranslationY());

		graphicsStandard.scale(scaleValue, scaleValue);
	}
	
	public ImageObserver getImageObserver(){
		
		return obs;
	}

	private double getScaling(Dimension windowDim, Dimension usable) {

		double widthScale = (double) windowDim.width / usable.width;
		double heightScale = (double) windowDim.height / usable.height;

		if (widthScale > heightScale) {

			if (((double) usable.getHeight()) * widthScale <= windowDim.height) {

				return widthScale;

			} else {

				return heightScale;
			}

		} else {

			if (((double) usable.getWidth()) * heightScale <= windowDim
					.getWidth()) {

				return heightScale;

			} else {

				return widthScale;
			}
		}
	}
	
	public int scaleX(int x){
		
		return (int) ((x/scaleValue)-((7+getTranslationX())/scaleValue));
	}
	
	public int scaleY(int y){
		
		return (int) ((y/scaleValue)-((30+getTranslationY())/scaleValue));
	}

	public void render(Drawable drawable) {

		this.current = drawable;

		currentGraphics = getGraphicsCopy();

		drawable.draw(this);
	}

	public void drawPlugins() {

		for (PluginWrapper pw : LutzEngine.getPlugins()) {

			currentGraphics = getGraphicsCopy();

			pw.uiTopLayer(this);
		}
	}

	private Graphics2D getGraphicsCopy() {

		Graphics2D g = (Graphics2D) graphicsStandard.create();

		if (UISettings.getAntiAlias()) {

			g.addRenderingHints(new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON));
		}

		return g;
	}

	public Drawable getCurrentDrawing() {

		return current;
	}

	public AspectRatio getAspectRatio() {

		return ratio;
	}

	public Dimension getUsableSpace() {

		return usableSpace;
	}

	public Dimension getUnusableSpace() {

		Dimension usable = getUsableSpace();

		int width = (int) (windowDim.width - (usable.getWidth() * scaleValue));
		int height = (int) (windowDim.height - (usable.getHeight() * scaleValue));

		return new Dimension(width, height);
	}

	public Graphics2D getGraphics() {

		return currentGraphics;
	}

	public int getWidth() {

		return (int) getUsableSpace().getWidth();
	}

	public int getHeight() {

		return (int) getUsableSpace().getHeight();
	}

	public int percWidth(double perc) {

		return (int) (getWidth() * perc);
	}

	public int percHeight(double perc) {

		return (int) (getHeight() * perc);
	}

	private ExtendedMap<String, Double, Integer> cachedSizes = new ExtendedMap<String, Double, Integer>();

	public int percentageFontSize(Font font, double percentage) {

		if (cachedSizes.containsKey(font.getFontName(), percentage)) {

			return cachedSizes.get(font.getFontName(), percentage);

		} else {

			int height = percHeight(percentage);

			Font temp = font.deriveFont((float) height);

			int trueHeight = currentGraphics.getFontMetrics(temp).getHeight();

			if (trueHeight > height) {

				while (trueHeight > height) {

					height++;

					font.deriveFont((float) height);

					trueHeight = currentGraphics.getFontMetrics(temp)
							.getHeight();
				}

			} else if (trueHeight < height) {

				while (trueHeight < height) {

					height--;

					font.deriveFont((float) height);

					trueHeight = currentGraphics.getFontMetrics(temp)
							.getHeight();
				}
			}

			cachedSizes.put(font.getFontName(), percentage, height);

			return height;
		}
	}

	private int getTranslationX() {

		return (int) (getUnusableSpace().getWidth()) / 2;
	}

	private int getTranslationY() {

		return (int) (getUnusableSpace().getHeight()) / 2;
	}
}
