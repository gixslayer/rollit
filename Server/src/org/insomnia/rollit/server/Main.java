package org.insomnia.rollit.server;

import java.util.Scanner;

import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.Server;
import org.insomnia.rollit.shared.network.ServerHandler;
import org.insomnia.rollit.shared.network.packets.PacketConnect;
import org.insomnia.rollit.shared.network.packets.PacketRaw;

public final class Main implements ServerHandler {
	private final Server server;
	private final Scanner scanner;
	private final RoomManager roomManager;
	private final PlayerManager playerManager;

	public Main() {
		this.server = new Server(this);
		this.scanner = new Scanner(System.in);
		this.roomManager = new RoomManager();
		this.playerManager = new PlayerManager();
	}

	public void startListening() {
		System.out.print("Please enter a port to start listening on: ");

		String line = scanner.nextLine();

		try {
			int port = Integer.valueOf(line).intValue();

			if (port >= 0 && port < 65536) {
				server.startListening(port);
			} else {
				// Split up println to get better formatting (prevents ugly line wrapping).
				String message = "Invalid port number " + port + " (must be between 0 and 65536)";

				System.out.println(message);

				startListening();
			}
		} catch (NumberFormatException e) {
			System.out.println("Could not parse input as integer");

			startListening();
		}
	}

	// ////// IServerHandler callbacks
	public void listening(int port) {
		System.out.println("Server started listening on port " + port);
	}

	public void listenFailed(int port) {
		System.out.println("Failed to start listening on port " + port);

		startListening();
	}

	public void stopped(String reason) {
		System.out.println("Server stopped listening: " + reason);
	}

	public void clientConnected(int clientId) {
		System.out.println("Client connected (clientId=" + clientId + ")");
	}

	public void clientRefused(String reason) {
		System.out.println("Client refused: " + reason);
	}

	public void clientDisconnected(int clientId) {
		System.out.println("Client disconnected (clientId=" + clientId + ")");
	}

	public void packetReceived(int clientId, Packet packet) {
		playerManager.handlePacket(clientId, packet);
		roomManager.handlePacket(clientId, packet);
	}

	public void packetDropped(int clientId, String reason) {
		System.out.println("Dropped packet for client " + clientId + " (" + reason + ")");

	}

	public void packetSend(int clientId, Packet packet) {
		// TODO Auto-generated method stub

	}

	public void packetSendFailed(int clientId, Packet packet, String reason) {
		System.out.println("Failed to send packet to client " + clientId + " (" + reason + ")");

	}

	public void dataReceived(int clientId, int bytes) {
		// TODO Auto-generated method stub

	}

	public void dataSend(int clientId, int bytes) {
		// TODO Auto-generated method stub

	}

	// ////// Entry point
	public static void main(String[] args) {
		// new Main().startListening();

		Main m = new Main();

		m.packetReceived(0, new PacketConnect("test"));
		m.packetReceived(0, new PacketRaw());
		m.packetReceived(0, new PacketRaw());
		m.packetReceived(0, new PacketConnect("testw"));
	}
}
