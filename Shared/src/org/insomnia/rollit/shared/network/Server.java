package org.insomnia.rollit.shared.network;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Server implements Runnable, Closeable {
	public static final int INVALID_CLIENT_ID = -1;
	public static final String DEFAULT_STOP_REASON = "Manually stopped listening";

	private final ServerHandler handler;
	private final Map<Integer, ServerClient> clients;
	private int lastClientId;
	private ServerSocket serverSocket;
	private volatile boolean keepListening;

	public Server(ServerHandler argHandler) {
		this.handler = argHandler;
		this.clients = new ConcurrentHashMap<Integer, ServerClient>();
		this.lastClientId = 0;
		this.keepListening = false;
	}

	public void run() {
		while (keepListening) {
			try {
				Socket socket = serverSocket.accept();
				int clientId = getNewClientId();

				if (clientId != INVALID_CLIENT_ID) {
					ServerClient client = new ServerClient(this, clientId, socket);

					if (client.startReceiving()) {
						clients.put(clientId, client);

						handler.clientConnected(clientId);
					} else {
						handler.clientRefused("Client could not start receiving");
					}
				} else {
					handler.clientRefused("Could not generate client id");
				}
			} catch (IOException e) {
				stopListening("IOException in listen thread: " + e.getMessage());
			}
		}
	}

	public void close() {
		disconnectAll();
		stopListening();

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

			handler.stopped(reason);
		}
	}

	private int getNewClientId() {
		int currentId = lastClientId;

		while (clients.containsKey(currentId) && currentId != INVALID_CLIENT_ID) {
			if (currentId == Integer.MAX_VALUE) {
				currentId = 0;
			} else {
				currentId++;

				if (currentId == lastClientId) {
					currentId = INVALID_CLIENT_ID;
				}
			}
		}

		if (currentId != INVALID_CLIENT_ID) {
			lastClientId = currentId;
		}

		return currentId;
	}

	// ////// Client callbacks
	protected synchronized void clientDisconnected(int clientId) {
		clients.remove(clientId);

		handler.clientDisconnected(clientId);
	}

	protected synchronized void clientSendPacket(int clientId, Packet packet) {
		handler.packetSend(clientId, packet);
	}

	protected synchronized void clientReceivedPacket(int clientId, Packet packet) {
		handler.packetReceived(clientId, packet);
	}

	protected synchronized void clientDroppedPacket(int clientId, String reason) {
		handler.packetDropped(clientId, reason);
	}

	protected synchronized void clientFailedSendPacket(int clientId, Packet packet, String reason) {
		handler.packetSendFailed(clientId, packet, reason);
	}

	// ////// User interaction
	public void startListening(int port) {
		if (keepListening) {
			// Split up to get better formatting.
			String message = "Tried to listen on a server that is already listening.";

			throw new IllegalStateException(message);
		}

		try {
			// Create server socket.
			this.serverSocket = new ServerSocket(port);
			this.keepListening = true;

			// Start the listen thread.
			new Thread(this, "Server listen").start();

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

	public boolean isClientConnected(int clientId) {
		return clients.containsKey(clientId);
	}

	public void send(int clientId, Packet packet) {
		if (isClientConnected(clientId)) {
			clients.get(clientId).send(packet);
		}
	}

	public void sendAll(Packet packet) {
		for (ServerClient client : clients.values()) {
			client.send(packet);
		}
	}

	public void disconnect(int clientId) {
		if (isClientConnected(clientId)) {
			clients.get(clientId).disconnect();
		}
	}

	public void disconnectAll() {
		Iterator<ServerClient> it = clients.values().iterator();

		while (it.hasNext()) {
			ServerClient client = it.next();

			client.disconnect();
		}
	}
}
