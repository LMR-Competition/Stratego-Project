package com.lutz.engine.core.game;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.lutz.advlogging.Logger.Verbosity;
import com.lutz.easyxml.XMLParser;
import com.lutz.easyxml.XMLTag;
import com.lutz.easyxml.XMLTree;
import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.plugins.PluginWrapper;

public class GameXMLReader {

	public static GameXML parseGameXML() {

		InputStream gameXMLStream = LutzEngine.class
				.getResourceAsStream("/game.xml");

		if (gameXMLStream != null) {

			XMLTree tree = new XMLParser().parseXML(gameXMLStream);

			XMLTag root = tree.getRoot();

			if (root.getName().equalsIgnoreCase("game")) {

				if (root.hasAttribute("id") && root.hasAttribute("name")
						&& root.hasAttribute("version")) {

					String id = root.getAttribute("id");
					String name = root.getAttribute("name");
					String version = root.getAttribute("version");

					if (root.hasChild("game-location")) {

						XMLTag gameLocation = root.getChild("game-location");

						String classLoc = gameLocation.getValue();

						if (classLoc != null) {

							try {

								Class<?> gameClass = Class.forName(classLoc);

								List<PluginWrapper> wrappers = new ArrayList<PluginWrapper>();

								if (root.hasChild("plugin")) {

									XMLTag[] pluginTags = root
											.getChildrenForName("plugin");

									for (XMLTag tag : pluginTags) {

										String className = tag.getValue();

										if (className != null) {

											try {

												Class<?> c = Class
														.forName(className);

												wrappers.add(new PluginWrapper(
														c));

											} catch (Exception e) {

												LutzEngine
														.getEngineLogger()
														.warn("The plugin at '"
																+ className
																+ "' could not be found!");
											}

										} else {

											LutzEngine
													.getEngineLogger()
													.warn("All <plugin> tags in game.xml must have a class name inside the them!");
										}
									}
								}

								GameXML game = new GameXML(id, name, version,
										gameClass, wrappers.toArray(new PluginWrapper[]{}));

								return game;

							} catch (Exception e) {

								LutzEngine.getEngineLogger().fatal(
										"There was an error loading the class at '"
												+ classLoc + "'!",
										Verbosity.MINIMAL);

								LutzEngine.getEngineLogger().logCrash(e);

								LutzEngine.crashEngine();
							}

						} else {

							LutzEngine
									.getEngineLogger()
									.fatal("The <game-location> tag within game.xml must have a value containing the location of the main game class file!",
											Verbosity.MINIMAL);

							LutzEngine.crashEngine();
						}

					} else {

						LutzEngine
								.getEngineLogger()
								.fatal("The <game> tag in game.xml must have a <game-location> child tag that contains the location of the main game class!",
										Verbosity.MINIMAL);

						LutzEngine.crashEngine();
					}

				} else {

					LutzEngine
							.getEngineLogger()
							.fatal("The <game> tag in the game.xml file must have attributes called 'id', 'name', and 'version'!",
									Verbosity.MINIMAL);

					LutzEngine.crashEngine();
				}

			} else {

				LutzEngine.getEngineLogger().fatal(
						"The root tag of a game.xml file must be <game>!",
						Verbosity.MINIMAL);

				LutzEngine.crashEngine();
			}

		} else {

			LutzEngine.getEngineLogger().fatal(
					"No game.xml was found inside the jar file!",
					Verbosity.MINIMAL);

			LutzEngine.crashEngine();
		}

		return null;
	}
}
