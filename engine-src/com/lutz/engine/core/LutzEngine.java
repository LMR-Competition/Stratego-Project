package com.lutz.engine.core;

import java.awt.Image;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import com.lutz.advlogging.LogChannel;
import com.lutz.advlogging.Logger;
import com.lutz.advlogging.Logger.Level;
import com.lutz.advlogging.Logger.Verbosity;
import com.lutz.advlogging.channels.ChannelFinalizer;
import com.lutz.advlogging.channels.DefaultChannel;
import com.lutz.advlogging.channels.FileChannel;
import com.lutz.advlogging.channels.FileListChannel;
import com.lutz.advlogging.channels.ListChannel;
import com.lutz.advlogging.channels.filters.IssueFilter;
import com.lutz.engine.core.game.GameInfo;
import com.lutz.engine.core.game.GameXML;
import com.lutz.engine.core.game.GameXMLHandler;
import com.lutz.engine.core.game.GameXMLReader;
import com.lutz.engine.data.DataTypes;
import com.lutz.engine.data.PersistentDataManager;
import com.lutz.engine.data.types.BooleanType;
import com.lutz.engine.data.types.ByteType;
import com.lutz.engine.data.types.CharType;
import com.lutz.engine.data.types.DoubleType;
import com.lutz.engine.data.types.FloatType;
import com.lutz.engine.data.types.IntegerType;
import com.lutz.engine.data.types.LongType;
import com.lutz.engine.data.types.ShortType;
import com.lutz.engine.data.types.StringType;
import com.lutz.engine.data.types.special.ResolutionType;
import com.lutz.engine.game.GameManager;
import com.lutz.engine.game.GameSettings;
import com.lutz.engine.logging.LoggerFactory;
import com.lutz.engine.logging.util.LoggingUtils;
import com.lutz.engine.plugins.PluginManager;
import com.lutz.engine.plugins.PluginWrapper;
import com.lutz.engine.ui.Screen;
import com.lutz.engine.ui.UISettings;
import com.lutz.engine.ui.graphics.resolutions.AspectRatio;
import com.lutz.engine.ui.graphics.resolutions.ScreenResolution;

public class LutzEngine {

	private static LogChannel stdOutChannel, logChannel;
	private static ListChannel errorChannel;

	private static Logger engineLogger = null;

	private static boolean running = false;

	private static GameXMLHandler xmlHandler;

	private static Thread mainThread = null;

	private static PluginWrapper[] plugins = {};

	public static void main(String[] args) {

		startMainSequence();
	}

	public static void startMainSequence() {

		startEngine();

		startMainThread();
	}

	public static void startEngine() {

		new File("logs").mkdirs();

		stdOutChannel = new DefaultChannel("std_out");

		LoggingUtils.moveOldLogs();

		try {

			logChannel = new FileChannel(LoggingUtils.getLogFile(),
					"engine-log");

		} catch (Exception e) {

			System.err.println("Logger unavailable!");

			e.printStackTrace();
		}

		errorChannel = new FileListChannel(LoggingUtils.getErrorFile(),
				"engine-errors");
		errorChannel.addFilter(new IssueFilter());

		engineLogger = LoggerFactory.createLogger("engine", stdOutChannel,
				logChannel, errorChannel);

		ScreenResolution.setup();

		DataTypes.registerDataType(new ShortType());
		DataTypes.registerDataType(new IntegerType());
		DataTypes.registerDataType(new LongType());
		DataTypes.registerDataType(new DoubleType());
		DataTypes.registerDataType(new FloatType());
		DataTypes.registerDataType(new StringType());
		DataTypes.registerDataType(new CharType());
		DataTypes.registerDataType(new BooleanType());
		DataTypes.registerDataType(new ByteType());
		DataTypes.registerDataType(new ResolutionType());

		getEngineLogger().log("Starting engine...");

		running = true;
	}

	public static boolean isRunning() {

		return running;
	}

	public static void startMainThread() {

		mainThread = new Thread(new Runnable() {

			@Override
			public void run() {

				onMainThreadStart();
			}
		});

		mainThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {

				getEngineLogger().logCrash(e);

				crashEngine();
			}
		});

		getEngineLogger().log("Starting main thread...");

		mainThread.start();
	}

	private static void onMainThreadStart() {

		// Setup the persistent data
		getEngineLogger().log("Loading persistent data...");

		PersistentDataManager.readData();

		// Setup settings
		GameSettings.setup();
		UISettings.setup();

		// Load game.xml
		getEngineLogger().log("Locating main game class...");

		GameXML xml = GameXMLReader.parseGameXML();

		GameManager.setup(xml);

		xmlHandler = new GameXMLHandler(xml);

		AspectRatio[] ratios = xmlHandler.getSupportedAspectRatios();

		if (ratios != null) {

			if (ratios.length > 0) {

				for (AspectRatio r : ratios) {

					AspectRatio.addSupportedAspectRatio(r);
				}

			} else {

				getEngineLogger().fatal(
						"There must be at least one supported aspect ratio!",
						Verbosity.MINIMAL);

				crashEngine();
			}

		} else {

			getEngineLogger()
					.fatal("Supported aspect ratios must be defined in a field in the main game class with the GameSetting annotation!");

			crashEngine();
		}

		plugins = xml.getPlugins();

		getEngineLogger().log("Initializing plugins...");

		xmlHandler.initializePlugins();

		getEngineLogger().log("Initializing game...");

		xmlHandler.initialize();

		getEngineLogger().log("Initializing screen...");

		Screen.setup();

		Image icon = xmlHandler.getGameIcon();

		Screen.setIcon(icon);

		String windowTitle = xmlHandler.getWindowTitle();

		if (windowTitle == null) {

			windowTitle = GameManager.getName();
		}

		Screen.setWindowTitle(windowTitle);

		xmlHandler.uiPreShow();

		Screen.setVisible(true);

		xmlHandler.uiPostShow();
	}

	public static GameInfo getGameInfo() {

		if (xmlHandler != null) {

			return new GameInfo(xmlHandler.getGameXML().getName(), xmlHandler
					.getGameXML().getId(),
					xmlHandler.getGameXML().getVersion(),
					xmlHandler.getGameIcon(), xmlHandler.getGameWebsite(),
					xmlHandler.getWindowTitle());
		}

		return new GameInfo("null", "null", "null", null, null, null);
	}

	public static void crashEngine() {

		getEngineLogger().fatal("The engine has stopped working correctly!",
				Verbosity.MINIMAL);

		closeEngine();
	}

	public static void closeEngine() {

		running = false;

		getEngineLogger().log("Closing engine...");

		ChannelFinalizer finalizer = new ChannelFinalizer() {

			@Override
			public String[] getFinalStrings() {

				List<String> lines = new ArrayList<String>();

				lines.add("=======[ END OF LOG ]=======");
				lines.add("");
				lines.add("LOGGING KEY:");
				lines.add("Engine messages: ["
						+ getEngineLogger().getName().toUpperCase() + "]");

				if (GameManager.getLogger() != null) {

					lines.add("Game messages: " + "["
							+ GameManager.getLogger().getName().toUpperCase()
							+ "]");
				}

				for (String id : PluginManager.getIds()) {

					PluginManager m = PluginManager.getPluginManager(id);

					if (m != null) {

						Logger l = m.getLogger();

						if (l != null) {

							lines.add("Messages for plugin '" + m.getId()
									+ "': [" + l.getName().toUpperCase() + "]");
						}
					}
				}

				return lines.toArray(new String[] {});
			}
		};

		if (xmlHandler != null) {

			getEngineLogger().log("Closing game...");

			xmlHandler.close();

			getEngineLogger().log("Unloading plugins...");

			xmlHandler.unloadPlugins();
		}

		getEngineLogger().log("Saving persistent data...");

		PersistentDataManager.writeData();

		if (errorChannel.getList().length > 0) {

			getEngineLogger().log("Writing error log...");

			try {

				errorChannel.printList(finalizer);

			} catch (Exception e) {

				getEngineLogger().log(
						"An error occurred while writing the error log file!",
						Level.WARN, Verbosity.MINIMAL);

				getEngineLogger().logException(e);
			}
		}

		getEngineLogger().log("Finalizing...");

		logChannel.finalizeChannel(finalizer);
		errorChannel.close();
		stdOutChannel.finalizeChannel(finalizer);

		System.exit(0);
	}

	public static Logger addChannels(Logger logger) {

		logger.addChannel(stdOutChannel);
		logger.addChannel(logChannel);
		logger.addChannel(errorChannel);

		return logger;
	}

	public static LogChannel[] getChannels() {

		return new LogChannel[] { stdOutChannel, logChannel, errorChannel };
	}

	public static Logger getEngineLogger() {

		return engineLogger;
	}

	public static PluginWrapper[] getPlugins() {

		return plugins;
	}
}
