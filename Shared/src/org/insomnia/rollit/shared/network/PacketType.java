package org.insomnia.rollit.shared.network;

import java.util.HashMap;
import java.util.Map;

/**
 * The type of a packet.
 * @author Ciske
 *
 */
public enum PacketType {
	/**
	 * A raw packet which contains an array of bytes.
	 */
	Raw((byte)0);
	
	private byte type;
	
	PacketType(byte type) {
		this.type = type;
	}
	
	/**
	 * Returns the unique value of the type as a <code>byte</code>.
	 */
	public byte getType() {
		return type;
	}
	
	//////// byte to PacketType conversion
	// Allows for easy, safe, fast and no maintenance conversion from a byte to the corresponding PacketType
	// All PacketType enum values are expected to have a unique value. If this is not the case duplicate values will overwrite and can never be converted to.
	// Jml not accepting legal Java syntax, who makes this shit.
	private static final Map<Byte, PacketType> typeMapping = new HashMap<Byte, PacketType>(); //HashMap<>();
	
	/**
	 * Converts a <code>Byte</code> to the corresponding <code>PacketType</code>.
	 * @param type The <code>Byte</code> value of the packet type (as returned by <code>getType()</code>).
	 * @return <code>null</code> if the byte did not correspond to a packet type. Otherwise the corresponding <code>PacketType</code> is returned.
	 */
	public static PacketType fromByte(Byte type) {
		PacketType result = null;
		
		// Build the mapping table if it has not yet been build.
		if(typeMapping.size() == 0) {
			for(PacketType packetType : PacketType.values()) {
				typeMapping.put(packetType.getType(), packetType);
			}
		}
		
		if(typeMapping.containsKey(type)) {
			result = typeMapping.get(type);
		}
		
		return result;
	}
}
