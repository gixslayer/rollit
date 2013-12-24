package org.insomnia.rollit.shared.network;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Constructs packets from raw binary data that can be fed in arbitrary sized chunks.
 * 
 * @author Ciske
 * 
 */
final class PacketBuffer {
	private final PacketBufferHandler handler;
	private final List<Packet> completedPackets;
	private byte[] currentPacket;
	private int currentOffset;
	private byte[] preBuffer;
	private int bytesToReceive;

	/**
	 * Creates a new instance of the <code>PacketBuffer</code>.
	 * 
	 * @param argHandler The handler that provides the callback methods.
	 */
	PacketBuffer(PacketBufferHandler argHandler) {
		this.handler = argHandler;
		this.completedPackets = new LinkedList<Packet>();
		this.bytesToReceive = 0;
	}

	/**
	 * Processes an arbitrary size of binary data.
	 * 
	 * @param buffer The buffer to read the binary data from.
	 * @param offset The offset within the buffer to start reading from.
	 * @param length The amount of bytes to process.
	 */
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
		// Perhaps enforce a maximum packet size as currently malicious clients can force massive
		// amounts of memory allocation on the server by sending insanely large packet sizes.
		if (length >= Packet.PACKET_LENGTH_SIZE) {
			bytesToReceive = PacketUtils.toInt(data, offset);

			if (bytesToReceive >= Packet.PACKET_TYPE_SIZE) {
				currentPacket = new byte[bytesToReceive];
				currentOffset = 0;

				if (length > Packet.PACKET_LENGTH_SIZE) {
					processPacket(data, offset + Packet.PACKET_LENGTH_SIZE, length
							- Packet.PACKET_LENGTH_SIZE);
				}
			} else {
				handler.packetDropped("Invalid packet length: " + bytesToReceive);

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
			handler.packetDropped(e.getMessage());
		}
	}

	/**
	 * Returns whether a packet has been successfully constructed and is currently available.
	 */
	public boolean isPacketAvailable() {
		return completedPackets.size() > 0;
	}

	/**
	 * Returns the next available packet.
	 * 
	 * @throws IllegalStateException If no packets are currently available.
	 */
	public Packet nextPacket() {
		if (!isPacketAvailable()) {
			throw new IllegalStateException("No packets are currently available");
		}

		return completedPackets.remove(0);
	}
}
