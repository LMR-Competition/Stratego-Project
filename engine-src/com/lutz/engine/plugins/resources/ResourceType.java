package com.lutz.engine.plugins.resources;

public enum ResourceType {

	PLUGIN_ID(String.class);

	private Class<?> required;

	private ResourceType(Class<?> c) {

		this.required = c;
	}

	public Class<?> getRequiredClass() {

		return required;
	}
}
