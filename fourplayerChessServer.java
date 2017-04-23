
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

public class fourplayerChessServer implements Runnable {

	// public static String[][] board = new String[8][8];
	// public static ChessGame cg = new ChessGame();
	// public static fourplayerChessGame cg = new fourplayerChessGame();

	// public static ChessPiece[][] board = cg.board;
	private basicFPGame cp;

	int portNumber;

	public fourplayerChessServer(int port) {
		portNumber = port;
	}

	public void run() {
		
		FPGame cg = new basicFPGame(0);
		try  {
			ServerSocket serverSocket = new ServerSocket(portNumber);
			fpClientInterface[] cis = new fpClientInterface[4];
			for(int i=0; i<cis.length; i++){
				cis[i] = new fpClientInterface(serverSocket);
				cis[i].out.println(i);
			}
			
			//main game loop
			
			Thread.sleep(1000);
			
			while(!cg.isOver()){
				for(int i=0; i<4;i++){
					System.out.println("getting move from player "+i);
					String move = cis[i].in.readLine();
					System.out.println("got move from player "+i);
					cg.sendMove(move);
					
					
					for(int j=0;j<4;j++){
						System.out.println("sending move to player "+j);
						cis[j].out.print(move);
					}
				}
			}
			System.out.println("game over");
			
			



		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * fourplayerClientWorker w1; fourplayerClientWorker w2;
	 * fourplayerClientWorker w3; fourplayerClientWorker w4; try { //
	 * server.accept() returns a client connection w1 = new
	 * fourplayerClientWorker(serverSocket.accept()); w2 = new
	 * fourplayerClientWorker(serverSocket.accept()); w3 = new
	 * fourplayerClientWorker(serverSocket.accept()); w4 = new
	 * fourplayerClientWorker(serverSocket.accept()); Thread t1 = new
	 * Thread(w1); Thread t2 = new Thread(w2); Thread t3 = new Thread(w3);
	 * Thread t4 = new Thread(w4);
	 * 
	 * t1.start(); t2.start(); t3.start(); t4.start();
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Usage: java ChessServer <port number>");
			System.exit(1);
		}
		(new Thread(new fourplayerChessServer(Integer.parseInt(args[0])))).start();
	}

	private static class fpClientInterface {
		PrintWriter out;
		BufferedReader in;

		public fpClientInterface(ServerSocket serverSocket) throws IOException {
			Socket s = serverSocket.accept();
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}

	}
}