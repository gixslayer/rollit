package org.insomnia.rollit.shared.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class MasterServerClient implements Runnable {
	public static final int RECEIVE_BUFFER_SIZE = 4096;

	private final Socket socket;
	private final MasterServerHandler handler;
	private final byte[] receiveBuffer;
	private final PacketBuffer packetBuffer;
	private InputStream inputStream;
	private OutputStream outputStream;
	private volatile boolean keepReceiving;

	MasterServerClient(Socket argSocket, MasterServerHandler argHandler) {
		this.socket = argSocket;
		this.handler = argHandler;
		this.receiveBuffer = new byte[RECEIVE_BUFFER_SIZE];
		this.packetBuffer = new PacketBuffer(handler);
		this.keepReceiving = true;
	}

	public void startReceiving() {
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			(new Thread(this, "MasterServerClient " + System.currentTimeMillis())).start();
		} catch (IOException e) {
			// Closing the socket will also close the input/output streams.
			close();
		}
	}

	public void run() {
		while (keepReceiving) {
			try {
				int bytesReceived = inputStream.read(receiveBuffer);

				if (bytesReceived >= 1) {
					packetBuffer.processData(receiveBuffer, 0, bytesReceived);

					// Master server only responds to a 'query' so disconnect the client after the
					// first received packet is handled.
					if (packetBuffer.isPacketAvailable()) {
						Packet packet = packetBuffer.nextPacket();

						handler.packetReceived(this, packet);

						disconnect();
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

	public void send(int response) {
		Packet packet = new PacketMasterServerResponse(response);

		byte[] data = packet.serialize();

		try {
			outputStream.write(data);
		} catch (IOException e) {
			handler.packetSendFailed(e.getMessage());
		}
	}

	private void disconnect() {
		if (keepReceiving) {
			keepReceiving = false;

			close();
		}
	}

	private void close() {
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
