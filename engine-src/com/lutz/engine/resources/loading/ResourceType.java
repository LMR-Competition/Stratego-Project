package com.lutz.engine.resources.loading;

import java.awt.Image;
import java.net.URL;

public enum ResourceType {

	GAME_ICON(Image.class), GAME_WEBSITE(URL.class);

	private Class<?> requiredType;

	private ResourceType(Class<?> requiredType) {

		this.requiredType = requiredType;
	}

	public Class<?> getRequiredType() {

		return requiredType;
	}

	public boolean matchesType(Object o) {

		try {

			requiredType.cast(o);

			return true;

		} catch (Exception e) {

			return false;
		}
	}
}
