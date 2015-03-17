package com.lutz.engine.logging;

import java.util.HashMap;
import java.util.Map;

import com.lutz.advlogging.LogChannel;
import com.lutz.advlogging.Logger;

public class LoggerFactory {

	private static Map<String, Logger> loggers = new HashMap<String, Logger>();

	public static Logger createLogger(String name, LogChannel... channels) {

		Logger logger = new Logger(name, channels);
		
		loggers.put(name, logger);

		return logger;
	}

	public static Logger getLogger(String name) {

		if (loggers.containsKey(name)) {

			return loggers.get(name);

		} else {

			return null;
		}
	}
}
