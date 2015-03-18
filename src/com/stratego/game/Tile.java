package com.stratego.game;

public class Tile {
	
	public static boolean moveable;
	public static Piece piece = null;
	
	public Tile(int x,int y){
		
		if ((y==4&&(x==2||x==3|| x==6||x==7))||(y==5&&(x==2||x==3|| x==6||x==7))){
			
			moveable = false;
			
		} else {
			
			moveable = true;
		}
	}
}
