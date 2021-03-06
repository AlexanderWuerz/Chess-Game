import java.util.Arrays;

import javafx.util.Pair;

public abstract class ChessPiece {

	String color;
	String pieceType;

	protected boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy, boolean considerCheck) {
		if (fx == ix && fy == iy)
			return false; // cannot move nothing
		if (fx < 0 || fx > 13 || ix < 0 || ix > 13 || fy < 0 || fy > 13 || iy < 0 || iy > 13)
			return false; // cannot move off board

		if (considerCheck) {
			fourplayerChessGame copy = new fourplayerChessGame(cg);
			copy.sendMove(ix + " " + iy + " " + fx + " " + fy);

			King myKing = cg.myKing;
			if (myKing.inCheck(copy))
				return false; // cannot move into check

			// must still check for occupied space
		}
		return true;
	}

	public ChessPiece(String c) {
		color = c;
	}

	public Pair<Integer, Integer> loc(fourplayerChessGame cg) { // locates THIS
																// piece on the
																// board
		ChessPiece[][] board = cg.board;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == this)
					return new Pair(i, j);
			}
		}
		return null; // if THIS is not on the board
	}

	public String toString() {
		return color + pieceType + fourplayerChessGame.RESET;
	}

	public static class Pawn extends ChessPiece {

		char direction;

		public static final char up = 'u';
		public static final char down = 'd';
		public static final char left = 'l';
		public static final char right = 'r';

		boolean moved;

		public Pawn(String c, char dir) {
			super(c);
			direction = dir;
			moved = false;
			pieceType = "\u265F";
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy, boolean considerCheck) {

			// must still consider the pawn's capture move

			if (!super.legalMove(cg, ix, iy, fx, fy, considerCheck))
				return false;

			// if (((fx == ix + 1) || (fx == ix - 1)) && fy == iy + 1) {
			// if (!(cg.isMyPiece(fx, fy)) && !(cg.IsEmptySquare(fx,fy)))
			// return true;
			// }

			switch (direction) {
			case up:
				if (!(cg.isMyPiece(fx, fy)) && !(cg.IsEmptySquare(fx, fy)))
					return (fy == iy + 1 && ((fx == ix - 1) || (fx == ix + 1)));
			case down:
				if (!(cg.isMyPiece(fx, fy)) && !(cg.IsEmptySquare(fx, fy)))
					return (fy == iy - 1 && ((fx == ix - 1) || (fx == ix + 1)));
			case left:
				if (!(cg.isMyPiece(fx, fy)) && !(cg.IsEmptySquare(fx, fy)))
					return (fx == ix - 1 && ((fy == iy - 1) || (fy == iy + 1)));
				if (!(cg.isMyPiece(fx, fy)) && !(cg.IsEmptySquare(fx, fy)))
					return (fx == ix + 1 && ((fy == iy - 1) || (fy == iy + 1)));

			}

			if (moved) {
				switch (direction) {
				case up:
					return (ix == fx && fy == iy + 1);
				case down:
					return (ix == fx && fy == iy - 1);
				case left:
					return (iy == fy && fx == ix - 1);
				}

				return (iy == fy && fx == ix + 1); // must be right
			} else {
				switch (direction) {
				case up:
					return (ix == fx && ((fy == iy + 2) || (fy == iy + 1)));
				case down:
					return (ix == fx && ((fy == iy - 2) || (fy == iy - 1)));
				case left:
					return (iy == fy && ((fx == ix - 2) || (fx == ix - 1)));
				}

				return (iy == fy && ((fx == ix + 2) || (fx == ix + 1))); // must
																			// be
																			// right
			}

		}

		// public void move(fourplayerChessGame cg, int ix, int iy, int fx, int
		// fy) {
		// moved = true;
		// super.move(cg, ix, iy, fx, fy);
		// }
	}

	public static class Knight extends ChessPiece {

		public Knight(String c) {
			super(c);
			pieceType = "\u265E";
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy, boolean considerCheck) {

			if (!super.legalMove(cg, ix, iy, fx, fy, considerCheck))
				return false;

			if (!(((fx == ix - 2) || (fx == ix + 2)) && ((fy == iy - 1) || (fy == iy + 1)))
					&& !(((fx == ix - 1) || (fx == ix + 1)) && ((fy == iy - 2) || (fy == iy + 2))))
				return false;

			return true;
		}

	}

	public static class Bishop extends ChessPiece {
		int k;

		public Bishop(String c) {
			super(c);
			pieceType = "\u265D";
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy, boolean considerCheck) {
			if (!super.legalMove(cg, ix, iy, fx, fy, considerCheck))
				return false;
			if ((Math.abs(ix - fx) == Math.abs(iy - fy)) && !(cg.isMyPiece(fx, fy)))
				return true;
			return false;
		}

	}

	public static class Rook extends Queen {

		public Rook(String c) {
			super(c);
			pieceType = "\u265C";
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy, boolean considerCheck) {
			// will take direction into account
			if (!super.legalMove(cg, ix, iy, fx, fy, considerCheck))
				return false;
			if ((Math.abs(fx - ix) == Math.abs(fy - iy))) // diagonal move
				return false;
			return true;
		}

	}

	public static class Queen extends ChessPiece {
		int k = 0;

		public Queen(String c) {
			super(c);
			pieceType = "\u265B";
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy, boolean considerCheck) {
			// will take direction into account
			if (!super.legalMove(cg, ix, iy, fx, fy, considerCheck))
				return false;

			
			if (iy == fy) { // horizontal move
				if (ix < fx) { // move right
					for (k = ix + 1; k <= fx; ++k) {
						if (cg.getPiece(k, iy) != null)
							return false;
					}
				} else if (ix > fx) { // move left
					for (k = ix - 1; k >= fx; --k) {
						if (cg.getPiece(k, iy) != null)
							return false;
					}
				}
			} else if (ix == fx) { // vertical move
				if (iy < fy) { // move down
					for (k = iy + 1; k <= fy; ++k) {
						if (cg.getPiece(ix, k) != null)
							return false;
					}
				} else if (iy > fy) { // move up
					for (k = iy - 1; k >= fy; --k) {
						if (cg.getPiece(ix, k) != null)
							return false;
					}
				}
			} else{
				
				if (!(Math.abs(ix - fx) == Math.abs(iy - fy))) 
						  return false; // diagonal move return true;
				 
			}
			return true;
		}

	}

	public static class King extends ChessPiece {

		public King(String c) {
			super(c);
			pieceType = "\u265A";
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy, boolean considerCheck) {
			if (!super.legalMove(cg, ix, iy, fx, fy, considerCheck))
				return false;

			if (Math.abs(ix - fx) > 1 || Math.abs(iy - fy) > 1)
				return false;

			return true;
		}

		public boolean inCheck(fourplayerChessGame cg) {

			Pair<Integer, Integer> kingCoords = loc(cg);

			ChessPiece[][] board = cg.board;
			for (int i = 0; i < 14; i++) {
				for (int j = 0; j < 14; j++) {
					if (board[i][j] != null && !(board[i][j] instanceof blocked)) {
						if (!cg.isMyPiece(i, j)
								&& board[i][j].legalMove(cg, i, j, kingCoords.getKey(), kingCoords.getValue(), false))
							return true;
					}
				}
			}

			return false;
			// tests if THIS king is in check
		}

		public boolean isCheckMate(fourplayerChessGame cg) {

			Pair<Integer, Integer> kingCoords = loc(cg);
			int kx = kingCoords.getKey(), ky = kingCoords.getValue();

			for (int i = 0; i < 14; i++)
				for (int j = 0; j < 14; j++)
					if (cg.isMyPiece(i, j)) // checks every friendly piece
						for (int k = 0; k < 14; k++)
							for (int m = 0; m < 14; m++)
								if (k != kx && m != ky && legalMove(cg, i, j, k, m, true))
									return false;

			return true;
		}

	}

	public static class blocked extends ChessPiece {

		public blocked(String c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		public String toString() {
			return "X";
		}

		public boolean legalMove() {
			return false;

		}

	}

	public void move(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {
		// TODO Auto-generated method stub

	}

}
