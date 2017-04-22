/*
 * Copyright (c) 1995, 2014, Oracle and/or its affiliates. All rights reserved.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class fourplayerChessServer implements Runnable{ 
	
//public static String[][] board = new String[8][8];
//public static ChessGame cg = new ChessGame();
//public static fourplayerChessGame cg = new fourplayerChessGame(); 

//public static ChessPiece[][] board = cg.board; 
private basicFPGame cp; 
	

		        int portNumber;
		public fourplayerChessServer(int port) {
			portNumber = port; 
		}
				
				
	public void run() {
				try (ServerSocket serverSocket = new ServerSocket(portNumber);
					Socket socket1 = serverSocket.accept(); 
					PrintWriter out1 = new PrintWriter(socket1.getOutputStream(),true); 
					BufferedReader in1 = new BufferedReader(new InputStreamReader
						(socket1.getInputStream())); 
					Socket socket2 = serverSocket.accept(); 
					PrintWriter out2 = new PrintWriter(socket2.getOutputStream(),true); 
					BufferedReader in2 = new BufferedReader(new InputStreamReader
						(socket2.getInputStream()));
					Socket socket3 = serverSocket.accept(); 
					PrintWriter out3 = new PrintWriter(socket3.getOutputStream(),true); 
					BufferedReader in3 = new BufferedReader(new InputStreamReader
						(socket3.getInputStream())); 
					Socket socket4 = serverSocket.accept(); 
					PrintWriter out4 = new PrintWriter(socket4.getOutputStream(),true); 
					BufferedReader in4 = new BufferedReader(new InputStreamReader
						(socket4.getInputStream()));
				 ){
					String res1,res2,res3,res4;
					
					//do {
						res1 = in1.readLine(); 
					//} while (true); 
					//do {
						res2 = in2.readLine(); 
					//} while (true); 
					//do {
						res3 = in3.readLine(); 
					//} while (true);
					//do {
						res4 = in4.readLine(); 
					//} while (true); 

					
					
				} catch (IOException e) {
					System.out.println("Exception caught when trying to listen on port "
					                + portNumber + " or listening for a connection");
					            System.out.println(e.getMessage());
				} 
	}
				
				/*
							fourplayerClientWorker w1; 
							fourplayerClientWorker w2;
							fourplayerClientWorker w3; 
							fourplayerClientWorker w4; 
							try {
								// server.accept() returns a client connection
								w1 = new fourplayerClientWorker(serverSocket.accept()); 
								w2 = new fourplayerClientWorker(serverSocket.accept()); 
								w3 = new fourplayerClientWorker(serverSocket.accept()); 
								w4 = new fourplayerClientWorker(serverSocket.accept()); 
								Thread t1 = new Thread(w1); 
								Thread t2 = new Thread(w2);
								Thread t3 = new Thread(w3); 
								Thread t4 = new Thread(w4); 
								
								t1.start(); 
								t2.start();
								t3.start(); 
								t4.start(); 
								*/
	public static void main(String[] args) throws IOException {
										
		if (args.length != 1) {
           	System.err.println("Usage: java ChessServer <port number>");
			System.exit(1);
		}
		(new Thread(new fourplayerChessServer(Integer.parseInt(args[0])))).start();
 	}
}