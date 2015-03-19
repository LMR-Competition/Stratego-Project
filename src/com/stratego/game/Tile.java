package com.stratego.game;

public class Tile {
	
	public boolean moveable;
	public Piece piece = null;
	
	public Tile(int x,int y){
		
		if ((y == 4 || y == 5) && (x == 2 || x == 3 || x == 6 || x == 7)){
			
			moveable = false;
			
		} else {
			
			moveable = true;
		}
	}
}
