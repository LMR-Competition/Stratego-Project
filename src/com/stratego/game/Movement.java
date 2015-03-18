//Preferably PPnum is used to end setup well function when it reaches 79.
/*
NEEDS:
if(MOUSE REALEASED ON SQUARE WHILE HOLDING GHOST FROM WELL){
  PreparePiece(X OF SQUARE,Y OF SQUARE, RANK, SIDE);   //Square over being the square the mouse or piece or piece is over, which is better interface? 
}
if (PIECE CLICKED ON){
  GatherPiece(X OF HOME, Y OF HOME);
}
if (MOUSE RELEASED WHILE HeldPiece is not null && MOUSE OVER A SQUARE){
  PlacePiece(X OF SQUARE OVER, Y OF SQUARE OVER);   //Square over being the square the mouse or piece or piece is over, which is better interface? 
}*/
public class Movement {
  public Piece HeldPiece = null;
    /**Counter for piece number during generation*/
  public int PPnum = 0;
  public static void PreparePiece(int x, int y, int rank, int side){
    soldiers[PPnum] = new Piece(x,y,rank,side);
    gameBoard[x][y].piece = soldier[PPnum];
    PPnum++;
  }
  public static void GatherPiece(int x, int y){
    if (soldiers[gameBoard[x][y].piece]<=10){
    heldPiece = gameBoard[x][y].piece;
    } else {
      //play error noise?
    }
  }
  public static void placePiece(int x, int y){
    //Moveable Destination?
    if((heldPiece.x == x&& heldPiece.y == y)||1<Math.abs(heldPiece.x-x) ||1<Math.abs(holdPiece.y-y){
    //error noise?
    } else(if gameBoard[x][y].piece !=null){
    //Combat?
    }
    else{
    //place piece here
    }
    //end
    heldPiece = null;
  }
}
