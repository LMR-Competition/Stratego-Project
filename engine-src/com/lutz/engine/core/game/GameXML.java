package com.lutz.engine.core.game;

import com.lutz.engine.plugins.PluginWrapper;

public class GameXML {

	private String id, name, version;

	private Class<?> gameClass;

	private PluginWrapper[] plugins;

	public GameXML(String id, String name, String version, Class<?> gameClass,
			PluginWrapper... plugins) {

		this.id = id;
		this.name = name;
		this.version = version;
		this.gameClass = gameClass;
		this.plugins = plugins;
	}

	public String getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	public String getVersion() {

		return version;
	}

	public Class<?> getGameClass() {

		return gameClass;
	}

	public PluginWrapper[] getPlugins() {

		return plugins;
	}
}
