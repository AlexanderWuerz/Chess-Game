import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TicTacToeServer implements Runnable {

	static String[] Xwins = { "XXX[OX-]{6}", "[OX-]{3}XXX[OX-]{3}", "[OX-]{6}XXX", "X[OX-]{3}X[OX-]{3}X",
			"[OX-]{2}X[OX-]X[OX-]X[OX-]{2}", "X[OX-]{2}X[OX-]{2}X[OX-]{2}", "[OX-]X[OX-][OX-]X[OX-][OX-]X[OX-]",
			"[OX-]{2}X[OX-]{2}X[OX-]{2}X" };

	static String[] Owins = { "OOO[XO-]{6}", "[XO-]{3}OOO[XO-]{3}", "[XO-]{6}OOO", "O[XO-]{3}O[XO-]{3}O",
			"[XO-]{2}O[XO-]O[XO-]O[XO-]{2}", "O[XO-]{2}O[XO-]{2}O[XO-]{2}", "[XO-]O[XO-][XO-]O[XO-][XO-]O[XO-]",
			"[XO-]{2}O[XO-]{2}O[XO-]{2}O" };
	int portNumber;

	public TicTacToeServer(int port) {
		portNumber = port;
	}

	public void run() {
		// TODO Auto-generated method stub
		// TODO Auto-generated constructor stub
		try (ServerSocket serverSocket = new ServerSocket(portNumber);

				Socket XSocket = serverSocket.accept();
				PrintWriter Xout = new PrintWriter(XSocket.getOutputStream(), true);
				BufferedReader Xin = new BufferedReader(new InputStreamReader(XSocket.getInputStream()));

				Socket OSocket = serverSocket.accept();
				PrintWriter Oout = new PrintWriter(OSocket.getOutputStream(), true);
				BufferedReader Oin = new BufferedReader(new InputStreamReader(OSocket.getInputStream()));)

		{
			char winner;
			String board = "---------";
			while ((winner = winner(board)) == '-') {
				String xres;
				Xout.println(board);
				do {
					xres = Xin.readLine();
					if (xres.length() > 1 || !Character.isDigit(xres.charAt(0))
							|| board.charAt(Integer.parseInt("" + xres.charAt(0))) != '-') {
						Xout.println("Inv");
					} else {
						Xout.println("OK");
						break;
					}
				} while (true);

				int spot = Integer.parseInt("" + xres.charAt(0));
				board = board.substring(0, spot) + 'X' + board.substring(spot + 1);

				if ((winner = winner(board)) != '-')
					break;

				String Ores;
				Oout.println(board);
				do {
					
					Ores = Oin.readLine();
					if (Ores.length() > 1 || !Character.isDigit(Ores.charAt(0))
							|| board.charAt(Integer.parseInt("" + Ores.charAt(0))) != '-') {
						Oout.println("Inv");
					} else {
						Oout.println("OK");
						break;
					}
				} while (true);

				spot = Integer.parseInt("" + Ores.charAt(0));
				board = board.substring(0, spot) + 'O' + board.substring(spot + 1);

			}

			String ending = winner == 'd' ? "Draw!" : winner + " wins!";
			Xout.println("end");
			Oout.println("end");

			Xout.println(board);
			Xout.println(ending);

			Oout.println(board);
			Oout.println(ending);

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trOing to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

	private char winner(String board) {
		for (String s : Xwins)
			if (board.matches(s))
				return 'X';
		for (String s : Owins)
			if (board.matches(s))
				return 'Y';
		boolean full = true;
		for (Character c : board.toCharArray())
			if (c == '-')
				full = false;
		if (full)
			return 'd';
		return '-';
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 1) {
			System.err.println("Usage: java KnockKnockServer <port number>");
			System.exit(1);
		}
		(new Thread(new TicTacToeServer(Integer.parseInt(args[0])))).start();
	}

}
