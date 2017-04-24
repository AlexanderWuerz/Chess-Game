/* This code was taken from Yunming Zhang's Blog: 
 * <https://yunmingzhang.wordpress.com/2013/12/18/multithreaded-server-multiple-clients-to-a-single-server-programming-in-java/>
 * He posted a tutorial about modifying the EchoServer to accept multiple threads. 
 */

import java.net.*; 
import java.io.*; 
import java.lang.*; 

public class fourplayerClientWorker implements Runnable {
	private Socket client; 
	//private fourplayerChessGame cp;
	private BasicMPGame cp; 

	
	public fourplayerClientWorker(Socket client) {
		this.client = client;  
	}
	
	public void run() {
		String line; 
		BufferedReader in = null; 
		PrintWriter out = null; 
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); 
			out = new PrintWriter(client.getOutputStream(), true); 
		} catch (IOException e) {
			System.out.println("failed connection1"); 
			System.exit(-1); 
		}
		

		//while(true) {
			try {
		 		String inputLine, outputLine="";
		            // Initiate conversation with client
		            //TicTacToeProtocol ttp = new TicTacToeProtocol();
		            //outputLine = cp.processInput(null);
				/*	outputLine = cp.getMove(); 
		            out.println(outputLine);  */
					
					
		            while ((inputLine = cp.getMove()) != "bye") {
		               // outputLine = cp.processInput(inputLine);
						cp.sendMove(inputLine); 
		                out.println(outputLine);
		                if (outputLine.equals("Bye."))
		                    break; 
		            } 

			} catch (Exception e) {
				System.out.println("failed connection"); 
				System.exit(-1); 
			}
		//}
	}
	
}