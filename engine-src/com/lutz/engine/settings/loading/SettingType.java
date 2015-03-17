package com.lutz.engine.settings.loading;

import com.lutz.engine.ui.graphics.resolutions.AspectRatio;

public enum SettingType {

	SUPPORTED_ASPECT_RATIOS(AspectRatio[].class), WINDOW_TITLE(String.class);

	private Class<?> requiredType;

	private SettingType(Class<?> requiredType) {

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
