import java.util.ArrayList;
import java.util.Scanner;


public class ChessPlayer {
	ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
	fourplayerChessGame cg;
	String playerName;
	
	public ChessPlayer(fourplayerChessGame c){
		cg=c;
		playerName = ""+c.players.size()+1;
	}
	
	boolean owns(ChessPiece c)	{
		return pieces.contains(c);
	}
	
	public String getMove(){
		System.out.println(playerName+" make your move.");
		
		
		return (new Scanner(System.in)).nextLine();
	}
	
	public void updateBoard(){
		
	}
}
