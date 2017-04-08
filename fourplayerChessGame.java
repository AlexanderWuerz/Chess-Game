/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.net.*;
import java.io.*;
import java.util.regex.*; 
import java.util.*; 

class fourplayerChessGame {
 	private static final int WAITING = 0; 
	private static final int SENTBOARD = 1; 
	public static int state = WAITING; 
	public static final int WTURN = 2; 
	public static final int BTURN = 3; 
	public static final int RTURN = 4; 
	public static final int LTURN = 5; 
	
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String RESET = "\u001B[0m";

	
  public static String[][] board = new String[14][14]; // 14 x 14
//public static String[][] board = ChessServer.board;
 public static boolean turn = false; // Fire Before Smoke, WHITE goes first. 
/* public static void main(String[] args) {
 //SetTestBoard();  
 SetInitBoard(); 
//SetOpeningBoard(); 
 } */
 
 public static void SetPiece(int x, int y, String r){
  board[x][y] = r;
 }
 public static String getPiece(int x, int y) {
  String pie; 
  pie = board[x][y]; // Get piece name; 
  return pie; 
 }
 public static void RemovePiece(int x, int y) {
  board[x][y] = " "; 
 }
 public static void SetTestBoard() {
  //SetPiece(1,0,"n");
//  SetPiece(2,0,"r");
  SetPiece(3,1,"k"); 
 // SetPiece(1,1,"q"); 
  SetPiece(1,5,"R"); 
  setBoard(); 
 }
 public static void SetInitBoard() {
  
  // set the pawns
   for(int i=3; i<11; i++) {
    SetPiece(i,1,"\u265F"); 
    SetPiece(i,12,"\u2659"); 
   } // and other pieces
   SetPiece(3,0,"\u265C");
   SetPiece(10,0,"\u265C"); 
   SetPiece(3,13,"\u2656");
   SetPiece(10,13,"\u2656"); 
   SetPiece(4,0,"\u265E");
   SetPiece(9,0,"\u265E"); 
   SetPiece(4,13,"\u2658");
   SetPiece(9,13,"\u2658"); 
   SetPiece(5,0,"\u265D");
   SetPiece(8,0,"\u265D"); 
   SetPiece(5,13,"\u2657");
   SetPiece(8,13,"\u2657"); 
   SetPiece(6,0,"\u265B"); 
   SetPiece(7,0,"\u265A"); 
   SetPiece(6,13,"\u2655"); 
   SetPiece(7,13,"\u2654"); 
   
	for(int i=3; i<11; i++) {
    SetPiece(1,i,"\u265F"); 
    SetPiece(12,i,"\u2659"); 
   }

	for(int row = 0; row < board.length; row++) {
		for(int col = 0; col < board[row].length; col++) {
			if(board[col][row] == null) {
				SetPiece(col,row," "); 
			}
		}
	}
   //setBoard(); 
           // System.out.println(""); 
 } // end setInitBoard()

 public static void setBoard() {
             System.out.println("   0   1   2   3   4   5   6   7   \t"); 
   for (int row = 0; row < board.length; row++)
            {
                System.out.println("");
                System.out.println(" ------------------------------------------------------------------");

                for(int col = 0; col < board[row].length; col++)
                {

                    System.out.print(" | "); //you're only printing spaces in the spots
                    if(board[col][row] == null){
                      System.out.print(" ");
                    }else{
                      System.out.print(board[col][row]);
                    }
                }
      System.out.print(" | "+row); 
            }
            System.out.println("");
            System.out.println(" ------------------------------------------------------------------");
      System.out.println("   0   1   2   3   4   5   6   7   \t"); 
    
    //MoveIntake();   

 } // end setBoard() 

 public static boolean IsValidMove(int ix, int iy, int fx, int fy, String type) {
	int k = 0; 
  boolean eval = false; 
 /* if(!IsMyPiece(ix,iy))
   return false;  */
	if(fx == ix && fy == iy)
		return false; //cannot move nothing
	if(fx < 0 || fx > 13 || ix < 0 || ix > 13 || fy < 0 || fy > 13 || iy <0 || iy > 13)	   return false; // cannot move outside the board
 switch(type) {
    case "\u265F": // black pawn
     if(fx==ix && fy==iy+1) {
      eval = true; // is valid move for pawn
	}
	else if (iy==1) {
		if(fx==ix&&fy==iy+2)
		eval = true; 		
	}
     else if(((fx==ix+1)||(fx==ix-1)) && fy==iy+1){
      if(!IsMyPiece(fx,fy)) {
       if(!IsEmptySquare(fx,fy)) {       
			eval = true;         
			Capture(fx,fy); // capture piece 
       }
      } else 
       eval = false; 
//     }
}     break;
    case "\u265B": // black king
     if((fx==ix||fx==ix+1||fx==ix-1) && (fy==iy||fy==iy+1||fy==iy-1)) {

      if(!IsMyPiece(fx,fy)) {
	      eval = true; 
		/*if(!IsEmptySquare(fx,fy))
       		Capture(fx,fy); */
}
     }
     break; 
    case "\u265A": // black queen (diagonal moves done)
	     	 k = 0; 
	/*if(iy==fy && ix==fx) 
		return false; // attempt to move to same position
	   if(fx < 0 || fx > 13 || ix < 0 || ix > 13 || fy < 0 || fy > 13 || iy <0 || iy > 13)
	    return false; */

	if (iy == fy) { // horizontal move 
		if(ix<fx) { // move right
			for(k=ix+1; k<=fx; ++k)
				if(getPiece(k,iy) != null && !IsMyPiece(k,iy)){
					Capture(k,iy); 
					return true; 
				}		
				else if(getPiece(k,iy) != null)
					return false; 
		} else if (ix>fx){ // move left
			for(k=ix-1; k>=fx; --k)
				if(!IsEmptySquare(k,iy) && !IsMyPiece(k,iy)) {
					Capture(k,iy); 
					return true; 
				}
				else if(getPiece(k,iy) != null) 
					return false; 
		}
	} else if (ix == fx) { // vertical move
		// Vertical move
		        if (iy < fy) {
		            // Move down
		            for (k = iy + 1; k <= fy; ++k)
						if (!IsEmptySquare(ix,k) && !IsMyPiece(ix,k)) {
							Capture(ix,k); 
							return true; 
						}
		                else if (getPiece(ix,k) != null)
		                    return false;
		        } else if (iy > fy){
		            // Move up
		            for (k = iy - 1; k >= fy; --k)
						if(!IsEmptySquare(ix,k) && !IsMyPiece(ix,k)) {
							Capture(ix,k); 
							return true; 
						}
		                else if (getPiece(ix,k) != null)
		                    return false;
		        }
		else return false; 
	} /*else // not a valid rook move
		return false; */

			else if((Math.abs(fx-ix) == Math.abs(fy-iy))) {
				eval = true; 
			}
		      break;

    case "\u265C": // black rook
	 k = 0; 
	/*if(iy==fy && ix==fx) 
		return false; // attempt to move to same position
    if(fx < 0 || fx > 13 || ix < 0 || ix > 13 || fy < 0 || fy > 13 || iy <0 || iy > 13)
	    return false; */

	if (iy == fy) { // horizontal move 
		if(ix<fx) { // move right
			for(k=ix+1; k<=fx; ++k)
				if(getPiece(k,iy) != null && !IsMyPiece(k,iy)){
					Capture(k,iy); 
					return true; 
				}		
				else if(getPiece(k,iy) != null)
					return false; 
		} else if (ix>fx){ // move left
			for(k=ix-1; k>=fx; --k)
				if(!IsEmptySquare(k,iy) && !IsMyPiece(k,iy)) {
					Capture(k,iy); 
					return true; 
				}
				else if(getPiece(k,iy) != null) 
					return false; 
		}
	} else if (ix == fx) { // vertical move
		// Vertical move
		        if (iy < fy) {
		            // Move down
		            for (k = iy + 1; k <= fy; ++k)
						if (!IsEmptySquare(ix,k) && !IsMyPiece(ix,k)) {
							Capture(ix,k); 
							return true; 
						}
		                else if (getPiece(ix,k) != null)
		                    return false;
		        } else if (iy > fy){
		            // Move up
		            for (k = iy - 1; k >= fy; --k)
						if(!IsEmptySquare(ix,k) && !IsMyPiece(ix,k)) {
							Capture(ix,k); 
							return true; 
						}
		                else if (getPiece(ix,k) != null)
		                    return false;
		        } 
	} else // not a valid rook move
		return false; 
	break; 
    case "\u265D": //black bishop
 /*	if(iy==fy && ix==fx)
    	return false; //cannot move nothing
    if(fx < 0 || fx > 13 || ix < 0 || ix > 13 || fy < 0 || fy > 13 || iy <0 || iy > 13)
        return false;  */
	if(Math.abs(ix-fx) == Math.abs(iy-fy)) {
		return true; 
	}
	case "\u265E": // black knight
	if ((((fx==ix-2)||(fx==ix+2)) && ((fy==iy-1)||(fy==iy+1))) ||
	      (((fx==ix-1)||(fx==ix+1)) && ((fy==iy-2)||(fy==iy+2)))) {
	      if(!IsEmptySquare(fx,fy)) {
	       if (!IsMyPiece(fx,fy)) {
	        eval = true; 
	        Capture(fx,fy); 
	       } else 
	        eval = false; 
	       } else // is empty square but no piece is captured. 
	        eval = true; 
	       } else // not valid move
	        eval = false; 
	    break; 
     
    case "\u2659": // white pawn

     if(fx==ix && fy==iy-1) {
      eval = true; // is valid move for pawn
}
	else if (iy==12) {
		if(fx==ix&&fy==iy-2)
		eval = true; 		
	}
     else if(((fx==ix+1)||(fx==ix-1)) && fy==iy-1){
      if(!IsMyPiece(fx,fy)) {
       if(!IsEmptySquare(fx,fy)) {       
			eval = true;         
			Capture(fx,fy); // capture piece 
       }
      } else 
       eval = false; 
     }

     break;
    case "\u2655": // white king
     if((fx==ix||fx==ix+1||fx==ix-1) && (fy==iy||fy==iy+1||fy==iy-1)) {
      eval = true; 
      if(!IsMyPiece(fx,fy))
       Capture(fx,fy); 
     }
     break; 
    case "\u2654": // white queen 
     	 k = 0; 
/*if(iy==fy && ix==fx) 
	return false; // attempt to move to same position
   if(fx < 0 || fx > 13 || ix < 0 || ix > 13 || fy < 0 || fy > 13 || iy <0 || iy > 13)
    return false; */

if (iy == fy) { // horizontal move 
	if(ix<fx) { // move right
		for(k=ix+1; k<=fx; ++k)
			if(getPiece(k,iy) != null && !IsMyPiece(k,iy)){
				Capture(k,iy); 
				return true; 
			}		
			else if(getPiece(k,iy) != null)
				return false; 
	} else if (ix>fx){ // move left
		for(k=ix-1; k>=fx; --k)
			if(!IsEmptySquare(k,iy) && !IsMyPiece(k,iy)) {
				Capture(k,iy); 
				return true; 
			}
			else if(getPiece(k,iy) != null) 
				return false; 
	}
} else if (ix == fx) { // vertical move
	// Vertical move
	        if (iy < fy) {
	            // Move down
	            for (k = iy + 1; k <= fy; ++k)
					if (!IsEmptySquare(ix,k) && !IsMyPiece(ix,k)) {
						Capture(ix,k); 
						return true; 
					}
	                else if (getPiece(ix,k) != null)
	                    return false;
	        } else if (iy > fy){
	            // Move up
	            for (k = iy - 1; k >= fy; --k)
					if(!IsEmptySquare(ix,k) && !IsMyPiece(ix,k)) {
						Capture(ix,k); 
						return true; 
					}
	                else if (getPiece(ix,k) != null)
	                    return false;
	        }
	else return false; 
} 

		else if((Math.abs(fx-ix) == Math.abs(fy-iy))) {
			eval = true; 
		}
	      break;

    case "\u2656": // white rook
	 k = 0; 
	/*if(iy==fy && ix==fx) 
		return false; // attempt to move to same position
	   if(fx < 0 || fx > 13 || ix < 0 || ix > 13 || fy < 0 || fy > 13 || iy <0 || iy > 13)
	    return false; */

	if (iy == fy) { // horizontal move 
		if(ix<fx) { // move right
			for(k=ix+1; k<=fx; ++k)
				if(getPiece(k,iy) != null && !IsMyPiece(k,iy)){
					Capture(k,iy); 
					return true; 
				}		
				else if(getPiece(k,iy) != null)
					return false; 
		} else if (ix>fx){ // move left
			for(k=ix-1; k>=fx; --k)
				if(!IsEmptySquare(k,iy) && !IsMyPiece(k,iy)) {
					Capture(k,iy); 
					return true; 
				}
				else if(getPiece(k,iy) != null) 
					return false; 
		}
	} else if (ix == fx) { // vertical move
		// Vertical move
		        if (iy < fy) {
		            // Move down
		            for (k = iy + 1; k <= fy; ++k)
						if (!IsEmptySquare(ix,k) && !IsMyPiece(ix,k)) {
							Capture(ix,k); 
							return true; 
						}
		                else if (getPiece(ix,k) != null)
		                    return false;
		        } else if (iy > fy){
		            // Move up
		            for (k = iy - 1; k >= fy; --k)
						if(!IsEmptySquare(ix,k) && !IsMyPiece(ix,k)) {
							Capture(ix,k); 
							return true; 
						}
		                else if (getPiece(ix,k) != null)
		                    return false;
		        } 
	} else // not a valid rook move
		return false; 
	break; 
    case "\u2657":  // white bishop
/*	if(fx == ix && fy == iy)
	    	return false; //cannot move nothing
	    if(fx < 0 || fx > 13 || ix < 0 || ix > 13 || fy < 0 || fy > 13 || iy <0 || iy > 13)
	        return false; */
		if((Math.abs(fx-ix) == Math.abs(fy-iy))) {
			eval = true; 
		}
	      break;
    case "\u2658": // white night
     if ((((fx==ix-2)||(fx==ix+2)) && ((fy==iy-1)||(fy==iy+1))) ||
      (((fx==ix-1)||(fx==ix+1)) && ((fy==iy-2)||(fy==iy+2)))) {
      if(!IsEmptySquare(fx,fy)) {
       if (!IsMyPiece(fx,fy)) {
        eval = true; 
        Capture(fx,fy); 
       } else 
        eval = false; 
       } else // is empty square but no piece is captured. 
        eval = true; 
       } else // not valid move
        eval = false; 
    break; 

   } 
   return eval; 

} // end IsValidMove()


 public String processInput(String theInput) {
	String theOutput = null; 
	  Scanner in = new Scanner(System.in); 
	  int startX,startY,xmove,ymove; 
	  String pname = " ", input; // piece name
	String[] values; 

	if (state == WAITING) {
		SetInitBoard(); 
		theOutput = toString(); 
		//SetInitBoard(); 
		state = SENTBOARD; 
	} else if (state == SENTBOARD || state == WTURN) {

		    //input = in.nextLine(); // get the entire line. 
		   values = theInput.split(" "); // split on spaces. 
		   startX = Integer.parseInt(values[0]); 
		   startY = Integer.parseInt(values[1]); 
			xmove = Integer.parseInt(values[2]); 
			ymove = Integer.parseInt(values[3]); 
		   //pname = values[2];
		   pname = getPiece(startX,startY);  
		    
		   //pname = in.nextLine(); 

		   // validate that the move is legal. 
		   if(IsValidMove(startX,startY,xmove,ymove,pname)) {
		    RemovePiece(startX,startY); 
		    SetPiece(xmove,ymove,pname); 
		    //turn = true; 
			state = BTURN; // switch to black turn
			setBoard(); 
			theOutput = toString(); 
		//setBoard(); 
		   } 
		   else {
			//setBoard(); 
		    theOutput = "Invalid move, try again!"; 
		   }

	} else if (state == BTURN) {

			    //input = in.nextLine(); // get the entire line. 
			   values = theInput.split(" "); // split on spaces. 
			   startX = Integer.parseInt(values[0]); 
			   startY = Integer.parseInt(values[1]); 
				xmove = Integer.parseInt(values[2]); 
				ymove = Integer.parseInt(values[3]); 
			   //pname = values[2];
			   pname = getPiece(startX,startY);  
			    
			   //pname = in.nextLine(); 

			   // validate that the move is legal. 
			   if(IsValidMove(startX,startY,xmove,ymove,pname)) {
			    RemovePiece(startX,startY); 
			    SetPiece(xmove,ymove,pname); 
			    //turn = true; 
				state = WTURN; // switch to black turn
			setBoard(); 
			theOutput = toString(); 
			   } 
			   else {
				//setBoard(); 
			    theOutput = "Invalid move, try again!"; 
			   }

		}

	return theOutput; 
}
public String toString() {
	String b = "\t"+
	"  0   1   2   3   4   5   6   7   8   9   10  11  12  13   "+
	"\t            ---------------------------------           \t"+
	"            | "+getPiece(3,0)+" | "+getPiece(4,0)+" | "+getPiece(5,0)+" | "+ 	getPiece(6,0)+" | "+getPiece(7,0)+" | "+getPiece(8,0)+" | "+
	getPiece(9,0)+" | "+getPiece(10,0)+" |              0"+
	"\t            ---------------------------------           \t"+
	"            | "+getPiece(3,1)+" | "+getPiece(4,1)+" | "+getPiece(5,1)+" | "+ 	getPiece(6,1)+" | "+getPiece(7,1)+" | "+getPiece(8,1)+" | "+
	getPiece(9,1)+" | "+getPiece(10,1)+" |              1"+
	"\t            ---------------------------------           \t"+
	"            | "+getPiece(3,2)+" | "+getPiece(4,2)+" | "+getPiece(5,2)+" | "+ 	getPiece(8,2)+" | "+getPiece(7,2)+" | "+getPiece(6,2)+" | "+
		getPiece(9,2)+" | "+getPiece(10,2)+" |             2"+
		"\t---------------------------------------------------------\t"+
		"| "+getPiece(0,3)+" | "+getPiece(1,3)+" | "+getPiece(2,3)+" | "+ 	getPiece(3,3)+" | "+getPiece(4,3)+" | "+getPiece(5,3)+" | "+
		getPiece(6,3)+" | "+getPiece(7,3)+" | "+
		getPiece(8,3)+" | "+getPiece(9,3)+" | "+getPiece(10,3)+" | "+ 		getPiece(11,3)+" | "+getPiece(12,3)+" | "+getPiece(13,3)+" | 3"+
		"\t---------------------------------------------------------\t"+
	"| "+getPiece(0,4)+" | "+getPiece(1,4)+" | "+getPiece(2,4)+" | "+ 	getPiece(3,4)+" | "+getPiece(4,4)+" | "+getPiece(5,4)+" | "+
		getPiece(6,4)+" | "+getPiece(7,4)+" | "+
				getPiece(8,4)+" | "+getPiece(9,4)+" | "+getPiece(10,4)+" | "+ 		getPiece(11,4)+" | "+getPiece(12,4)+" | "+getPiece(13,4)+" | 4"+
		"\t---------------------------------------------------------\t"+
		"| "+getPiece(0,5)+" | "+getPiece(1,5)+" | "+getPiece(2,5)+" | "+ 	getPiece(3,5)+" | "+getPiece(4,5)+" | "+getPiece(5,5)+" | "+
		getPiece(6,5)+" | "+getPiece(7,5)+" | "+
				getPiece(8,5)+" | "+getPiece(9,5)+" | "+getPiece(10,5)+" | "+ 		getPiece(11,5)+" | "+getPiece(12,5)+" | "+getPiece(13,5)+" | 5"+
		"\t---------------------------------------------------------\t"+
		"| "+getPiece(0,6)+" | "+getPiece(1,6)+" | "+getPiece(2,6)+" | "+ 	getPiece(3,6)+" | "+getPiece(4,6)+" | "+getPiece(5,6)+" | "+
			getPiece(6,6)+" | "+getPiece(7,6)+" | "+
					getPiece(8,6)+" | "+getPiece(9,6)+" | "+getPiece(10,6)+" | "+ 		getPiece(11,6)+" | "+getPiece(12,6)+" | "+getPiece(13,6)+" | 6"+
			"\t---------------------------------------------------------\t"+
			"| "+getPiece(0,7)+" | "+getPiece(1,7)+" | "+getPiece(2,7)+" | "+ 	getPiece(3,7)+" | "+getPiece(4,7)+" | "+getPiece(5,7)+" | "+
			getPiece(6,7)+" | "+getPiece(7,7)+" | "+
					getPiece(8,7)+" | "+getPiece(9,7)+" | "+getPiece(10,7)+" | "+ 		getPiece(11,7)+" | "+getPiece(12,7)+" | "+getPiece(13,7)+" | 7"+
			"\t---------------------------------------------------------\t"+
	"| "+getPiece(0,8)+" | "+getPiece(1,8)+" | "+getPiece(2,8)+" | "+ 	getPiece(3,8)+" | "+getPiece(4,8)+" | "+getPiece(5,8)+" | "+
			getPiece(6,8)+" | "+getPiece(7,8)+" | "+
					getPiece(8,8)+" | "+getPiece(9,8)+" | "+getPiece(10,8)+" | "+ 		getPiece(11,8)+" | "+getPiece(12,8)+" | "+getPiece(13,8)+" | 8"+
			"\t---------------------------------------------------------\t"+
			"| "+getPiece(0,9)+" | "+getPiece(1,9)+" | "+getPiece(2,9)+" | "+ 	getPiece(3,9)+" | "+getPiece(4,9)+" | "+getPiece(5,9)+" | "+
			getPiece(6,9)+" | "+getPiece(7,9)+" | "+
					getPiece(8,9)+" | "+getPiece(9,9)+" | "+getPiece(10,9)+" | "+ 		getPiece(11,9)+" | "+getPiece(12,9)+" | "+getPiece(13,9)+" | 3"+
			"\t---------------------------------------------------------\t"+
		"| "+getPiece(0,10)+" | "+getPiece(1,10)+" | "+getPiece(2,10)+" | "+ 	getPiece(3,10)+" | "+getPiece(4,10)+" | "+getPiece(5,10)+" | "+
			getPiece(6,10)+" | "+getPiece(7,10)+" | "+
					getPiece(8,10)+" | "+getPiece(9,10)+" | "+getPiece(10,10)+" | "+ 		getPiece(11,10)+" | "+getPiece(12,10)+" | "+getPiece(13,10)+" | 10"+
			"\t---------------------------------------------------------\t"+
			"            | "+getPiece(3,11)+" | "+getPiece(4,11)+" | "+getPiece(5,11)+" | "+ 	getPiece(6,11)+" | "+getPiece(7,11)+" | "+getPiece(8,11)+" | "+
			getPiece(6,11)+" | "+getPiece(7,11)+" | 11"+
			"\t            ---------------------------------           \t"+
			"            | "+getPiece(3,12)+" | "+getPiece(4,12)+" | "+getPiece(5,12)+" | "+ 	getPiece(6,12)+" | "+getPiece(7,12)+" | "+getPiece(8,12)+" | "+
				getPiece(9,12)+" | "+getPiece(10,12)+" | 12"+
				"\t            ---------------------------------           \t"+
				"            | "+getPiece(3,13)+" | "+getPiece(4,13)+" | "+getPiece(5,13)+" | "+ 	getPiece(6,13)+" | "+getPiece(7,12)+" | "+getPiece(8,13)+" | "+
				getPiece(9,12)+" | "+getPiece(10,13)+" | 13"+
			"\t            ---------------------------------           \t"+
			"  0   1   2   3   4   5   6   7   8   9   10  11  12  13   ";
		return b; 

}
 public static void MoveIntake() {
  
  Scanner in = new Scanner(System.in); 
  int startX,startY,xmove,ymove; 
  String pname = " ", input; // piece name
  String[] values; 
  while(turn) {
   System.out.println("Smoke, select piece to move (x y): "); 
    input = in.nextLine(); // get the entire line. 
    values = input.split(" "); // split on spaces. 
    startX = Integer.parseInt(values[0]); 
    startY = Integer.parseInt(values[1]); 
    pname = getPiece(startX,startY);  
    System.out.println("Where would you like to move? (x y) "); 
    //pname = in.nextLine(); 
    input = in.nextLine(); 
    values = input.split(" "); 
    xmove = Integer.parseInt(values[0]); 
    ymove = Integer.parseInt(values[1]); 
	//Move humanMove = new Move(xmove,ymove); 
    // validate that the move is legal. 
    if(IsValidMove(startX,startY,xmove,ymove,pname)) {
	//if(generateValidMoves(startX,startY,pname) != null) {
		
      RemovePiece(startX,startY);
     SetPiece(xmove,ymove,pname); 
     turn = false; 
    setBoard(); 
    }
    else {
     System.out.println("Invalid move, try again!"); 
    }  
  //  setBoard(); 
      } 
  while(!turn) { // FIRE goes first 

   System.out.println("Fire, select piece to move (x y): ");

    input = in.nextLine(); // get the entire line. 
   values = input.split(" "); // split on spaces. 
   startX = Integer.parseInt(values[0]); 
   startY = Integer.parseInt(values[1]); 
   //pname = values[2];
   pname = getPiece(startX,startY);  
   System.out.println("Where would you like to move? (x y)"); 
   //pname = in.nextLine(); 
   input = in.nextLine(); 
   values = input.split(" "); 
   xmove = Integer.parseInt(values[0]); 
   ymove = Integer.parseInt(values[1]); 

   // validate that the move is legal. 
   if(IsValidMove(startX,startY,xmove,ymove,pname)) {
    RemovePiece(startX,startY); 
    SetPiece(xmove,ymove,pname); 
    turn = true; 
//setBoard(); 
   } 
   else {
    System.out.println("Invalid move, try again!"); 
   }

  // setBoard(); 

  }
  
 } // end MoveIntake() 
 
 public static void Capture(int x, int y) {
	if(getPiece(x,y) == "\u265B" && state == WTURN)
		System.out.println("Game Over!"); 
	else if(getPiece(x,y) == "\u2655" && state == BTURN)
		System.out.println("Game Over!"); 
  RemovePiece(x,y); 
	EndGame(); 
 }
 public static boolean IsEmptySquare(int x, int y) {
 // String square = board[x][y]; 
  if (board[x][y] == null)
   return true; 
  else return false; 
 }
 public static Boolean IsMyPiece(int x, int y) {
  String piece = getPiece(x,y);
 
  if(state == WTURN) {
	if(piece=="\u2654"||piece=="\u2655"||piece=="\u2656"||piece=="\u2657"||
		piece=="\u2658"||piece=="\u2659")
  		return true; 
	else 
		return false; 
	}
  else if(state == BTURN) {
	if(piece=="\u265A"||piece=="\u265B"||piece=="\u265C"||piece=="\u265D"||
		piece=="\u265E"||piece=="\u265F")
   		return true;
	else 
		return false; 
	} 
	return false; 
 }
public static boolean isCapture(Move move) {
	int x = move.getX(); 
	int y = move.getY(); 
	if(!IsEmptySquare(x,y))
		return true; 
	else 
		return false; 
}

public static void EndGame() {}

 
}