package org.insomnia.rollit.shared.network;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class MasterServer implements Runnable, Closeable {
	public static final String DEFAULT_STOP_REASON = "Manually stopped listening";

	private final MasterServerHandler handler;
	private ServerSocket serverSocket;
	private volatile boolean keepListening;

	public MasterServer(MasterServerHandler argHandler) {
		this.handler = argHandler;
		this.keepListening = true;
	}

	public void run() {
		while (keepListening) {
			try {
				Socket socket = serverSocket.accept();
				MasterServerClient client = new MasterServerClient(socket, handler);

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

	private synchronized void stopListening(String reason) {
		// This method should only be executed once.
		if (keepListening) {
			keepListening = false;

			close();

			handler.stopped(reason);
		}
	}

	public void startListening(int port) {
		try {
			// Create server socket.
			this.serverSocket = new ServerSocket(port);

			// Start the listen thread.
			new Thread(this, "MasterServer listen").start();

			// Signal the handler the server started listening successfully.
			handler.listening(port);
		} catch (IOException e) {
			// Signal the handler the server could not start listening.
			handler.listenFailed(port);
		}
	}

	public void stopListening() {
		stopListening(DEFAULT_STOP_REASON);
	}
}
