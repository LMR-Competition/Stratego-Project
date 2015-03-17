package com.lutz.engine.core.game;

import java.awt.Desktop;
import java.awt.Image;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

import com.lutz.advlogging.Logger.Verbosity;
import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.game.ActionType;
import com.lutz.engine.game.GameAction;
import com.lutz.engine.plugins.PluginWrapper;
import com.lutz.engine.resources.loading.GameResource;
import com.lutz.engine.resources.loading.ResourceType;
import com.lutz.engine.settings.loading.GameSetting;
import com.lutz.engine.settings.loading.SettingType;
import com.lutz.engine.ui.graphics.resolutions.AspectRatio;

public class GameXMLHandler {

	private GameXML xml;

	private Image icon = null;
	private URL website = null;
	private AspectRatio[] supportedAspectRatios = null;
	private String windowTitle = null;

	public GameXMLHandler(GameXML xml) {

		this.xml = xml;
	}

	public GameXML getGameXML() {

		return xml;
	}

	public void initializePlugins() {

		for (PluginWrapper p : xml.getPlugins()) {

			p.init();
		}
	}

	public void unloadPlugins() {

		for (PluginWrapper p : xml.getPlugins()) {

			p.unload();
		}
	}

	public void initialize() {

		Class<?> gameClass = xml.getGameClass();

		for (Method m : gameClass.getMethods()) {

			if (m.isAnnotationPresent(GameAction.class)) {

				GameAction action = m.getAnnotation(GameAction.class);

				if (action.value() == ActionType.INITIALIZE) {

					if (Modifier.isStatic(m.getModifiers())
							&& Modifier.isPublic(m.getModifiers())
							&& m.getReturnType() == Void.TYPE
							&& m.getParameterTypes().length == 0) {

						try {

							m.invoke(gameClass.newInstance());

						} catch (InvocationTargetException e) {

							LutzEngine
									.getEngineLogger()
									.fatal("An error occurred while invoking the 'initialize' action method in the main game class!",
											Verbosity.MINIMAL);

							LutzEngine.getEngineLogger().logCrash(e.getCause());

							LutzEngine.crashEngine();

						} catch (Exception e) {

							LutzEngine
									.getEngineLogger()
									.fatal("An error occurred while invoking the 'initialize' action method in the main game class!",
											Verbosity.MINIMAL);

							LutzEngine.getEngineLogger().logCrash(e);

							LutzEngine.crashEngine();
						}

					} else {

						LutzEngine
								.getEngineLogger()
								.fatal("The 'initialize' action method of the main game class should be public, static, return void, and take no parameters!",
										Verbosity.MINIMAL);

						LutzEngine.crashEngine();
					}
				}
			}
		}
	}

	public void uiPreShow() {

		Class<?> gameClass = xml.getGameClass();

		for (Method m : gameClass.getMethods()) {

			if (m.isAnnotationPresent(GameAction.class)) {

				GameAction action = m.getAnnotation(GameAction.class);

				if (action.value() == ActionType.UI_PRESHOW) {

					if (Modifier.isStatic(m.getModifiers())
							&& Modifier.isPublic(m.getModifiers())
							&& m.getReturnType() == Void.TYPE
							&& m.getParameterTypes().length == 0) {

						try {

							m.invoke(gameClass.newInstance());

						} catch (InvocationTargetException e) {

							LutzEngine
									.getEngineLogger()
									.fatal("An error occurred while invoking the 'ui_preshow' action method in the main game class!",
											Verbosity.MINIMAL);

							LutzEngine.getEngineLogger().logCrash(e.getCause());

							LutzEngine.crashEngine();

						} catch (Exception e) {

							LutzEngine
									.getEngineLogger()
									.fatal("An error occurred while invoking the 'ui_preshow' action method in the main game class!",
											Verbosity.MINIMAL);

							LutzEngine.getEngineLogger().logCrash(e);

							LutzEngine.crashEngine();
						}

					} else {

						LutzEngine
								.getEngineLogger()
								.fatal("The 'ui_preshow' action method of the main game class should be public, static, return void, and take no parameters!",
										Verbosity.MINIMAL);

						LutzEngine.crashEngine();
					}
				}
			}
		}
	}

	public void uiPostShow() {

		Class<?> gameClass = xml.getGameClass();

		for (Method m : gameClass.getMethods()) {

			if (m.isAnnotationPresent(GameAction.class)) {

				GameAction action = m.getAnnotation(GameAction.class);

				if (action.value() == ActionType.UI_POSTSHOW) {

					if (Modifier.isStatic(m.getModifiers())
							&& Modifier.isPublic(m.getModifiers())
							&& m.getReturnType() == Void.TYPE
							&& m.getParameterTypes().length == 0) {

						try {

							m.invoke(gameClass.newInstance());

						} catch (InvocationTargetException e) {

							LutzEngine
									.getEngineLogger()
									.fatal("An error occurred while invoking the 'ui_postshow' action method in the main game class!",
											Verbosity.MINIMAL);

							LutzEngine.getEngineLogger().logCrash(e.getCause());

							LutzEngine.crashEngine();

						} catch (Exception e) {

							LutzEngine
									.getEngineLogger()
									.fatal("An error occurred while invoking the 'ui_postshow' action method in the main game class!",
											Verbosity.MINIMAL);

							LutzEngine.getEngineLogger().logCrash(e);

							LutzEngine.crashEngine();
						}

					} else {

						LutzEngine
								.getEngineLogger()
								.fatal("The 'ui_postshow' action method of the main game class should be public, static, return void, and take no parameters!",
										Verbosity.MINIMAL);

						LutzEngine.crashEngine();
					}
				}
			}
		}
	}

	public void close() {

		Class<?> gameClass = xml.getGameClass();

		for (Method m : gameClass.getMethods()) {

			if (m.isAnnotationPresent(GameAction.class)) {

				GameAction action = m.getAnnotation(GameAction.class);

				if (action.value() == ActionType.CLOSE) {

					if (Modifier.isStatic(m.getModifiers())
							&& Modifier.isPublic(m.getModifiers())
							&& m.getReturnType() == Void.TYPE
							&& m.getParameterTypes().length == 0) {

						try {

							m.invoke(gameClass.newInstance());

						} catch (InvocationTargetException e) {

							LutzEngine
									.getEngineLogger()
									.fatal("An error occurred while invoking the 'close' action method in the main game class!",
											Verbosity.MINIMAL);

							LutzEngine.getEngineLogger().logCrash(e.getCause());

						} catch (Exception e) {

							LutzEngine
									.getEngineLogger()
									.fatal("An error occurred while invoking the 'close' action method in the main game class!",
											Verbosity.MINIMAL);

							LutzEngine.getEngineLogger().logCrash(e);
						}

					} else {

						LutzEngine
								.getEngineLogger()
								.fatal("The 'close' action method of the main game class should be public, static, return void, and take no parameters!",
										Verbosity.MINIMAL);
					}
				}
			}
		}
	}

	public Image getGameIcon() {

		if (icon == null) {

			Class<?> gameClass = xml.getGameClass();

			for (Field f : gameClass.getFields()) {

				if (f.isAnnotationPresent(GameResource.class)) {

					GameResource resource = f.getAnnotation(GameResource.class);

					if (resource.value() == ResourceType.GAME_ICON) {

						if (Modifier.isStatic(f.getModifiers())
								&& Modifier.isPublic(f.getModifiers())) {

							try {

								Object o = f.get(gameClass.newInstance());

								if (resource.value().matchesType(o)) {

									icon = (Image) o;
								}

							} catch (Exception e) {

								LutzEngine
										.getEngineLogger()
										.fatal("An error occurred while loading the game icon from the main game class!",
												Verbosity.MINIMAL);

								LutzEngine.getEngineLogger().logCrash(e);
							}

						} else {

							LutzEngine
									.getEngineLogger()
									.fatal("The 'game icon' field of the main game class should be public, static, and be of type "
											+ resource.value()
													.getRequiredType()
													.getSimpleName() + "!",
											Verbosity.MINIMAL);
						}
					}
				}
			}
		}

		return icon;
	}

	public URL getGameWebsite() {

		if (website == null) {

			Class<?> gameClass = xml.getGameClass();

			for (Field f : gameClass.getFields()) {

				if (f.isAnnotationPresent(GameResource.class)) {

					GameResource resource = f.getAnnotation(GameResource.class);

					if (resource.value() == ResourceType.GAME_WEBSITE) {

						if (Modifier.isStatic(f.getModifiers())
								&& Modifier.isPublic(f.getModifiers())) {

							try {

								Object o = f.get(gameClass.newInstance());

								if (resource.value().matchesType(o)) {

									website = (URL) o;
								}

							} catch (Exception e) {

								LutzEngine
										.getEngineLogger()
										.fatal("An error occurred while loading the game website URL from the main game class!",
												Verbosity.MINIMAL);

								LutzEngine.getEngineLogger().logCrash(e);
							}

						} else {

							LutzEngine
									.getEngineLogger()
									.fatal("The 'game website' field of the main game class should be public, static, and be of type "
											+ resource.value()
													.getRequiredType()
													.getSimpleName() + "!",
											Verbosity.MINIMAL);
						}
					}
				}
			}
		}

		return website;
	}

	public void openGameWebsite() {

		URL url = getGameWebsite();

		if (url != null) {

			if (Desktop.isDesktopSupported()) {

				try {

					Desktop.getDesktop().browse(url.toURI());

				} catch (Exception e) {

					LutzEngine
							.getEngineLogger()
							.warn("An error occurred while opening the game's website!");

					LutzEngine.getEngineLogger().logException(e);
				}

			} else {

				LutzEngine
						.getEngineLogger()
						.warn("The Desktop library is not supported on this computer!  The game URL could not be opened!");
			}
		}
	}

	public AspectRatio[] getSupportedAspectRatios() {

		if (supportedAspectRatios == null) {

			Class<?> gameClass = xml.getGameClass();

			for (Field f : gameClass.getFields()) {

				if (f.isAnnotationPresent(GameSetting.class)) {

					GameSetting setting = f.getAnnotation(GameSetting.class);

					if (setting.value() == SettingType.SUPPORTED_ASPECT_RATIOS) {

						if (Modifier.isStatic(f.getModifiers())
								&& Modifier.isPublic(f.getModifiers())) {

							try {

								Object o = f.get(gameClass.newInstance());

								if (setting.value().matchesType(o)) {

									supportedAspectRatios = (AspectRatio[]) o;
								}

							} catch (Exception e) {

								LutzEngine
										.getEngineLogger()
										.fatal("An error occurred while loading the supported aspect ratios from the main game class!",
												Verbosity.MINIMAL);

								LutzEngine.getEngineLogger().logCrash(e);
							}

						} else {

							LutzEngine
									.getEngineLogger()
									.fatal("The 'supported aspect ratios' field of the main game class should be public, static, and be of type "
											+ setting.value().getRequiredType()
													.getSimpleName() + "!",
											Verbosity.MINIMAL);
						}
					}
				}
			}
		}

		return supportedAspectRatios;
	}

	public String getWindowTitle() {

		if (windowTitle == null) {

			Class<?> gameClass = xml.getGameClass();

			for (Field f : gameClass.getFields()) {

				if (f.isAnnotationPresent(GameSetting.class)) {

					GameSetting setting = f.getAnnotation(GameSetting.class);

					if (setting.value() == SettingType.WINDOW_TITLE) {

						if (Modifier.isStatic(f.getModifiers())
								&& Modifier.isPublic(f.getModifiers())) {

							try {

								Object o = f.get(gameClass.newInstance());

								if (setting.value().matchesType(o)) {

									windowTitle = (String) o;
								}

							} catch (Exception e) {

								return null;
							}

						} else {

							return null;
						}
					}
				}
			}
		}

		return windowTitle;
	}
}
