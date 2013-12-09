package org.insomnia.rollit.shared.network;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Provides an abstract base for packets that will be send between the server and client.
 * @author Ciske
 *
 */
public abstract class Packet {
	public static int PACKET_HEADER_SIZE = 5;
	
	private final PacketType type;
	
	Packet(PacketType type) {
		this.type = type;
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
		
		buffer.putInt(data.length + PACKET_HEADER_SIZE);
		buffer.put(type.getType());
		buffer.put(data);
		
		return buffer.array();
	}
	
	/**
	 * Deserializes the array of bytes to a new <code>Packet</code> instance.
	 * @param serializedData The serialized data as received over the network.
	 * @return A new instance of a class that extends <code>Packet</code> which has all fields set according to the serialized data.
	 * @throws PacketFormatException If the packet could not be constructed and deserialized.
	 */
	public static Packet deserialize(byte[] serializedData) throws PacketFormatException {
		// The serialized data must contain at least one byte that specifies the packet type.
		if(serializedData.length == 0) {
			throw new PacketFormatException("Missing packet type (length == 0)");
		}
		
		try {
			// Create a packet instance of the correct type and extract the actual serialized data.
			PacketType packetType = PacketType.fromByte(serializedData[0]);
			Packet packet = PacketFactory.createPacket(packetType);	
			byte[] data = Arrays.copyOfRange(serializedData, 1, serializedData.length); // Will return a 0 sized array if serializedData.length == 1.
					
			packet.deserializeData(data);
				
			return packet;
		} catch(IllegalArgumentException e) {
			throw new PacketFormatException(e.getMessage());
		} catch(ReflectiveOperationException e) {
			throw new PacketFormatException("Could not create packet instance: " + e.getMessage());
		} catch(PacketFormatException e) {
			throw e;
		}
	}
	
	/**
	 * Serializes all fields into a single array of bytes.
	 */
	protected abstract byte[] serializeData();
	
	/**
	 * Deserializes the data and sets all fields to their corresponding value.
	 * @param data The serialized data as returned by <code>serializeData()</code>.
	 * @throws PacketFormatException If an error occurs while deserializing.
	 */
	protected abstract void deserializeData(byte[] data) throws PacketFormatException;
}
