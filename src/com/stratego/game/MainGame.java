package com.stratego.game;

import java.awt.Image;

import com.lutz.engine.game.ActionType;
import com.lutz.engine.game.GameAction;
import com.lutz.engine.resources.ImageResource;
import com.lutz.engine.resources.loading.GameResource;
import com.lutz.engine.resources.loading.ResourceType;
import com.lutz.engine.settings.loading.GameSetting;
import com.lutz.engine.settings.loading.SettingType;
import com.lutz.engine.ui.Screen;
import com.lutz.engine.ui.graphics.resolutions.AspectRatio;

public class MainGame {
	//TODO: make Setup Function and call for wells and board
	/**Currently not used beyond creation, work done through the piece stored by gameBoard. Possible to remove this array?*/
	public static Piece[] soldiers = new Piece[80];
	public static Tile[][] gameBoard = new Tile[10][10];
	
	@GameResource(ResourceType.GAME_ICON)
	public static Image icon = ImageResource.getExternalImage(
			"resources/images/icon.png").getImage();

	@GameSetting(SettingType.SUPPORTED_ASPECT_RATIOS)
	public static AspectRatio[] aspectRatios = new AspectRatio[] { new AspectRatio(
			16, 9) };

	@GameSetting(SettingType.WINDOW_TITLE)
	public static String windowTitle = "Stratego";

	@GameAction(ActionType.INITIALIZE)
	public static void initialize() {
		
		for (int x = 0; x < 10; x++){
			
			for (int y = 0; y < 10; y++){
				
				gameBoard[x][y] = new Tile(x,y);
			}
		}
	}

	@GameAction(ActionType.UI_PRESHOW)
	public static void uiPreShow() {
	}

	@GameAction(ActionType.UI_POSTSHOW)
	public static void uiPostShow() {
		
		Screen.startTick();
	}

	@GameAction(ActionType.CLOSE)
	public static void close() {
	}
}
