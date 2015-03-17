package com.lutz.engine.plugins;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.plugins.resources.PluginResource;
import com.lutz.engine.plugins.resources.ResourceType;
import com.lutz.engine.ui.graphics.GraphicsEngine;

public class PluginWrapper {

	private Class<?> pluginClass;
	private String pluginId;
	private PluginManager manager;

	public PluginWrapper(Class<?> pluginClass) {

		this.pluginClass = pluginClass;
		this.pluginId = getPluginIdFromClass();
		this.manager = PluginManager.getPluginManager(pluginId);
	}

	public String getPluginId() {

		return pluginId;
	}

	public PluginManager getPluginManager() {

		return manager;
	}

	private String getPluginIdFromClass() {

		for (Field f : pluginClass.getFields()) {

			if (f.isAnnotationPresent(PluginResource.class)) {

				PluginResource resource = f.getAnnotation(PluginResource.class);

				if (resource.value() == ResourceType.PLUGIN_ID) {

					if (Modifier.isPublic(f.getModifiers())
							&& Modifier.isStatic(f.getModifiers())
							&& Modifier.isFinal(f.getModifiers())
							&& f.getType() == resource.value()
									.getRequiredClass()) {

						try {

							Object o = f.get(null);

							return (String) o;

						} catch (Exception e) {

							LutzEngine
									.getEngineLogger()
									.warn("An error occurred while reading a plugin id!");
							LutzEngine.getEngineLogger().logException(e);
						}

					} else {

						LutzEngine.getEngineLogger().warn(
								"The plugin id field must be public, static, final, and of type "
										+ resource.value().getRequiredClass()
												.getSimpleName());
					}
				}
			}
		}

		return pluginClass.getSimpleName();
	}

	public void init() {

		for (Method m : pluginClass.getMethods()) {

			if (m.isAnnotationPresent(PluginAction.class)) {

				PluginAction action = m.getAnnotation(PluginAction.class);

				if (action.value() == ActionType.PLUGIN_INIT) {

					if (Modifier.isPublic(m.getModifiers())
							&& Modifier.isStatic(m.getModifiers())
							&& m.getReturnType() == Void.TYPE
							&& m.getParameterTypes().length == 1) {

						if (m.getParameterTypes()[0] == PluginManager.class) {

							try {

								m.invoke(null, manager);

							} catch (Exception e) {

								LutzEngine
										.getEngineLogger()
										.warn("An error occurred while invoking the initialization method in plugin with id '"
												+ pluginId + "'!");
							}

						} else {

							LutzEngine
									.getEngineLogger()
									.warn("The initialization method in a plugin must take a PluginManager as an argument!");
						}

					} else {

						LutzEngine
								.getEngineLogger()
								.warn("The initialization method in a plugin must be public and static, return void, and take a PluginManager as an argument!");
					}
				}
			}
		}
	}

	private boolean failedUITopLayer = false;

	public void uiTopLayer(GraphicsEngine engine) {

		if (!failedUITopLayer) {

			for (Method m : pluginClass.getMethods()) {

				if (m.isAnnotationPresent(PluginAction.class)) {

					PluginAction action = m.getAnnotation(PluginAction.class);

					if (action.value() == ActionType.UI_TOPLAYER) {

						if (Modifier.isPublic(m.getModifiers())
								&& Modifier.isStatic(m.getModifiers())
								&& m.getReturnType() == Void.TYPE
								&& m.getParameterTypes().length == 2) {

							if (m.getParameterTypes()[0] == PluginManager.class
									&& m.getParameterTypes()[1] == GraphicsEngine.class) {

								try {

									m.invoke(null, manager, engine);

								} catch (Exception e) {

									failedUITopLayer = true;

									LutzEngine.getEngineLogger().warn(
											"An error occurred while invoking the top layer ui method in plugin with id '"
													+ pluginId + "'!");
								}

							} else {

								failedUITopLayer = true;

								LutzEngine
										.getEngineLogger()
										.warn("The top layer ui method in a plugin must take a PluginManager and a GraphicsEngine object as arguments!");
							}

						} else {

							failedUITopLayer = true;

							LutzEngine
									.getEngineLogger()
									.warn("The top layer ui method in a plugin must be public and static, return void, and take a PluginManager and a GraphicsEngine object as arguments!");
						}
					}
				}
			}
		}
	}

	public void unload() {

		for (Method m : pluginClass.getMethods()) {

			if (m.isAnnotationPresent(PluginAction.class)) {

				PluginAction action = m.getAnnotation(PluginAction.class);

				if (action.value() == ActionType.PLUGIN_UNLOAD) {

					if (Modifier.isPublic(m.getModifiers())
							&& Modifier.isStatic(m.getModifiers())
							&& m.getReturnType() == Void.TYPE
							&& m.getParameterTypes().length == 1) {

						if (m.getParameterTypes()[0] == PluginManager.class) {

							try {

								m.invoke(null, manager);

							} catch (Exception e) {

								LutzEngine.getEngineLogger().warn(
										"An error occurred while invoking the unload method in plugin with id '"
												+ pluginId + "'!");
							}

						} else {

							LutzEngine
									.getEngineLogger()
									.warn("The unload method in a plugin must take a PluginManager as an argument!");
						}

					} else {

						LutzEngine
								.getEngineLogger()
								.warn("The unload method in a plugin must be public and static, return void, and take a PluginManager as an argument!");
					}
				}
			}
		}
	}
}
