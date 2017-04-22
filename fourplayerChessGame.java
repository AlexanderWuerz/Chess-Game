
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

	ArrayList<ChessPiece> myPieces = new ArrayList<ChessPiece>();
	public ChessPiece[][] board = new ChessPiece[14][14]; // 14 x 14
	// public static String[][] board = ChessServer.board;
	public static boolean turn = false; // Fire Before Smoke, WHITE goes first.

	int playerNum;

	public fourplayerChessGame(int pNum) {
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

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				SetPiece(i, j, new ChessPiece.blocked(RESET));
				SetPiece(i, 13 - j, new ChessPiece.blocked(RESET));
				SetPiece(13 - i, 13 - j, new ChessPiece.blocked(RESET));
				SetPiece(13 - i, j, new ChessPiece.blocked(RESET));
			}

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

	public String toString() {
		String b = "\t" + "  0   1   2   3   4   5   6   7   8   9   10  11  12  13   "
				+ "\t            ---------------------------------           \t" + "            | " + getPiece(3, 0)
				+ " | " + getPiece(4, 0) + " | " + getPiece(5, 0) + " | " + getPiece(6, 0) + " | " + getPiece(7, 0)
				+ " | " + getPiece(8, 0) + " | " + getPiece(9, 0) + " | " + getPiece(10, 0) + " |              0"
				+ "\t            ---------------------------------           \t" + "            | " + getPiece(3, 1)
				+ " | " + getPiece(4, 1) + " | " + getPiece(5, 1) + " | " + getPiece(6, 1) + " | " + getPiece(7, 1)
				+ " | " + getPiece(8, 1) + " | " + getPiece(9, 1) + " | " + getPiece(10, 1) + " |              1"
				+ "\t            ---------------------------------           \t" + "            | " + getPiece(3, 2)
				+ " | " + getPiece(4, 2) + " | " + getPiece(5, 2) + " | " + getPiece(8, 2) + " | " + getPiece(7, 2)
				+ " | " + getPiece(6, 2) + " | " + getPiece(9, 2) + " | " + getPiece(10, 2) + " |             2"
				+ "\t---------------------------------------------------------\t" + "| " + getPiece(0, 3) + " | "
				+ getPiece(1, 3) + " | " + getPiece(2, 3) + " | " + getPiece(3, 3) + " | " + getPiece(4, 3) + " | "
				+ getPiece(5, 3) + " | " + getPiece(6, 3) + " | " + getPiece(7, 3) + " | " + getPiece(8, 3) + " | "
				+ getPiece(9, 3) + " | " + getPiece(10, 3) + " | " + getPiece(11, 3) + " | " + getPiece(12, 3) + " | "
				+ getPiece(13, 3) + " | 3" + "\t---------------------------------------------------------\t" + "| "
				+ getPiece(0, 4) + " | " + getPiece(1, 4) + " | " + getPiece(2, 4) + " | " + getPiece(3, 4) + " | "
				+ getPiece(4, 4) + " | " + getPiece(5, 4) + " | " + getPiece(6, 4) + " | " + getPiece(7, 4) + " | "
				+ getPiece(8, 4) + " | " + getPiece(9, 4) + " | " + getPiece(10, 4) + " | " + getPiece(11, 4) + " | "
				+ getPiece(12, 4) + " | " + getPiece(13, 4) + " | 4"
				+ "\t---------------------------------------------------------\t" + "| " + getPiece(0, 5) + " | "
				+ getPiece(1, 5) + " | " + getPiece(2, 5) + " | " + getPiece(3, 5) + " | " + getPiece(4, 5) + " | "
				+ getPiece(5, 5) + " | " + getPiece(6, 5) + " | " + getPiece(7, 5) + " | " + getPiece(8, 5) + " | "
				+ getPiece(9, 5) + " | " + getPiece(10, 5) + " | " + getPiece(11, 5) + " | " + getPiece(12, 5) + " | "
				+ getPiece(13, 5) + " | 5" + "\t---------------------------------------------------------\t" + "| "
				+ getPiece(0, 6) + " | " + getPiece(1, 6) + " | " + getPiece(2, 6) + " | " + getPiece(3, 6) + " | "
				+ getPiece(4, 6) + " | " + getPiece(5, 6) + " | " + getPiece(6, 6) + " | " + getPiece(7, 6) + " | "
				+ getPiece(8, 6) + " | " + getPiece(9, 6) + " | " + getPiece(10, 6) + " | " + getPiece(11, 6) + " | "
				+ getPiece(12, 6) + " | " + getPiece(13, 6) + " | 6"
				+ "\t---------------------------------------------------------\t" + "| " + getPiece(0, 7) + " | "
				+ getPiece(1, 7) + " | " + getPiece(2, 7) + " | " + getPiece(3, 7) + " | " + getPiece(4, 7) + " | "
				+ getPiece(5, 7) + " | " + getPiece(6, 7) + " | " + getPiece(7, 7) + " | " + getPiece(8, 7) + " | "
				+ getPiece(9, 7) + " | " + getPiece(10, 7) + " | " + getPiece(11, 7) + " | " + getPiece(12, 7) + " | "
				+ getPiece(13, 7) + " | 7" + "\t---------------------------------------------------------\t" + "| "
				+ getPiece(0, 8) + " | " + getPiece(1, 8) + " | " + getPiece(2, 8) + " | " + getPiece(3, 8) + " | "
				+ getPiece(4, 8) + " | " + getPiece(5, 8) + " | " + getPiece(6, 8) + " | " + getPiece(7, 8) + " | "
				+ getPiece(8, 8) + " | " + getPiece(9, 8) + " | " + getPiece(10, 8) + " | " + getPiece(11, 8) + " | "
				+ getPiece(12, 8) + " | " + getPiece(13, 8) + " | 8"
				+ "\t---------------------------------------------------------\t" + "| " + getPiece(0, 9) + " | "
				+ getPiece(1, 9) + " | " + getPiece(2, 9) + " | " + getPiece(3, 9) + " | " + getPiece(4, 9) + " | "
				+ getPiece(5, 9) + " | " + getPiece(6, 9) + " | " + getPiece(7, 9) + " | " + getPiece(8, 9) + " | "
				+ getPiece(9, 9) + " | " + getPiece(10, 9) + " | " + getPiece(11, 9) + " | " + getPiece(12, 9) + " | "
				+ getPiece(13, 9) + " | 3" + "\t---------------------------------------------------------\t" + "| "
				+ getPiece(0, 10) + " | " + getPiece(1, 10) + " | " + getPiece(2, 10) + " | " + getPiece(3, 10) + " | "
				+ getPiece(4, 10) + " | " + getPiece(5, 10) + " | " + getPiece(6, 10) + " | " + getPiece(7, 10) + " | "
				+ getPiece(8, 10) + " | " + getPiece(9, 10) + " | " + getPiece(10, 10) + " | " + getPiece(11, 10)
				+ " | " + getPiece(12, 10) + " | " + getPiece(13, 10) + " | 10"
				+ "\t---------------------------------------------------------\t" + "            | " + getPiece(3, 11)
				+ " | " + getPiece(4, 11) + " | " + getPiece(5, 11) + " | " + getPiece(6, 11) + " | " + getPiece(7, 11)
				+ " | " + getPiece(8, 11) + " | " + getPiece(6, 11) + " | " + getPiece(7, 11) + " | 11"
				+ "\t            ---------------------------------           \t" + "            | " + getPiece(3, 12)
				+ " | " + getPiece(4, 12) + " | " + getPiece(5, 12) + " | " + getPiece(6, 12) + " | " + getPiece(7, 12)
				+ " | " + getPiece(8, 12) + " | " + getPiece(9, 12) + " | " + getPiece(10, 12) + " | 12"
				+ "\t            ---------------------------------           \t" + "            | " + getPiece(3, 13)
				+ " | " + getPiece(4, 13) + " | " + getPiece(5, 13) + " | " + getPiece(6, 13) + " | " + getPiece(7, 12)
				+ " | " + getPiece(8, 13) + " | " + getPiece(9, 12) + " | " + getPiece(10, 13) + " | 13"
				+ "\t            ---------------------------------           \t"
				+ "  0   1   2   3   4   5   6   7   8   9   10  11  12  13   ";
		return b;

	}

	public void MoveIntake() {

		Scanner in = new Scanner(System.in);
		int startX, startY, xmove, ymove;
		ChessPiece pname;
		String input; // piece name
		String[] values;
		while (turn) {
			System.out.println("Smoke, select piece to move (x y): ");
			input = in.nextLine(); // get the entire line.
			values = input.split(" "); // split on spaces.
			startX = Integer.parseInt(values[0]);
			startY = Integer.parseInt(values[1]);
			pname = getPiece(startX, startY);
			System.out.println("Where would you like to move? (x y) ");
			// pname = in.nextLine();
			input = in.nextLine();
			values = input.split(" ");
			xmove = Integer.parseInt(values[0]);
			ymove = Integer.parseInt(values[1]);
			// Move humanMove = new Move(xmove,ymove);
			// validate that the move is legal.
			if (IsValidMove(startX, startY, xmove, ymove, pname)) {
				// if(generateValidMoves(startX,startY,pname) != null) {

				RemovePiece(startX, startY);
				SetPiece(xmove, ymove, pname);
				turn = false;
				setBoard();
			} else {
				System.out.println("Invalid move, try again!");
			}
			// setBoard();
		}
		while (!turn) { // FIRE goes first

			System.out.println("Fire, select piece to move (x y): ");

			input = in.nextLine(); // get the entire line.
			values = input.split(" "); // split on spaces.
			startX = Integer.parseInt(values[0]);
			startY = Integer.parseInt(values[1]);
			// pname = values[2];
			pname = getPiece(startX, startY);
			System.out.println("Where would you like to move? (x y)");
			// pname = in.nextLine();
			input = in.nextLine();
			values = input.split(" ");
			xmove = Integer.parseInt(values[0]);
			ymove = Integer.parseInt(values[1]);

			// validate that the move is legal.
			if (IsValidMove(startX, startY, xmove, ymove, pname)) {
				RemovePiece(startX, startY);
				SetPiece(xmove, ymove, pname);
				turn = true;
				// setBoard();
			} else {
				System.out.println("Invalid move, try again!");
			}

			// setBoard();

		}

	} // end MoveIntake()

	public boolean IsEmptySquare(int x, int y) {
		// String square = board[x][y];
		if (board[x][y] == null)
			return true;
		else
			return false;
	}

	public Boolean IsMyPiece(int x, int y) { // no longer needed
		ChessPiece piece = getPiece(x, y);

		// if(state == WTURN) {
		// if(piece=="\u2654"||piece=="\u2655"||piece=="\u2656"||piece=="\u2657"||
		// piece=="\u2658"||piece=="\u2659")
		// return true;
		// else
		// return false;
		// }
		// else if(state == BTURN) {
		// if(piece=="\u265A"||piece=="\u265B"||piece=="\u265C"||piece=="\u265D"||
		// piece=="\u265E"||piece=="\u265F")
		// return true;
		// else
		// return false;
		// }
		// return false;
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
		System.out.println("Your Turn! ");

		while (true) {
			System.out.println("Which Piece do you want to move?");
			input = sc.nextLine(); // get the entire line.
			values = input.split(" "); // split on spaces.
			startX = Integer.parseInt(values[0]);
			startY = Integer.parseInt(values[1]);
			ChessPiece p = getPiece(startX, startY);

			System.out.println("Where would you like to move? (x y)");
			// pname = in.nextLine();
			input = sc.nextLine();
			values = input.split(" ");
			xmove = Integer.parseInt(values[0]);
			ymove = Integer.parseInt(values[1]);

			// validate that the move is legal.
			if (myPieces.contains(p) && IsValidMove(startX, startY, xmove, ymove, p)) {
				RemovePiece(startX, startY);
				SetPiece(xmove, ymove, p);
				turn = true;
				// setBoard();
			} else {
				System.out.println("Invalid move, try again!");
			}
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void sendMove(String s) {
		// TODO Auto-generated method stub

	}

}