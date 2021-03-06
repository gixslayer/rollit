package org.insomnia.rollit.shared.network;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Provides an abstract base for packets that will be send between the server and client. All
 * classes that inherit this class must have a public empty constructor for the
 * <code>PacketFactory</code> to function correctly.
 * 
 * @author Ciske
 * 
 */
public abstract class Packet {
	/**
	 * The size of the packet length section in bytes.
	 */
	public static final int PACKET_LENGTH_SIZE = PacketUtils.getSize((int) 0);
	/**
	 * The size of the packet type section in bytes.
	 */
	public static final int PACKET_TYPE_SIZE = PacketUtils.getSize((byte) 0);
	/**
	 * The size of the entire packet header in bytes.
	 */
	public static final int PACKET_HEADER_SIZE = PACKET_LENGTH_SIZE + PACKET_TYPE_SIZE;

	private final PacketType type;

	protected Packet(PacketType argType) {
		this.type = argType;
	}

	/**
	 * Returns the type of the packet.
	 */
	public PacketType getType() {
		return type;
	}

	/**
	 * Serializes the packet to an array of bytes that can be send across the network.
	 */
	public byte[] serialize() {
		byte[] data = serializeData();

		ByteBuffer buffer = ByteBuffer.allocate(PACKET_HEADER_SIZE + data.length);

		buffer.putInt(data.length + PACKET_TYPE_SIZE);
		buffer.put(type.getType());

		if (data.length > 0) {
			buffer.put(data);
		}

		return buffer.array();
	}

	/**
	 * Deserializes the array of bytes to a new <code>Packet</code> instance.
	 * 
	 * @param serializedData The serialized data as received over the network.
	 * @return A new instance of a class that extends <code>Packet</code> which has all fields set
	 * according to the serialized data.
	 * @throws PacketFormatException If the packet could not be constructed and deserialized.
	 */
	public static Packet deserialize(byte[] serializedData) throws PacketFormatException {
		// The serialized data must contain at least one byte that specifies the packet type.
		if (serializedData.length < PACKET_TYPE_SIZE) {
			throw new PacketFormatException("Missing packet type");
		}

		try {
			// Create a packet instance of the correct type and extract the actual serialized data.
			PacketType packetType = PacketType.fromByte(serializedData[0]);
			Packet packet = PacketFactory.createPacket(packetType);
			// Will return a 0 sized array if serializedData.length == 1.
			byte[] data = Arrays.copyOfRange(serializedData, 1, serializedData.length);

			packet.deserializeData(data);

			return packet;
		} catch (IllegalArgumentException e) {
			throw new PacketFormatException(e.getMessage());
		} catch (ReflectiveOperationException e) {
			throw new PacketFormatException("Could not create packet instance: " + e.getMessage());
		} catch (PacketFormatException e) {
			throw e;
		}
	}

	/**
	 * Serializes all fields into a single array of bytes.
	 */
	protected abstract byte[] serializeData();

	/**
	 * Deserializes the data and sets all fields to their corresponding value.
	 * 
	 * @param data The serialized data as returned by <code>serializeData()</code>.
	 * @throws PacketFormatException If an error occurs while deserializing.
	 */
	protected abstract void deserializeData(byte[] data) throws PacketFormatException;
}
