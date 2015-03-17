package com.lutz.engine.plugins;

import java.util.HashMap;
import java.util.Map;

import com.lutz.advlogging.Logger;
import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.logging.LoggerFactory;

public class PluginManager {

	private static Map<String, PluginManager> managers = new HashMap<String, PluginManager>();

	private String id;
	private Logger logger;

	public PluginManager(String id, Logger logger) {

		this.id = id;
		this.logger = logger;
	}

	public String getId() {

		return id;
	}

	public Logger getLogger() {

		return logger;
	}

	public static PluginManager getPluginManager(String id) {

		if (managers.containsKey(id)) {

			return managers.get(id);

		} else {

			Logger logger = LoggerFactory.createLogger(id,
					LutzEngine.getChannels());

			PluginManager manager = new PluginManager(id, logger);

			managers.put(id, manager);

			return manager;
		}
	}
	
	public static String[] getIds(){

		return managers.keySet().toArray(new String[] {});
	}
}
