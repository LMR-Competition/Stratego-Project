package com.lutz.engine.core.game;

import java.awt.Image;
import java.net.URL;

public class GameInfo {

	private String name, id, version, windowTitle;

	private Image icon;

	private URL website;

	public GameInfo(String name, String id, String version, Image icon,
			URL website, String windowTitle) {

		this.name = name;
		this.id = id;
		this.version = version;
		this.icon = icon;
		this.website = website;
		this.windowTitle = windowTitle;
	}

	public String getName() {

		return name;
	}

	public String getId() {

		return id;
	}

	public String getVersion() {

		return version;
	}

	public Image getIcon() {

		return icon;
	}

	public URL getWebsite() {

		return website;
	}

	public String getWindowTitle() {

		return windowTitle;
	}
}
