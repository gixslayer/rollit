package org.insomnia.rollit.server;

import org.insomnia.rollit.shared.network.Client;
import org.insomnia.rollit.shared.network.ClientHandler;
import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.packets.PacketMasterServerResponse;
import org.insomnia.rollit.shared.network.packets.PacketRegister;

public final class MasterServerQuery implements ClientHandler {
	public static final String MASTER_SERVER_HOST = "127.0.0.1";
	public static final int MASTER_SERVER_IP = 6969;
	public static final long DEFAULT_TIMEOUT = 10000;

	private Client client;
	private PacketRegister packetRegister;
	private volatile boolean result;
	private volatile Object lock;
	private boolean shouldLock;

	public boolean verifyRegistration(PacketRegister packet) {
		client = new Client(this);
		result = false;
		packetRegister = packet;
		shouldLock = true;
		lock = new Object();

		client.connect(MASTER_SERVER_HOST, MASTER_SERVER_IP);

		if (shouldLock) {
			try {
				lock.wait(DEFAULT_TIMEOUT);
			} catch (InterruptedException e) {
				e.equals(null);
			}
		}

		client.disconnect();

		return result;
	}

	public void packetDropped(String reason) {
		lock.notify();
	}

	public void connected(String host, int port) {
		client.send(packetRegister);
	}

	public void connectFailed(String host, int port) {
		shouldLock = false;
	}

	public void disconnected(String reason) {
		lock.notify();
	}

	public void packetReceived(Packet packet) {
		if (packet instanceof PacketMasterServerResponse) {
			int response = ((PacketMasterServerResponse) packet).getResponse();

			if (response == PacketMasterServerResponse.SUCCESS) {
				result = true;
			}
		}

		lock.notify();
	}

	public void packetSend(Packet packet) {
		shouldLock = true;
	}

	public void packetSendFailed(Packet packet, String reason) {
		shouldLock = false;
	}

}
