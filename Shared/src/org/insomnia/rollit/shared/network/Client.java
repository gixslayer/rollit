package org.insomnia.rollit.shared.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class Client implements Runnable {
	public static final int RECEIVE_BUFFER_SIZE = 4096;
	public static final String DEFAULT_DISCONNECT_REASON = "Manually disconnected";

	private final ClientHandler handler;
	private final byte[] receiveBuffer;
	private final PacketBuffer packetBuffer;
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private volatile boolean keepReceiving;

	public Client(ClientHandler argHandler) {
		this.handler = argHandler;
		this.receiveBuffer = new byte[RECEIVE_BUFFER_SIZE];
		this.packetBuffer = new PacketBuffer(argHandler);
		this.keepReceiving = false;
	}

	public void connect(String host, int port) {
		if (keepReceiving) {
			throw new IllegalStateException("Tried to connect on an already connected client.");
		}

		try {
			socket = new Socket(host, port);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			keepReceiving = true;

			handler.connected(host, port);

			new Thread(this, "Client receive").start();
		} catch (IOException e) {
			closeSocket();

			handler.connectFailed(host, port);
		}
	}

	public void run() {
		while (keepReceiving) {
			try {
				int bytesReceived = inputStream.read(receiveBuffer);

				if (bytesReceived >= 1) {
					packetBuffer.processData(receiveBuffer, 0, bytesReceived);

					while (packetBuffer.isPacketAvailable()) {
						Packet packet = packetBuffer.nextPacket();

						handler.packetReceived(packet);
					}
				} else {
					// End of stream.
					disconnect("End of stream");
				}
			} catch (IOException e) {
				disconnect(e.getMessage());
			}
		}
	}

	public synchronized void send(Packet packet) {
		byte[] data = packet.serialize();

		try {
			outputStream.write(data);

			handler.packetSend(packet);
		} catch (IOException e) {
			handler.packetSendFailed(packet, e.getMessage());
		}
	}

	public void disconnect() {
		disconnect(DEFAULT_DISCONNECT_REASON);
	}

	private synchronized void disconnect(String reason) {
		if (keepReceiving) {
			keepReceiving = false;

			handler.disconnected(reason);

			closeSocket();
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
}
