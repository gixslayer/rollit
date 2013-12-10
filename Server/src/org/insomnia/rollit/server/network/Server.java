package org.insomnia.rollit.server.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public final class Server implements Runnable {
	private ServerSocket serverSocket;
	private volatile boolean keepRunning;

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		keepRunning = true;

		new Thread(this).start();
	}

	public void run() {
		while (keepRunning) {
			try {
				Socket socket = serverSocket.accept();
				OutputStream outStream = socket.getOutputStream();
				byte[] buffer = new byte[4096];

				System.out.println("Server: connection: " + socket.getInetAddress().toString());

				outStream.write(buffer, 0, 16);
				outStream.flush();
				outStream.write(buffer, 0, 14);
				outStream.flush();
				outStream.write(buffer, 0, 18);
				outStream.flush();
				outStream.write(buffer, 0, 12);
				outStream.flush();

				// socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("exit");
	}

	public void stop() {
		keepRunning = false;
	}
}
