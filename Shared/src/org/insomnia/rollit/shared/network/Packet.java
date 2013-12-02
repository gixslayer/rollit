package org.insomnia.rollit.shared.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Provides an abstract base for packets that will be send between the server and client.
 * @author Ciske
 *
 */
public abstract class Packet {
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
		ByteBuffer buffer = ByteBuffer.allocate(5 + data.length);
		
		buffer.put(type.getType());
		buffer.putInt(data.length);
		buffer.put(data);
		
		return buffer.array();
	}
	
	/**
	 * Deserializes the array of bytes to a new <code>Packet</code> instance.
	 * @param serializedData The serialized data as received over the network.
	 * @return <code>null</code> if the data could not be deserialized. Otherwise a new instance of a class that extends <code>Packet</code> is returned which has all fields set.
	 */
	public static Packet deserialize(byte[] serializedData) {
		Packet result = null;
		
		if(serializedData.length >= 5) {
			try (ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedData); DataInputStream inputStream = new DataInputStream(byteStream)) {
				byte type = inputStream.readByte();
				PacketType packetType = PacketType.fromByte(type);
				
				if(packetType != null) {
					int dataLength = inputStream.readInt();
			
					if(dataLength >= 0) {
						if (5 + dataLength == serializedData.length) {
							byte[] data = Arrays.copyOfRange(serializedData, 5, serializedData.length);
					
							result = PacketFactory.createPacket(packetType);
							
							if(result != null) {
								result.deserializeData(data);
							} else {
								System.err.println("Could not create packet instance for packet type: " + packetType);
							}
						} else {	
							System.err.println("Invalid packet length (expected " + (5 + dataLength) + " received " + serializedData.length + ")");
						}
					} else {
						System.err.println("Packet data length cannot be negative: " + dataLength);
					}
				} else {
					System.err.println("Invalid packet type: " + type);
				}
			} catch(IOException e) {
				System.err.println("Could not read packet: " + e.getMessage());
			}
		}
		
		return result;
	}
	
	/**
	 * Serializes all fields into a single array of bytes.
	 */
	protected abstract byte[] serializeData();
	/**
	 * Deserializes the data and sets all fields to their corresponding value.
	 * @param data The serialized data as returned by <code>serializeData()</code>.
	 */
	protected abstract void deserializeData(byte[] data);
}
