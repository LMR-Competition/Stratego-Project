package com.lutz.engine.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.ui.graphics.Drawable;
import com.lutz.engine.ui.graphics.GraphicsEngine;
import com.lutz.engine.ui.graphics.resolutions.AspectRatio;
import com.lutz.engine.ui.graphics.resolutions.ScreenResolution;

public class Screen {

	private static ScreenFrame frame;

	private static Timer timer;

	private static boolean isFullscreen = false;

	private static GraphicsEnvironment env;

	private static GraphicsDevice dev;

	private static int fps = 0;

	public static void setup() {

		env = GraphicsEnvironment.getLocalGraphicsEnvironment();

		dev = env.getDefaultScreenDevice();

		frame = new ScreenFrame();

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				LutzEngine.closeEngine();
			}
		});

		frame.addMouseListener(com.stratego.game.ui.Screen.getMouseListener());
		frame.addMouseMotionListener(com.stratego.game.ui.Screen
				.getMouseMotionListener());
		frame.addKeyListener(com.stratego.game.ui.Screen.getKeyListener());

		figureSize();
	}

	public static void setVisible(boolean visible) {

		frame.setVisible(visible);
	}

	public static void startTick() {

		if (timer != null) {

			if (timer.isRunning()) {

				timer.stop();
			}
		}

		timer = new Timer(1000 / UISettings.getTargetFPS(),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						frame.canvas.revalidate();
						frame.canvas.repaint();
					}
				});

		timer.start();
	}

	public static void setIcon(Image icon) {

		if (icon != null) {

			frame.setIconImage(icon);
		}
	}

	public static void changeViewMode(boolean fullscreen) {

		if (fullscreen && !isFullscreen) {

			if (dev != null) {

				frame.dispose();

				frame.setUndecorated(true);

				frame.setResizable(false);

				if (dev.isFullScreenSupported()) {

					dev.setFullScreenWindow(frame);

					if (dev.isDisplayChangeSupported()) {

						dev.setDisplayMode(UISettings.getResolution()
								.getModeForResolution());
					}

				} else {

					frame.setUndecorated(false);

					frame.setResizable(true);

					frame.setVisible(true);
				}

				if (dev.isFullScreenSupported()) {

					dev.setFullScreenWindow(frame);

				} else {

					frame.setUndecorated(false);

					frame.setResizable(true);

					frame.setVisible(true);
				}
			}

		} else if (!fullscreen && isFullscreen) {

			if (dev != null) {

				frame.dispose();

				frame.setUndecorated(false);

				frame.setResizable(true);

				if (dev.isDisplayChangeSupported()) {

					dev.setDisplayMode(ScreenResolution.getDefaultResolution()
							.getModeForResolution());
				}

				if (dev.isFullScreenSupported()) {

					dev.setFullScreenWindow(null);
				}

				frame.setVisible(true);
			}
		}
	}

	public static void setWindowTitle(String windowTitle) {

		frame.setTitle(windowTitle);
	}

	private static void figureSize() {

		ScreenResolution res = UISettings.getResolution();

		frame.canvas.setPreferredSize(new Dimension(res.getWidth(), res
				.getHeight()));

		frame.pack();
	}

	public static void closeScreen() {

		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	public static int getFPS() {

		return fps;
	}

	@SuppressWarnings("serial")
	private static class ScreenFrame extends JFrame {

		private ScreenCanvas canvas;

		public ScreenFrame() {

			canvas = new ScreenCanvas();

			this.setContentPane(canvas);
		}

		private class ScreenCanvas extends JPanel {

			@Override
			protected void paintComponent(Graphics graphics) {

				graphics.setColor(Color.BLACK);

				graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

				GraphicsEngine engine = new GraphicsEngine(graphics,
						new Dimension(this.getWidth(), this.getHeight()),
						new AspectRatio(16, 9), this);

				Drawable d = new Drawable() {

					@Override
					public void draw(GraphicsEngine engine) {

						com.stratego.game.ui.Screen.draw(engine);
					}
				};

				engine.render(d);

				engine.drawPlugins();
			}
		}
	}
}