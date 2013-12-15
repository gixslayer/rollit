package org.insomnia.rollit.client;

import java.io.IOException;

import org.insomnia.rollit.shared.network.Client;
import org.insomnia.rollit.shared.network.ClientHandler;
import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.PacketMasterServerResponse;
import org.insomnia.rollit.shared.network.PacketValidateRegistration;

public class Main implements ClientHandler {
	private final Client client;

	public Main() {
		this.client = new Client(this);
	}

	public void connect(String host, int port) {
		client.connect(host, port);
	}

	public void disconnect() {
		client.disconnect();
	}

	// ////// ClientHandler
	public void packetDropped(String reason) {
		System.out.println("Packet dropped: " + reason);
	}

	public void connected(String host, int port) {
		System.out.println("Connected to " + host + ":" + port);

		// client.send(new PacketRegister("test", new byte[16]));
		client.send(new PacketValidateRegistration("test", new byte[16]));
	}

	public void connectFailed(String host, int port) {
		System.out.println("Failed to connect to " + host + ":" + port);
	}

	public void disconnected(String reason) {
		System.out.println("Disconnected: " + reason);
	}

	public void packetReceived(Packet packet) {
		if (packet instanceof PacketMasterServerResponse) {
			PacketMasterServerResponse response = (PacketMasterServerResponse) packet;

			System.out.println("Master server response: " + response.getResponse());
		} else {
			System.out.println("Unexpected packet type " + packet.getType());
		}
	}

	public void packetSend(Packet packet) {
		System.out.println("Send packet " + packet.getType());
	}

	public void packetSendFailed(Packet packet, String reason) {
		System.out.println("Failed to send packet " + packet.getType() + ": " + reason);
	}

	// ////// Entry point
	public static void main(String[] args) {
		Main main = new Main();

		main.connect("127.0.0.1", 6969);

		System.out.println("Press enter to exit");

		// Wait for user input.
		try {
			System.in.read();
		} catch (IOException e) {
			// Ignore exception.
			e.equals(null);
		} finally { // Finally force the server to stop listening.
			main.disconnect();
		}
	}
}
