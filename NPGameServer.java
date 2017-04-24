
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

public class NPGameServer implements Runnable {


	int portNumber;

	NPGame cg;
	
	public NPGameServer(int port, NPGame g) {
		cg = g;
		portNumber = port;
	}

	public void run() {
		
		
		try  {
			ServerSocket serverSocket = new ServerSocket(portNumber);
			fpClientInterface[] cis = new fpClientInterface[cg.n];
			for(int i=0; i<cis.length; i++){
				cis[i] = new fpClientInterface(serverSocket);
				cis[i].out.println(i);
				System.out.println("Accepted Client"+ i);
			}
			
			//main game loop
			
			Thread.sleep(1000);
			
			while(!cg.isOver()){
				for(int i=0; i<cg.n;i++){
					System.out.println("getting move from player "+i);
					String move = cis[i].in.readLine();
					System.out.println("got move from player "+i);
					cg.sendMove(move);
					Thread.sleep(100);
					
					for(int j=0;j<cg.n;j++){
						System.out.println("sending move to player "+j);
						cis[j].out.println(move);
						Thread.sleep(100);
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

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Usage: java ChessServer <port number>");
			System.exit(1);
		}
		(new Thread(new NPGameServer(Integer.parseInt(args[0]), new fourplayerChessGame(0)))).start();
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