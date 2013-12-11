package org.insomnia.rollit.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.insomnia.rollit.shared.network.Packet;

final class Client implements Runnable {
	public static final int RECEIVE_BUFFER_SIZE = 4096;

	private final Socket socket;
	private final Server server;
	private final int clientId;
	private final byte[] receiveBuffer;
	private final PacketBuffer packetBuffer;
	private InputStream inputStream;
	private OutputStream outputStream;
	private volatile boolean keepReceiving;

	Client(Server argServer, int argClientId, Socket argSocket) {
		this.socket = argSocket;
		this.server = argServer;
		this.clientId = argClientId;
		this.receiveBuffer = new byte[RECEIVE_BUFFER_SIZE];
		this.packetBuffer = new PacketBuffer(argClientId, argServer);
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
			try {
				int bytesReceived = inputStream.read(receiveBuffer);

				if (bytesReceived >= 1) {
					server.clientReceivedData(clientId, bytesReceived);

					packetBuffer.processData(receiveBuffer, 0, bytesReceived);

					while (packetBuffer.isPacketAvailable()) {
						Packet packet = packetBuffer.nextPacket();

						server.clientReceivedPacket(clientId, packet);
					}
				} else {
					// End of stream.
					disconnect();
				}
			} catch (IOException e) {
				disconnect();
			}
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
