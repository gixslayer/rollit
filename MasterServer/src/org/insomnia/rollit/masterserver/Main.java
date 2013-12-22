package org.insomnia.rollit.masterserver;

import java.io.IOException;

import org.insomnia.rollit.shared.network.MasterServer;
import org.insomnia.rollit.shared.network.MasterServerClient;
import org.insomnia.rollit.shared.network.MasterServerHandler;
import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.packets.PacketMasterServerResponse;
import org.insomnia.rollit.shared.network.packets.PacketRegister;
import org.insomnia.rollit.shared.network.packets.PacketValidateRegistration;

public class Main implements MasterServerHandler {
	public static final int SERVER_PORT = 6969;

	private final MasterServer server;
	private final RegistrationManager registrationManager;

	public Main() {
		this.server = new MasterServer(this);
		this.registrationManager = new RegistrationManager();
	}

	public boolean loadData() {
		return registrationManager.loadData();
	}

	public void startListening() {
		server.startListening(SERVER_PORT);

		sysout("Press enter to exit");

		// Wait for user input.
		try {
			System.in.read();
		} catch (IOException e) {
			// Ignore exception.
			e.equals(null);
		} finally { // Finally force the server to stop listening.
			server.stopListening();
		}
	}

	private synchronized void sysout(String message) {
		System.out.println(message);
	}

	private synchronized void syserr(String message) {
		System.err.println(message);
	}

	private void handlePacket(MasterServerClient client, PacketRegister packet) {
		String name = packet.getName();
		byte[] hash = packet.getHash();

		if (!registrationManager.isNameValid(name)) {
			sysout("Failed to register client: " + name + " (invalid name)");

			client.send(PacketMasterServerResponse.REG_NAME_INVALID);
		} else if (registrationManager.isRegistered(name)) {
			sysout("Failed to register client: " + name + " (name taken)");

			client.send(PacketMasterServerResponse.REG_NAME_TAKEN);
		} else {
			registrationManager.register(name, hash);

			sysout("Registered new client: " + name);

			client.send(PacketMasterServerResponse.SUCCESS);
		}
	}

	private void handlePacket(MasterServerClient client, PacketValidateRegistration packet) {
		String name = packet.getName();
		byte[] hash = packet.getHash();

		if (registrationManager.validate(name, hash)) {
			sysout("Valiadation for client " + name + " succeeded");

			client.send(PacketMasterServerResponse.SUCCESS);
		} else {
			sysout("Valiadation for client " + name + " failed");

			client.send(PacketMasterServerResponse.VAL_FAILED);
		}
	}

	// ////// IServerHandler implementation
	public void listening(int port) {
		sysout("Started listening on port " + port);
	}

	public void listenFailed(int port) {
		syserr("Could not start listening on port " + port);
	}

	public void stopped(String reason) {
		sysout("Stopped listening: " + reason);

		registrationManager.saveData();
	}

	public void packetReceived(MasterServerClient client, Packet packet) {
		if (packet instanceof PacketRegister) {
			handlePacket(client, (PacketRegister) packet);
		} else if (packet instanceof PacketValidateRegistration) {
			handlePacket(client, (PacketValidateRegistration) packet);
		} else {
			packetDropped("Unkown packet type");
		}
	}

	public void packetDropped(String reason) {
		syserr("Dropped packet: " + reason);
	}

	public void packetSendFailed(String reason) {
		syserr("Failed to send packet: " + reason);
	}

	// ////// Entry point
	public static void main(String[] args) {
		Main main = new Main();

		if (main.loadData()) {
			main.startListening();
		} else {
			System.err.println("Could not load registration data");
		}
	}

}
