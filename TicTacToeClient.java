import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TicTacToeClient implements Runnable {
	String hostName;
	int portNumber;

	public TicTacToeClient(String host, int port) {
		hostName = host;
		portNumber = port;
	}

	@Override
	public void run() {
		try (Socket tttSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(tttSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(tttSocket.getInputStream()));) {
			Scanner sc = new Scanner(System.in);
			String fromServer;
			while (!tttSocket.isClosed()) {
				fromServer = in.readLine();
				if (fromServer.contains("end")) {
					displayBoard(in.readLine());
					System.out.println(in.readLine());
					tttSocket.close();
					break;
				}
				boolean passed = false;
				while (!passed) {
					displayBoard(fromServer);
					int move = sc.nextInt() - 1;
					out.println(move);
					if (!in.readLine().equals("Inv"))
						passed = true;
					else
						System.out.println("Invalid Move");
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}
	private static void displayBoard(String board) {
		char[] cells = board.toCharArray();
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == '-')
				cells[i] = (char) (49 + i);
		}
		System.out.println();
		System.out.println(cells[0] + " | " + cells[1] + " | " + cells[2]);
		System.out.println("--+---+--");
		System.out.println(cells[3] + " | " + cells[4] + " | " + cells[5]);
		System.out.println("--+---+--");
		System.out.println(cells[6] + " | " + cells[7] + " | " + cells[8]);
		System.out.println();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			System.err.println("Usage: java EchoClient <host name> <port number>");
			System.exit(1);
		}

		(new Thread(new TicTacToeClient(args[0], Integer.parseInt(args[1])))).start();
	}

}
