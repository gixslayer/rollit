package org.insomnia.rollit.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.insomnia.rollit.shared.LogLevel;
import org.insomnia.rollit.shared.Logger;
import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.Server;
import org.insomnia.rollit.shared.network.ServerHandler;

public final class Main implements ServerHandler {
	public static final Scanner SCANNER = new Scanner(System.in);
	public static final int MIN_PORT = 0;
	public static final int MAX_PORT = 65536;

	private static Main instance = null;

	public static synchronized Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}

		return instance;
	}

	private final Server server;
	private final Set<NetworkHandler> networkHandlers;
	private RoomHandler roomHandler;
	private PlayerHandler playerHandler;

	private Main() {
		this.server = new Server(this);
		this.networkHandlers = new HashSet<NetworkHandler>();

		Logger.attachStream(System.out);
		Logger.setLogLevel(LogLevel.Low);
	}

	public synchronized <T extends NetworkHandler> T attachNetworkHandler(T handler) {
		networkHandlers.add(handler);

		return handler;
	}

	public synchronized void detachNetworkHandler(NetworkHandler handler) {
		networkHandlers.remove(handler);
	}

	public void initializeHandlers() {
		// The reason this is split up and not executed directly in the constructor is because the
		// static Main instance is not set until the constructor exits. The problem is that the
		// NetworkHandler constructor calls Main.getInstance() which will invoke the Main
		// constructor again causing an infinite loop which results into a stack overflow.
		// This method should always be called after the static Main instance is assigned.
		this.roomHandler = attachNetworkHandler(new RoomHandler());
		this.playerHandler = attachNetworkHandler(new PlayerHandler());
	}

	public void startListening() {
		// Scanner scanner = new Scanner(System.in);

		while (!server.isListening()) {
			System.out.print("Please enter a port to start listening on: ");

			String line = SCANNER.nextLine();

			try {
				int port = Integer.parseInt(line);

				if (port >= MIN_PORT && port < MAX_PORT) {
					server.startListening(port);
				} else {
					System.err.println("Invalid port number " + port + " (must be between "
							+ MIN_PORT + " and " + MAX_PORT + ")");
				}
			} catch (NumberFormatException e) {
				System.err.println("Could not parse input as integer");
			}
		}

		// Closing the scanner here will also close System.in which will cause issues in the entry
		// point. Using a constant instance of Scanner now that will last during the entire
		// application lifetime and therefore will not close System.in.
		// scanner.close();
	}

	public void stopListening() {
		server.close();
	}

	public Server getServer() {
		return server;
	}

	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

	public RoomHandler getRoomHandler() {
		return roomHandler;
	}

	// ////// ServerHandler callbacks
	public void listening(int port) {
		Logger.println(LogLevel.Low, "Server started listening on port " + port);
	}

	public void listenFailed(int port) {
		Logger.println(LogLevel.Low, "Failed to start listening on port " + port);
	}

	public void stopped(String reason) {
		Logger.println(LogLevel.Low, "Server stopped listening: " + reason);
	}

	public void clientConnected(int clientId) {
		Logger.println(LogLevel.Medium, "Client connected (clientId=" + clientId + ")");
	}

	public void clientRefused(String reason) {
		Logger.println(LogLevel.Medium, "Client refused: " + reason);
	}

	public void clientDisconnected(int clientId) {
		Logger.println(LogLevel.Medium, "Client disconnected (clientId=" + clientId + ")");

		playerHandler.unregisterPlayer(clientId);
	}

	public void packetReceived(int clientId, Packet packet) {
		Logger.println(LogLevel.High, "Received packet " + packet.getType() + " from client "
				+ clientId);

		for (NetworkHandler handler : networkHandlers) {
			handler.handlePacket(clientId, packet);
		}
	}

	public void packetDropped(int clientId, String reason) {
		Logger.println(LogLevel.Low, "Dropped packet for client " + clientId + " (" + reason + ")");
	}

	public void packetSend(int clientId, Packet packet) {
		Logger.println(LogLevel.High, "Send packet " + packet.getType() + " to client " + clientId);
	}

	public void packetSendFailed(int clientId, Packet packet, String reason) {
		Logger.println(LogLevel.Low, "Failed to send packet to client " + clientId + " (" + reason
				+ ")");
	}

	// ////// Entry point
	public static void main(String[] args) {
		// Get the instance of Main (should not exist at this point so it should be created now).
		Main main = Main.getInstance();

		// Create and attach the network handlers.
		main.initializeHandlers();

		// Start the server.
		main.startListening();

		// Print a message and wait for user input.
		System.out.println("Press ENTER to exit");

		try {
			System.in.read();
		} catch (IOException e) {
			System.err.println("IOException during System.in.read() -> " + e.getMessage());
		}

		// Force the server to shutdown.
		main.stopListening();
	}
}
