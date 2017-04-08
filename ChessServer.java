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

import java.net.*;
import java.io.*;

public class ChessServer { 
	
//public static String[][] board = new String[8][8];
//public static ChessGame cg = new ChessGame();
public static fourplayerChessGame cg = new fourplayerChessGame(); 
public static String[][] board = cg.board; 

	public static void main(String[] args) throws IOException {
		
		if (args.length != 1) {
		            System.err.println("Usage: java ChessServer <port number>");
		            System.exit(1);
		        }

		        int portNumber = Integer.parseInt(args[0]);
				ServerSocket serverSocket = null;
				
				try {
					serverSocket = new ServerSocket(portNumber); 
				} catch (IOException e) {
					System.out.println("Exception caught when trying to listen on port "
					                + portNumber + " or listening for a connection");
					            System.out.println(e.getMessage());
				} 
				
				ClientWorker w1; 
							ClientWorker w2; 
							try {
								// server.accept() returns a client connection
								w1 = new ClientWorker(serverSocket.accept(),cg); 
								w2 = new ClientWorker(serverSocket.accept(),cg); 
								Thread t1 = new Thread(w1); 
								Thread t2 = new Thread(w2); 
								
								t1.start(); 
								t2.start(); 
							} catch (IOException e) {
								System.out.println("Accept failed"); 
								System.exit(-1); 
							}

//	ChessGame cg = new ChessGame(); 
 	//SetTestBoard();  
 	//cg.SetInitBoard(); 
	//SetOpeningBoard(); 
 	}
}