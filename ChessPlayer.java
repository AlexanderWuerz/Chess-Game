import java.util.ArrayList;
import java.util.Scanner;


public class ChessPlayer {
	ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
	fourplayerChessGame cg;
	String playerName;
	
	public ChessPlayer(fourplayerChessGame c){
		cg=c;
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
	
	public static void main(String[] args) throws InterruptedException{
		System.out.println("working");
		
		(new Thread(new fourplayerChessServer(4455))).start();
		
		for(int i=0; i<4;i++){
			(new Thread(new ChessClient("localhost",4455))).start();
			Thread.sleep(100);
		}
		
		
		
//		a.start();
		
	}
}
