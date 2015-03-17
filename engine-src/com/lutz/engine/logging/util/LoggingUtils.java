package com.lutz.engine.logging.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.lutz.engine.io.FileUtils;

public class LoggingUtils {

	private static final String LOG_FILE_NAME = "logs/engine-log",
			ERROR_LOG_FILE_NAME = "logs/engine-errors",
			PREVIOUS_ADD = ".previous";
	private static File logFile = null, errorFile = null;

	public static File getLogFile() {

		if (logFile == null) {

			logFile = new File(LOG_FILE_NAME + ".txt");
		}

		return logFile;
	}

	public static File getErrorFile() {

		if (errorFile == null) {

			errorFile = new File(ERROR_LOG_FILE_NAME + ".txt");
		}

		return errorFile;
	}

	public static void moveOldLogs() {

		File oldLog = new File(LOG_FILE_NAME + PREVIOUS_ADD + ".txt"), oldError = new File(
				ERROR_LOG_FILE_NAME + PREVIOUS_ADD + ".txt");
		File log = new File(LOG_FILE_NAME + ".txt"), error = new File(
				ERROR_LOG_FILE_NAME + ".txt");

		if (oldLog.exists()) {

			oldLog.delete();
		}

		if (oldError.exists()) {

			oldError.delete();
		}

		if (log.exists()) {

			try {

				Files.move(log.toPath(), oldLog.toPath(),
						StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {

				System.err
						.println("There was an error while copying an old log file!");

				e.printStackTrace();
			}
		}

		if (error.exists()) {

			try {

				Files.move(error.toPath(), oldError.toPath(),
						StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {

				System.err
						.println("There was an error while copying an old log file!");

				e.printStackTrace();
			}
		}
	}

	public static void clearLogs() {

		File logDir = new File("logs");

		for (File file : logDir.listFiles()) {

			FileUtils.delete(file);
		}
	}
}
