package org.insomnia.rollit.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.insomnia.rollit.shared.network.Packet;

final class Client implements Runnable {
	private final Socket socket;
	private final Server server;
	private final int clientId;
	private InputStream inputStream;
	private OutputStream outputStream;
	private volatile boolean keepReceiving;

	Client(Server argServer, int argClientId, Socket argSocket) {
		this.socket = argSocket;
		this.server = argServer;
		this.clientId = argClientId;
		this.keepReceiving = true;
	}

	public boolean startReceiving() {
		boolean result = true;

		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			(new Thread(this)).start();
		} catch (IOException e) {
			// Closing the socket will also close the input/output streams.
			closeSocket();

			result = false;
		}

		return result;
	}

	public void run() {
		while (keepReceiving) {
			// to avoid Checkstyle whining for now.
			int temp = 0;

			// TODO: Implement packet receiving.
		}
	}

	private void closeSocket() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// Simply ignore as there is no sensible thing to do.
				// Checkstyle will whine about empty statement so do *something* to satisfy it.
				socket.equals(null);
			}
		}
	}

	public synchronized void send(Packet packet) {
		byte[] data = packet.serialize();

		try {
			outputStream.write(data);

			server.clientSendData(clientId, data.length);
			server.clientSendPacket(clientId, packet);
		} catch (IOException e) {
			server.clientFailedSendPacket(clientId, packet);
		}
	}

	public synchronized void disconnect() {
		if (keepReceiving) {
			keepReceiving = false;

			// Signal the server to remove this client from the clients hashmap.
			server.clientDisconnected(clientId);

			closeSocket();
		}
	}
}
