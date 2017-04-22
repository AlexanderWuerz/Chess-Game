
public abstract class ChessPiece {

	String color;

	protected boolean legalMove(fourplayerChessGame fourplayerChessGame, int ix, int iy, int fx, int fy) {
		if (fx == ix && fy == iy)
			return false; // cannot move nothing
		if (fx < 0 || fx > 7 || ix < 0 || ix > 7 || fy < 0 || fy > 7 || iy < 0
				|| iy > 7)
			return false; // cannot move off board

		
		// must still check for occupied space

		return true;
	}

	public ChessPiece(String c) {
		color = c;
	}

	public void move(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {
		if (legalMove(cg, ix, iy, fx, fy)) {

			// edit position in cg

		}
	}

	public static class Pawn extends ChessPiece {

		char direction;

		public static final char up = 'u';
		public static final char down = 'd';
		public static final char left = 'l';
		public static final char right = 'r';

		boolean moved;

		public Pawn(String c, char dir) throws Exception {
			super(c);
			if (dir != 'u' && dir != 'd' && dir != 'l' && dir != 'r')
				throw new Exception();
			direction = dir;
			moved = false;
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {
			
			//must still consider the pawn's capture move
			
			if (!super.legalMove(cg, ix, iy, fx, fy))
				return false;
			
			
			
			if(moved){
				switch (direction) {
				case up:
					return (ix == fx && fy == iy + 1);
				case down:
					return (ix == fx && fy == iy - 1);
				case left:
					return (iy == fy && fx == ix - 1);
				}
	
				return (iy == fy && fx == ix + 1); // must be right
			}
			else{
				switch (direction) {
				case up:
					return (ix == fx && ((fy == iy + 2)||(fy == iy + 1)));
				case down:
					return (ix == fx && ((fy == iy - 2)||(fy == iy - 1)));
				case left:
					return (iy == fy && ((fx == ix - 2)||(fy == iy - 1)));
				}
	
				return (iy == fy && ((fx == ix + 2)||(fy == iy +1))); // must be right
			}
			
		}

		public void move(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {
			moved = true;
			super.move(cg, ix, iy, fx, fy);
		}
	}

	public static class Knight extends ChessPiece {

		public Knight(String c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {
			
			if (!super.legalMove(cg, ix, iy, fx, fy))
				return false;
			
			
			if(!(((fx == ix - 2) || (fx == ix + 2)) && ((fy == iy - 1) || (fy == iy + 1)))
					|| (((fx == ix - 1) || (fx == ix + 1)) && ((fy == iy - 2) || (fy == iy + 2))))
				return false;
			
			return true;
		}

	}

	public static class Bishop extends ChessPiece {

		public Bishop(String c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {

			return false;
		}

	}

	public static class Rook extends ChessPiece {

		public Rook(String c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {
			// will take direction into account
			return false;
		}

	}

	public static class Queen extends ChessPiece {

		public Queen(String c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {
			// will take direction into account
			return false;
		}

	}

	public static class King extends ChessPiece {

		public King(String c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean legalMove(fourplayerChessGame cg, int ix, int iy, int fx, int fy) {
			// will take direction into account
			return false;
		}

		public boolean inCheck(fourplayerChessGame cg) {
			return false;
			// tests if THIS king is in check
		}

		public boolean isCheckMate(fourplayerChessGame cg) {
			return false;
			// checks if THIS king is in checkmate, not any king
		}

	}

}
