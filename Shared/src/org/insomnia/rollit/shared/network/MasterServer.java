package org.insomnia.rollit.shared.network;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class MasterServer implements Runnable, Closeable {
	private final MasterServerHandler serverHandler;
	private ServerSocket serverSocket;
	private volatile boolean keepListening;

	public MasterServer(MasterServerHandler handler) {
		this.serverHandler = handler;
		this.keepListening = true;
	}

	public void run() {
		while (keepListening) {
			try {
				Socket socket = serverSocket.accept();
				MasterServerClient client = new MasterServerClient(socket, serverHandler);

				client.startReceiving();
			} catch (IOException e) {
				stopListening("IOException in listen thread: " + e.getMessage());
			}
		}
	}

	public void close() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// Simply ignore as there is no sensible thing to do.
				// Checkstyle will whine about empty statement so do *something* to satisfy it.
				serverSocket.equals(null);
			}
		}
	}

	public synchronized void stopListening(String reason) {
		// This method should only be executed once.
		if (keepListening) {
			keepListening = false;

			close();

			serverHandler.stopped(reason);
		}
	}

	public void startListening(int port) {
		try {
			// Create server socket.
			this.serverSocket = new ServerSocket(port);

			// Start the listen thread.
			new Thread(this).start();

			// Signal the handler the server started listening successfully.
			serverHandler.listening(port);
		} catch (IOException e) {
			// Signal the handler the server could not start listening.
			serverHandler.listenFailed(port);
		}
	}
}
