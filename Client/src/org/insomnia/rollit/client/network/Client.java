package org.insomnia.rollit.client.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.PacketFormatException;

public final class Client implements Runnable {
	private final Socket socket;
	private final OutputStream outStream;
	private final InputStream inStream;
	private final byte[] buffer;
	private final IClientHandler handler;
	private volatile boolean keepRunning;
	private boolean disconnected;
	private int bytesToReceive;
	private byte[] currentPacket;
	private byte[] preBuffer;

	public Client(String host, int port, IClientHandler handler) throws UnknownHostException,
			IOException {
		this.socket = new Socket(host, port);
		this.outStream = socket.getOutputStream();
		this.inStream = socket.getInputStream();
		this.buffer = new byte[4096];
		this.handler = handler;
		this.keepRunning = true;
		this.disconnected = false;

		handler.connected(socket.getInetAddress().getHostAddress(), socket.getPort());

		new Thread(this).start();
	}

	public void run() {
		while (keepRunning) {
			try {
				int bytesReceived = inStream.read(buffer);

				if (bytesReceived == -1) {
					disconnect();
				} else {
					handler.dataReceived(bytesReceived);

					handleData(buffer, bytesReceived);
				}
			} catch (IOException e) {
				disconnect();
			}
		}
	}

	private void handleData(byte[] data, int bytesReceived) throws IOException {
		if (preBuffer != null) {
			byte[] newData = new byte[preBuffer.length + bytesReceived];

			System.arraycopy(preBuffer, 0, newData, 0, preBuffer.length);
			System.arraycopy(data, 0, newData, preBuffer.length, bytesReceived);

			preBuffer = null;

			handleData(newData, newData.length);
		} else {
			if (bytesToReceive == 0) {
				startPacket(data, bytesReceived);
			} else {
				if (bytesToReceive > bytesReceived) {
					processPartialPacket(data, bytesReceived);
				} else {
					int remainder = bytesReceived - bytesToReceive;
					int offset = bytesToReceive;

					finishPacket(data);

					if (remainder > 0) {
						byte[] newData = Arrays.copyOfRange(data, offset, data.length);

						handleData(newData, newData.length);
					}
				}
			}
		}
	}

	private void finishPacket(byte[] data) {
		processPartialPacket(data, bytesToReceive);

		Packet packet;
		try {
			packet = Packet.deserialize(currentPacket);

			handler.packetReceived(packet);
		} catch (PacketFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		currentPacket = null;
	}

	private void processPartialPacket(byte[] data, int bytesToProcess) {
		byte[] packetData = new byte[bytesToProcess + currentPacket.length];

		if (currentPacket.length > 0) {
			System.arraycopy(currentPacket, 0, packetData, 0, currentPacket.length);
		}
		System.arraycopy(data, 0, packetData, currentPacket.length, bytesToProcess);

		bytesToReceive -= bytesToProcess;
		currentPacket = packetData;
	}

	private void startPacket(byte[] data, int bytesToProcess) throws IOException {
		if (bytesToProcess < 4) {
			preBuffer = Arrays.copyOfRange(data, 0, bytesToProcess);
		} else {
			int length = ByteBuffer.wrap(data, 0, bytesToProcess).asIntBuffer().get();

			if (length < 1) {
				throw new IOException("Invalid packet length " + length);
			} else {
				currentPacket = new byte[0];
				bytesToReceive = length;

				if (bytesToProcess > 4) {
					byte[] newData = Arrays.copyOfRange(data, 4, bytesToProcess);

					handleData(newData, newData.length);
				}
			}
		}
	}

	public void send(Packet packet) {
		try {
			byte[] packetData = packet.serialize();

			outStream.write(packetData);
			outStream.flush();

			handler.dataSend(packetData.length);
			handler.packetSend(packet);
		} catch (IOException e) {
			disconnect();
		}
	}

	public void disconnect() {
		if (!disconnected) {
			keepRunning = false;

			try {
				socket.close();
			} catch (IOException e) {

			}

			handler.disconnected();

			disconnected = true;
		}
	}
}
