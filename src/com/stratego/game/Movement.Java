//Preferably PPnum is used to end setup well function when it reaches 79.
NEEDS:
if(MOUSE REALEASED ON SQUARE WHILE HOLDING GHOST FROM WELL){
  PreparePiece(X OF SQUARE,Y OF SQUARE, RANK, SIDE);
}
if (PIECE CLICKED ON){
  GatherPiece(X OF HOME, Y OF HOME);
}
if (MOUSE RELEASED WHILE HeldPiece != -1 && MOUSE OVER A SQUARE){
  PlacePiece(X OF SQUARE OVER, Y OF SQUARE OVER);
}
public class Movement {
  Public int HeldPiece = -1;
    /**Counter for piece number during generation*/
  Public int PPnum = 0;
  public static void PreparePiece(int x, int y, int rank, int side){
    Soldiers[PPnum] = new Piece(rank,side);
    GameBoard[x][y].piece = Soldier[PPnum];
    PPnum++;
  }
  public static void GatherPiece(int x, int y){
    if (Soldiers[GameBoard[x][y].piece]<=10){
    HeldPiece = GameBoard[x][y].piece;
    } else {
      //play error noise?
    }
  }
  public static void PlacePiece(int x, int y){
    //Moveable Destination?
    
    //Combat?
    else{
    //place piece here
    }
    //end
    HeldPiece = -1
  }
}
