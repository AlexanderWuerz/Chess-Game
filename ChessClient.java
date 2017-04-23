
/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.io.*;
import java.net.*;
import java.util.Scanner;

class ChessClient implements Runnable {
	public FPGame cg;// = new basicFPGame();
	private String hostName;
	private int portNumber;

	public ChessClient(String host, int port) {
		hostName = host;
		portNumber = port;
	}

	@Override
	public void run() {
		try (Socket tttSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(tttSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(tttSocket.getInputStream()));) {
			Scanner sc = new Scanner(System.in);
			String move;
			int playerNum = Integer.parseInt(in.readLine());
			cg = new basicFPGame(playerNum);

			while (!cg.isOver()) {
				for (int i = 0; i < 4; i++) {
					if (i == playerNum) {

						System.out.println(playerNum + " getting move.");
						move = cg.getMove();
						out.println(move);
						System.out.println(playerNum + " sent move.");
					}
					System.out.println(playerNum + " waiting on server");
					move = in.readLine();
					cg.sendMove(move);
					System.out.println(playerNum + " got move.");

				}
			}
			System.out.println("game over!");
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			System.err.println("Usage: java ChessClient <host name> <port number>");
			System.exit(1);
		}

		(new Thread(new ChessClient(args[0], Integer.parseInt(args[1])))).start();
	}

}