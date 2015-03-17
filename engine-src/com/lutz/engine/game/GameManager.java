package com.lutz.engine.game;

import com.lutz.advlogging.Logger;
import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.core.game.GameXML;
import com.lutz.engine.logging.LoggerFactory;

public class GameManager {

	private static GameXML xml;
	
	private static Logger logger;

	public static void setup(GameXML xml) {

		GameManager.xml = xml;
		
		logger = LoggerFactory.createLogger(xml.getId(),
				LutzEngine.getChannels());
	}

	public static Logger getLogger() {

		return logger;
	}
	
	public static String getId(){
		
		return xml.getId();
	}
	
	public static String getName(){
		
		return xml.getName();
	}
	
	public static String getVersion(){
		
		return xml.getVersion();
	}
}
