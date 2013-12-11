package org.insomnia.rollit.server.network;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.PacketFormatException;
import org.insomnia.rollit.shared.network.PacketUtils;

final class PacketBuffer {
	public static final int PACKET_LENGTH_SIZE = PacketUtils.getSize((int) 0);
	public static final int MIN_PACKET_LENGTH = Packet.PACKET_HEADER_SIZE;

	private final int clientId;
	private final Server server;
	private final List<Packet> completedPackets;
	private byte[] currentPacket;
	private int currentOffset;
	private byte[] preBuffer;
	private int bytesToReceive;

	PacketBuffer(int argClientId, Server argServer) {
		this.clientId = argClientId;
		this.server = argServer;
		this.completedPackets = new LinkedList<Packet>();
		this.bytesToReceive = 0;
	}

	public void processData(byte[] buffer, int offset, int length) {
		byte[] data;

		if (preBuffer != null) {
			data = new byte[preBuffer.length + length];

			System.arraycopy(preBuffer, 0, data, 0, preBuffer.length);
			System.arraycopy(buffer, offset, data, preBuffer.length, length);

			preBuffer = null;
		} else {
			data = Arrays.copyOfRange(buffer, offset, offset + length);
		}

		if (bytesToReceive == 0) {
			beginPacket(data, 0, data.length);
		}
	}

	private void beginPacket(byte[] data, int offset, int length) {
		if (length >= PACKET_LENGTH_SIZE) {
			bytesToReceive = PacketUtils.toInt(data, offset);

			if (bytesToReceive >= MIN_PACKET_LENGTH) {
				currentPacket = new byte[bytesToReceive];
				currentOffset = 0;

				if (length > PACKET_LENGTH_SIZE) {
					processPacket(data, offset + PACKET_LENGTH_SIZE, length - PACKET_LENGTH_SIZE);
				}
			} else {
				server.clientDroppedPacket(clientId, "Invalid packet length: " + bytesToReceive);

				bytesToReceive = 0;
			}
		} else {
			preBuffer = Arrays.copyOfRange(data, offset, offset + length);
		}
	}

	private void processPacket(byte[] data, int offset, int length) {
		int bytesToProcess = length > bytesToReceive ? bytesToReceive : length;

		System.arraycopy(data, offset, currentPacket, currentOffset, bytesToProcess);
		bytesToReceive -= bytesToProcess;
		currentOffset += bytesToProcess;

		if (bytesToReceive == 0) {
			finishPacket();
		}

		if (length > bytesToProcess) {
			beginPacket(data, offset + bytesToProcess, length - bytesToProcess);
		}
	}

	private void finishPacket() {
		try {
			Packet packet = Packet.deserialize(currentPacket);

			completedPackets.add(packet);
		} catch (PacketFormatException e) {
			server.clientDroppedPacket(clientId, e.getMessage());
		}
	}

	public boolean isPacketAvailable() {
		return completedPackets.size() > 0;
	}

	public Packet nextPacket() {
		return completedPackets.remove(0);
	}
}
