
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

class fourplayerChessGame implements FPGame {
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

	ArrayList<ChessPiece> allPieces = new ArrayList<ChessPiece>();
	ArrayList<ChessPiece> myPieces = new ArrayList<ChessPiece>();
	public ChessPiece[][] board = new ChessPiece[14][14]; // 14 x 14
	// public static String[][] board = ChessServer.board;
	//public static boolean turn = false; // Fire Before Smoke, WHITE goes first.
	public boolean isOver = false;

	int playerNum;
	String mycolor;
	
	public fourplayerChessGame(fourplayerChessGame orig){
		allPieces = orig.allPieces;
		myPieces = orig.myPieces;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = orig.board[i][j];
			}
		}
				
	}
	public fourplayerChessGame(int pNum) {
		
		mycolor = pNum==0?"blue":pNum==1?"red":pNum==2?"purple":"cyan";
		playerNum = pNum;
		// set the pawns
		for (int i = 3; i < 11; i++) {
			SetPiece(i, 1, new ChessPiece.Pawn(BLUE, 'u'));
			SetPiece(i, 12, new ChessPiece.Pawn(RED, 'd'));
		} // and other pieces //Must be altered for 4 player ruleset
		SetPiece(3, 0, new ChessPiece.Rook(BLUE)); // Rooks
		SetPiece(10, 0, new ChessPiece.Rook(BLUE));
		SetPiece(3, 13, new ChessPiece.Rook(RED));
		SetPiece(10, 13, new ChessPiece.Rook(RED));
		SetPiece(0, 3, new ChessPiece.Rook(PURPLE)); // Rooks
		SetPiece(0, 10, new ChessPiece.Rook(PURPLE));
		SetPiece(13, 3, new ChessPiece.Rook(CYAN));
		SetPiece(13, 10, new ChessPiece.Rook(CYAN));
		SetPiece(4, 0, new ChessPiece.Knight(BLUE)); // knights
		SetPiece(9, 0, new ChessPiece.Knight(BLUE));
		SetPiece(4, 13, new ChessPiece.Knight(RED));
		SetPiece(9, 13, new ChessPiece.Knight(RED));
		SetPiece(0, 4, new ChessPiece.Knight(PURPLE)); // knights
		SetPiece(0, 9, new ChessPiece.Knight(PURPLE));
		SetPiece(13, 4, new ChessPiece.Knight(CYAN));
		SetPiece(13, 9, new ChessPiece.Knight(CYAN));
		SetPiece(5, 0, new ChessPiece.Bishop(BLUE)); // bishops
		SetPiece(8, 0, new ChessPiece.Bishop(BLUE));
		SetPiece(5, 13, new ChessPiece.Bishop(RED));
		SetPiece(8, 13, new ChessPiece.Bishop(RED));
		SetPiece(0, 5, new ChessPiece.Bishop(PURPLE)); // bishops
		SetPiece(0, 8, new ChessPiece.Bishop(PURPLE));
		SetPiece(13, 5, new ChessPiece.Bishop(CYAN));
		SetPiece(13, 8, new ChessPiece.Bishop(CYAN));
		SetPiece(6, 0, new ChessPiece.Queen(BLUE)); // queen
		SetPiece(7, 0, new ChessPiece.King(BLUE)); // king
		SetPiece(6, 13, new ChessPiece.Queen(RED)); // queen
		SetPiece(7, 13, new ChessPiece.King(RED)); // king
		SetPiece(0, 6, new ChessPiece.Queen(PURPLE)); // queen
		SetPiece(0, 7, new ChessPiece.King(PURPLE)); // king
		SetPiece(13, 6, new ChessPiece.Queen(CYAN)); // queen
		SetPiece(13, 7, new ChessPiece.King(CYAN)); // king

		for (int i = 3; i < 11; i++) {
			SetPiece(1, i, new ChessPiece.Pawn(PURPLE, 'r'));
			SetPiece(12, i, new ChessPiece.Pawn(CYAN, 'l'));
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				SetPiece(i, j, new ChessPiece.blocked(RESET));
				SetPiece(i, 13 - j, new ChessPiece.blocked(RESET));
				SetPiece(13 - i, 13 - j, new ChessPiece.blocked(RESET));
				SetPiece(13 - i, j, new ChessPiece.blocked(RESET));
			}
		}

		switch (playerNum) {
		case 0:
			for (int i = 3; i < 11; i++) {
				myPieces.add(getPiece(i, 1));
				myPieces.add(getPiece(i, 0));
			}
			break;
		case 1:
			for (int i = 3; i < 11; i++) {
				myPieces.add(getPiece(i, 12));
				myPieces.add(getPiece(i, 13));
			}
			break;
		case 2:
			for (int i = 3; i < 11; i++) {
				myPieces.add(getPiece(0, i));
				myPieces.add(getPiece(1, i));
			}
		case 3:
			for (int i = 3; i < 11; i++) {
				myPieces.add(getPiece(12, i));
				myPieces.add(getPiece(13, i));
			}
		}
		
		for(ChessPiece[] r:board)
			for(ChessPiece p:r)
				if(p!=null && !(p instanceof ChessPiece.blocked))
					allPieces.add(p);

	}

	public void SetPiece(int x, int y, ChessPiece r) {
		board[x][y] = r;
	}

	public ChessPiece getPiece(int x, int y) {

		return board[x][y];
	}

	public void RemovePiece(int x, int y) {
		board[x][y] = null;
	}

	public void setBoard() {
		System.out.println("   0   1   2   3   4   5   6   7   \t");
		for (int row = 0; row < board.length; row++) {
			System.out.println("");
			System.out.println(" ------------------------------------------------------------------");

			for (int col = 0; col < board[row].length; col++) {

				System.out.print(" | "); // you're only printing spaces in the
											// spots
				if (board[col][row] == null) {
					System.out.print(" ");
				} else {
					System.out.print(board[col][row]);
				}
			}
			System.out.print(" | " + row);
		}
		System.out.println("");
		System.out.println(" ------------------------------------------------------------------");
		System.out.println("   0   1   2   3   4   5   6   7   \t");

		// MoveIntake();
		// getMove();

	} // end setBoard()

	public boolean IsValidMove(int ix, int iy, int fx, int fy, ChessPiece pname) {

		return pname.legalMove(this, ix, iy, fx, fy);
	} // end IsValidMove()

	public String processInput(String theInput) {
		String theOutput = null;
		Scanner in = new Scanner(System.in);
		int startX, startY, xmove, ymove;
		String input; // piece name
		ChessPiece pname;
		String[] values;

		if (state == WAITING) {

			theOutput = toString();
			// SetInitBoard();
			state = SENTBOARD;
		} else if (state == SENTBOARD || state == WTURN) {

			// input = in.nextLine(); // get the entire line.
			values = theInput.split(" "); // split on spaces.
			startX = Integer.parseInt(values[0]);
			startY = Integer.parseInt(values[1]);
			xmove = Integer.parseInt(values[2]);
			ymove = Integer.parseInt(values[3]);
			// pname = values[2];
			pname = getPiece(startX, startY);

			// pname = in.nextLine();

			// validate that the move is legal.
			if (IsValidMove(startX, startY, xmove, ymove, pname)) {
				RemovePiece(startX, startY);
				SetPiece(xmove, ymove, pname);
				// turn = true;
				state = BTURN; // switch to black turn
				setBoard();
				theOutput = toString();
				// setBoard();
			} else {
				// setBoard();
				theOutput = "Invalid move, try again!";
			}

		} else if (state == BTURN) {

			// input = in.nextLine(); // get the entire line.
			values = theInput.split(" "); // split on spaces.
			startX = Integer.parseInt(values[0]);
			startY = Integer.parseInt(values[1]);
			xmove = Integer.parseInt(values[2]);
			ymove = Integer.parseInt(values[3]);
			// pname = values[2];
			pname = getPiece(startX, startY);

			// pname = in.nextLine();

			// validate that the move is legal.
			if (IsValidMove(startX, startY, xmove, ymove, pname)) {
				RemovePiece(startX, startY);
				SetPiece(xmove, ymove, pname);
				// turn = true;
				state = WTURN; // switch to black turn
				setBoard();
				theOutput = toString();
			} else {
				// setBoard();
				theOutput = "Invalid move, try again!";
			}

		}

		return theOutput;
	}


	public boolean IsEmptySquare(int x, int y) {
		// String square = board[x][y];
		if (board[x][y] == null)
			return true;
		else
			return false;
	}
	
	public boolean isMyPiece(int x, int y) {
		ChessPiece piece = getPiece(x,y); 
		if (myPieces.contains(piece)) 
			return true; 
		return false; 			
	}
	
	public boolean isCapture(Move move) {
		int x = move.getX();
		int y = move.getY();
		if (!IsEmptySquare(x, y))
			return true;
		else
			return false;
	}

	Scanner sc = new Scanner(System.in);

	@Override
	public String getMove() {

		int startX, startY, xmove, ymove;
		ChessPiece pname;
		String input; // piece name
		String[] values;
		System.out.println(mycolor+", Your Turn! ");
		setBoard();
		while (true) {
			System.out.println("Which Piece do you want to move?");
			input = sc.nextLine(); // get the entire line.
			values = input.split(" "); // split on spaces.
			if (values.length < 2) {
				System.out.println("Invalid move, try again!");
				continue;
			}
			startX = Integer.parseInt(values[0]);
			startY = Integer.parseInt(values[1]);
			ChessPiece p = getPiece(startX, startY);

			System.out.println("Where would you like to move? (x y)");
			// pname = in.nextLine();
			input = sc.nextLine();
			values = input.split(" ");
			if (values.length < 2)
				continue;
			xmove = Integer.parseInt(values[0]);
			ymove = Integer.parseInt(values[1]);

			// validate that the move is legal.
			if (myPieces.contains(p) && IsValidMove(startX, startY, xmove, ymove, p)) {
				return startX + " " + startY + " " + xmove + " " + ymove;
			} else {
				System.out.println("Invalid move, try again!");
			}
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void sendMove(String s) {
		String input = s;// get the entire line.
		String[] values = input.split(" "); // split on spaces.
		int startX = Integer.parseInt(values[0]);
		int startY = Integer.parseInt(values[1]);
		int xmove = Integer.parseInt(values[2]);
		int ymove = Integer.parseInt(values[3]);
		
		
		ChessPiece p = getPiece(startX, startY);
		RemovePiece(startX, startY);
		SetPiece(xmove, ymove, p);
		// RemovePiece(startX, startY);
		// SetPiece(xmove, ymove, p);
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOver() {

		return isOver;
	}

}