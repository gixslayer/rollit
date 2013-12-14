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
	Raw((byte) 0),
	/**
	 * A packet that is send when a client wants to connect to the server. It will initially put the
	 * client in the lobby and assign a name to the client.
	 */
	Connect((byte) 1),
	/**
	 * A packet that is send when a client wants to register to the master server.
	 */
	Register((byte) 2),
	/**
	 * A packet that is send from the master server which contains the response to a query.
	 */
	MasterServerResponse((byte) 3),
	/**
	 * A packet that is send when a server wants to validate the client's credentials with the
	 * master server.
	 */
	ValidateRegistration((byte) 4);

	private byte type;

	PacketType(byte argType) {
		this.type = argType;
	}

	/**
	 * Returns the unique value of the type as a <code>byte</code>.
	 */
	public byte getType() {
		return type;
	}

	// ////// byte to PacketType conversion
	// Allows for easy, safe, fast and no maintenance conversion from a byte to the corresponding
	// PacketType
	// All PacketType enum values are expected to have a unique value. If this is not the case
	// duplicate values will overwrite and can never be converted to.
	// Jml not accepting legal Java syntax, who makes this shit.
	private static final Map<Byte, PacketType> TYPE_MAPPING = new HashMap<Byte, PacketType>();
	// HashMap<>();

	// Build the mapping table.
	static {
		for (PacketType packetType : PacketType.values()) {
			TYPE_MAPPING.put(packetType.getType(), packetType);
		}
	}

	/**
	 * Converts a <code>Byte</code> to the corresponding <code>PacketType</code>. If no matching
	 * <code>PacketType</code> could be found, an <code>IllegalArgumentException</code> is thrown.
	 * @param type The <code>Byte</code> value of the packet type (as returned by
	 * <code>getType()</code>).
	 * @return The corresponding <code>PacketType</code>.
	 */
	public static PacketType fromByte(Byte type) {
		if (!TYPE_MAPPING.containsKey(type)) {
			throw new IllegalArgumentException("Unkown packet type: " + type);
		}

		return TYPE_MAPPING.get(type);
	}
}
