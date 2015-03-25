//Needed TODO: ADD no piece can move end to game.
//Wanted TODO: Add moving pieces out of way(fill method).  Fix shouldMoveHEre for off board and not one conditions.
//Can skip and maybe not notice TODO/do elsewhere: Add error handling if no way found to target within 100 moves, pick a new piece/targ? ignore and make random move?
package com.stratego.game.ai;

import java.util.Arrays;
import java.util.Random;

import com.stratego.game.MainGame;
import com.stratego.game.Movement;
import com.stratego.game.PieceData;
import com.stratego.game.Tile;

public class MainAI{
	private static int aiSide = 0;
	public static void aiTurn(){
		Tile home = null;
		Tile target = null;
	    home = aiFindStrongest();
		target = aiFindNearest(home);
		aiReachTarget(home, target);
    //weighting?
	}
	public static Tile aiFindStrongest(){
		Tile highest = null;
	    for (int x = 0; x<10; x++){
	        for (int y = 0; y<10;y++){
	          if (MainGame.gameBoard[x][y].piece.soldierSide == aiSide){
	            if (highest==null){
	            	highest = MainGame.gameBoard[x][y];
	            } else if (MainGame.gameBoard[x][y].piece.soldierRank < highest.piece.soldierRank){
	            	highest = MainGame.gameBoard[x][y];
	            }
	          }
	        }
	    }
	    return highest;
	}
	public static Tile aiFindNearest(Tile home){
		Tile closestTile = null;
    	for (int x = 0; x<10; x++){
    	for (int y = 0; y<10;y++){
    	  if (MainGame.gameBoard[x][y].piece.soldierSide != aiSide){      
        		if (closestTile == null){
        			closestTile = MainGame.gameBoard[x][y];//finish checking for adjacent spy
        		} else if (Math.abs(MainGame.gameBoard[x][y].piece.x-home.piece.x)+ Math.abs(MainGame.gameBoard[x][y].piece.y-home.piece.y)<Math.abs(closestTile.piece.x - home.piece.x)+ Math.abs(closestTile.piece.y - home.piece.y)){
        			closestTile = MainGame.gameBoard[x][y];
        		}
        	}
    	}
    	}
    //Calc closest piece
    return closestTile;
	}
	public static boolean shouldMoveHere(Tile here){//add something for own pieces there, fix off board
		if ((MainGame.gameBoard[here.piece.x][here.piece.y].piece.soldierRank !=11 && MainGame.gameBoard[here.piece.x-1][here.piece.y].piece.soldierRank != 10&&MainGame.gameBoard[here.piece.x+1][here.piece.y].piece.soldierRank != 10&&MainGame.gameBoard[here.piece.x][here.piece.y-1].piece.soldierRank != 10&&MainGame.gameBoard[here.piece.x][here.piece.y+1].piece.soldierRank != 10)&&MainGame.gameBoard[here.piece.x][here.piece.y].moveable == true){
			return true;
		}else{
			return false;
		}
	}
	public static void aiFreeSpaceFor(Tile toFree){
		//free in cardinal direction first
		//ask if piece in way, next in cardinal direction can move, if so move in possible direction, towards enemy if poss, else continue in cardinal directions
		// if none of above do random?
	}
	public static char aiCanMove(Tile mover){
		if (mover.piece.soldierRank >10){
			return 'x';
		}
		else if (MainGame.gameBoard[mover.piece.x][mover.piece.y+1].piece.soldierSide != aiSide&&MainGame.gameBoard[mover.piece.x][mover.piece.y+1].moveable == true){
			return 's';
		}
		else if (MainGame.gameBoard[mover.piece.x+1][mover.piece.y].piece.soldierSide != aiSide&&MainGame.gameBoard[mover.piece.x+1][mover.piece.y].moveable == true){
			return 'e';
		}
		else if (MainGame.gameBoard[mover.piece.x-1][mover.piece.y].piece.soldierSide != aiSide&&MainGame.gameBoard[mover.piece.x-1][mover.piece.y].moveable == true){
			return 'w';
		}
		else if (MainGame.gameBoard[mover.piece.x][mover.piece.y-1].piece.soldierSide != aiSide&&MainGame.gameBoard[mover.piece.x][mover.piece.y-1].moveable == true){
			return 'n';
		} else{
			return 'x';
		}
	}
	public static void aiReachTarget(Tile start, Tile finish){
		int goalX = finish.piece.x;
		int goalY = finish.piece.y;
		int currentX = start.piece.x;
		int currentY = start.piece.y;
		int moveToX;
		int moveToY;
		int chosenPath = 0;
		Pathway[] paths = new Pathway[100];
		for (int k = 0; k<100; k++){
			Arrays.fill(paths[k].xPath, -1);
			Arrays.fill(paths[k].yPath, -1);
		}
		paths[0].used = true;
		while (currentX != goalX && currentY != goalY){
			int usedPathways = 0;
			//collection of null checks on pathways
			for (int i = 1; i<100; i++){
				if (paths[i].used == true){
					usedPathways++;
				}
			}
			int newPaths = 0;
			for (int j = 0; j<=usedPathways; j++){
				if (paths[j].xPath[95] != -1){
					//call free space method
				}
				for(int loop = 0; loop <= 100; loop++){
					if (paths[j].xPath[loop] ==-1){
						currentX = paths[j].xPath[loop-1];
						currentY = paths[j].yPath[loop-1];
						break;//only breaking out of one?
					}
				}
				if (currentX<goalX){
					if (shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						currentX++;
					}else if (currentY<goalY){
						if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
							currentY++;
						} /*both need increasing, can increase neither*/else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true && shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
							for(int loop1 = 0; loop1 <= 100; loop1++){
								paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
								paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
								if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
									paths[usedPathways+newPaths].xPath[loop1] =currentX;
									paths[usedPathways+newPaths].yPath[loop1] =currentY-1;
									break;//only breaking out of one?
								}
							}
							currentX--;
						} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true){
							currentY--;
						} else if (shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
							currentX--;
						} else{//free up space for the highest piece to move.
							aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
						}
					} if (currentY>goalY){
						if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true){
							currentY--;
						} /*both need increasing, can increase neither*/else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true && shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
							for(int loop1 = 0; loop1 <= 100; loop1++){
								paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
								paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
								if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
									paths[usedPathways+newPaths].xPath[loop1] =currentX;
									paths[usedPathways+newPaths].yPath[loop1] =currentY+1;
									break;//only breaking out of one?
								}
							}
							currentX--;
						} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
							currentY++;
						} else if (shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
							currentX--;
						} else{//free up space for the highest piece to move.
							aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
						}
					} else if (currentY == goalY){
						if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true && shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
							for(int loop1 = 0; loop1 <= 100; loop1++){
								paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
								paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
								if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
									paths[usedPathways+newPaths].xPath[loop1] =currentX;
									paths[usedPathways+newPaths].yPath[loop1] =currentY-1;
									break;//only breaking out of one?
								}
							}
							currentY++;
						} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true){
							currentY--;
						} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
							currentY++;
						} else if ((shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true)){
							currentX--;
						}else{//free up space for the highest piece to move.
							aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
						}
					}
				}
// BREAK BREAK BREAK BREAK BREAK BREAK				
				else if (currentX<goalX){
					if (shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
					currentX--;
				}else if (currentY<goalY){
					if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
						currentY++;
					} /*both need increasing, can increase neither*/else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY-1;
								break;//only breaking out of one?
							}
						}
						currentX++;
					} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true){
						currentY--;
					} else if (shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						currentX++;
					} else{//free up space for the highest piece to move.
						aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
					}
				} if (currentY>goalY){
					if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true){
						currentY--;
					} /*both need increasing, can increase neither*/else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY+1;
								break;//only breaking out of one?
							}
						}
						currentX++;
					} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
						currentY++;
					} else if (shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						currentX++;
					} else{//free up space for the highest piece to move.
						aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
					}
				} else if (currentY == goalY){
					if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true && shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY-1;
								break;//only breaking out of one?
							}
						}
						currentY++;
					} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true){
						currentY--;
					} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
						currentY++;
					} else if ((shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true)){
						currentX++;
					}else{//free up space for the highest piece to move.
						aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
					}
				}
//BREAK BREAK BREAK BREAK BREAK BREAK BREAK BREAK
			}else if (currentX==goalX){
				if (currentY<goalY){
				if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
					currentY++;
				} /*both need increasing, can increase neither*/else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true &&shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
					for(int loop1 = 0; loop1 <= 100; loop1++){
						paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
						paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
						if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
							paths[usedPathways+newPaths].xPath[loop1] =currentX;
							paths[usedPathways+newPaths].yPath[loop1] =currentY-1;
							break;//only breaking out of one?
						}
					}
					for(int loop1 = 0; loop1 <= 100; loop1++){
						paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
						paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
						if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
							paths[usedPathways+newPaths].xPath[loop1] =currentX-1;
							paths[usedPathways+newPaths].yPath[loop1] =currentY;
							break;//only breaking out of one?
						}
					}
					currentX++;
				}else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY-1;
								break;//only breaking out of one?
							}
						}
						currentX++;
					}else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true && shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY-1;
								break;//only breaking out of one?
							}
						}
						currentX--;
					}else if (shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX-1;
								paths[usedPathways+newPaths].yPath[loop1] =currentY;
								break;//only breaking out of one?
							}
						}
						currentX++;
					}else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true){
						currentY--;
					} else if ((shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true)){
						currentX++;
					}else if ((shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true)){
						currentX--;
					}else{//free up space for the highest piece to move.
						aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
					}
				
				
				
				
				
			} if (currentY>goalY){
				if (shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true){
					currentY--;
				} /*both need increasing, can increase neither*/else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true &&shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
					for(int loop1 = 0; loop1 <= 100; loop1++){
						paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
						paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
						if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
							paths[usedPathways+newPaths].xPath[loop1] =currentX;
							paths[usedPathways+newPaths].yPath[loop1] =currentY+1;
							break;//only breaking out of one?
						}
					}
					for(int loop1 = 0; loop1 <= 100; loop1++){
						paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
						paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
						if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
							paths[usedPathways+newPaths].xPath[loop1] =currentX-1;
							paths[usedPathways+newPaths].yPath[loop1] =currentY;
							break;//only breaking out of one?
						}
					}
					currentX++;
				}else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY+1;
								break;//only breaking out of one?
							}
						}
						currentX++;
					}else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true && shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY+1;
								break;//only breaking out of one?
							}
						}
						currentX--;
					}else if (shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX-1;
								paths[usedPathways+newPaths].yPath[loop1] =currentY;
								break;//only breaking out of one?
							}
						}
						currentX++;
					} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
						currentY++;
					} else if ((shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true)){
						currentX++;
					}else if ((shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true)){
						currentX--;
					}else{//free up space for the highest piece to move.
						aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
					}
				
				
				
				
				
			} else if (currentY == goalY){
				if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true && shouldMoveHere(MainGame.gameBoard[currentX][currentY-1])==true &&shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true &&shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
					for(int loop1 = 0; loop1 <= 100; loop1++){
						paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
						paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
						if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
							paths[usedPathways+newPaths].xPath[loop1] =currentX;
							paths[usedPathways+newPaths].yPath[loop1] =currentY+1;
							break;//only breaking out of one?
						}
					}
					for(int loop1 = 0; loop1 <= 100; loop1++){
						paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
						paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
						if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
							paths[usedPathways+newPaths].xPath[loop1] =currentX;
							paths[usedPathways+newPaths].yPath[loop1] =currentY-1;
							break;//only breaking out of one?
						}
					}
					for(int loop1 = 0; loop1 <= 100; loop1++){
						paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
						paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
						if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
							paths[usedPathways+newPaths].xPath[loop1] =currentX-1;
							paths[usedPathways+newPaths].yPath[loop1] =currentY;
							break;//only breaking out of one?
						}
					}
					currentX++;
					
					
//STOPPED HERE STOPPED HERE					
				}else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY+1;
								break;//only breaking out of one?
							}
						}
						currentX++;
					}else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true && shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX;
								paths[usedPathways+newPaths].yPath[loop1] =currentY+1;
								break;//only breaking out of one?
							}
						}
						currentX--;
					}else if (shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true && shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true){
						for(int loop1 = 0; loop1 <= 100; loop1++){
							paths[usedPathways+newPaths].xPath[loop1] = paths[j].xPath[loop1];
							paths[usedPathways+newPaths].yPath[loop1] = paths[j].yPath[loop1];
							if (paths[usedPathways+newPaths].xPath[loop1] ==-1){
								paths[usedPathways+newPaths].xPath[loop1] =currentX-1;
								paths[usedPathways+newPaths].yPath[loop1] =currentY;
								break;//only breaking out of one?
							}
						}
						currentX++;
					} else if (shouldMoveHere(MainGame.gameBoard[currentX][currentY+1])==true){
						currentY++;
					} else if ((shouldMoveHere(MainGame.gameBoard[currentX+1][currentY])==true)){
						currentX++;
					}else if ((shouldMoveHere(MainGame.gameBoard[currentX-1][currentY])==true)){
						currentX--;
					}else{//free up space for the highest piece to move.
						aiFreeSpaceFor(start);//SAVE FOR LAST, hope never gets here
					}
				chosenPath = j;
			}
		}
		moveToX = paths[chosenPath].xPath[0];
		moveToY = paths[chosenPath].yPath[0];
	}
	public static void setPlayerBoard() {
		int[] ranks = { 12, 11, 11, 11, 8, 5, 7, 4, 3, 4, 6, 6, 7, 5, 5, 8, 3,
				8, 6, 7, 11, 9, 5, 7, 8, 6, 4, 9, 9, 8, 10, 9, 9, 9, 1, 2, 9, 9,
				11, 11 };
		boolean[] filled = new boolean[40];
		for (int row = 9; row >= 6; row--) {
			for (int col = 0; col < 10; col++) {

				if (col == 0) {
					Arrays.fill(filled, false);
				}
				boolean thisFilled = false;
				while (thisFilled == false) {
					int toFill = new Random().nextInt(10) + (9 - row) * 10;
					if (filled[toFill] == false) {
						Movement.preparePiece(col, row, ranks[toFill], 1);
						PieceData.removePieceFromWell(1, ranks[toFill]);
						filled[toFill] = true;
						thisFilled = true;
					}
				}
			}
		}
	}

	public static void setBoard() {
		int frontRowY = 3;
		int[] frontRank = { 1, 2, 9, 9, 11, 11, 11, 5, 7, 4 };
		int[] backRanks = { 7, 9, 9, 9, 9, 3, 4, 8, 4, 5, 5, 5, 6, 6, 6, 8, 6,
				10, 7, 7, 8, 8, 8, 3, 9, 11, 9 };//
		// frontrow
		boolean[] filled = new boolean[10];
		for (int col = 0; col < 10; col++) {

			if (col == 0) {
				Arrays.fill(filled, false);
			}

			boolean thisFilled = false;
			while (thisFilled == false) {
				int toFill = new Random().nextInt(10);// below not working
				// True and (false or true) and not false7
				// true and ((true or true/false) and (true and true/false))
				if (filled[toFill] == false
						&& !((toFill == 2 || toFill == 3 || toFill == 6 || toFill == 7) && frontRank[col] == 1)) {
					Movement.preparePiece(toFill, frontRowY, frontRank[col],
							aiSide);
					PieceData.removePieceFromWell(aiSide, frontRank[col]);
					filled[toFill] = true;
					// array of pieces to place place next one based on col
					thisFilled = true;
				}
			}
		}

		filled = new boolean[27];
		for (int row = 2; row >= 0; row--) {
			int extraFill = 0;
			int colToFill = 10;
			if (row == 1) {
				colToFill = 9;
				extraFill = 10;
			} else if (row == 0) {
				colToFill = 8;
				extraFill = 19;
			}
			for (int col = 0; col < colToFill; col++) {

				if (col == 0) {
					Arrays.fill(filled, false);
				}
				boolean thisFilled = false;
				while (thisFilled == false) {
					int toFill = new Random().nextInt(colToFill);
					if (filled[toFill + extraFill] == false) {
						Movement.preparePiece(col, row, backRanks[toFill
								+ extraFill], aiSide);
						PieceData.removePieceFromWell(aiSide, backRanks[toFill
								+ extraFill]);
						filled[toFill + extraFill] = true;
						thisFilled = true;
					}
				}
			}
		}
		Movement.preparePiece(8, 0, 11, aiSide);
		Movement.preparePiece(9, 0, 12, aiSide);
		Movement.preparePiece(9, 1, 11, aiSide);
		PieceData.removePieceFromWell(aiSide, 11);
		PieceData.removePieceFromWell(aiSide, 12);
		PieceData.removePieceFromWell(aiSide, 11);
	}
}
