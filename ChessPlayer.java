import java.util.ArrayList;
import java.util.Scanner;


public class ChessPlayer {
	
	public static void main(String[] args) throws InterruptedException{
		System.out.println("working");
		
		(new Thread(new NPGameServer(4455, new fourplayerChessGame(0)))).start();
		
		for(int i=0; i<4;i++){
			(new Thread(new NPGameClient("localhost",4455, new fourplayerChessGame(0)))).start();
			Thread.sleep(100);
		}

	}
}
