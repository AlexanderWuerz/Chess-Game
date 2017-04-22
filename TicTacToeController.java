
public class TicTacToeController {

	public static void main(String[] args) {
		TicTacToeClient mrX = new TicTacToeClient("localhost", 4455);
		TicTacToeClient mrO = new TicTacToeClient("localhost", 4455);
		
		TicTacToeServer server = new TicTacToeServer(4455);
		
		Thread a = new Thread(server);
		Thread b = new Thread(mrX);
		Thread c = new Thread(mrO);
		
		a.start();
		b.start();
		c.start();

	}

}
