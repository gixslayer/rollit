package org.insomnia.rollit.shared.network;

import java.util.HashMap;
import java.util.Map;

import org.insomnia.rollit.shared.network.packets.PacketMasterServerResponse;
import org.insomnia.rollit.shared.network.packets.PacketRaw;
import org.insomnia.rollit.shared.network.packets.PacketRegister;
import org.insomnia.rollit.shared.network.packets.PacketValidateRegistration;

/**
 * A <code>Packet</code> factory that will produce new <code>Packet</code> instances corresponding
 * to a given <code>PacketType</code>.
 * @author Ciske
 * 
 */
public final class PacketFactory {
	// Jml not accepting legal Java syntax, who makes this shit.
	private static final Map<PacketType, Class<? extends Packet>> PACKET_MAPPING =
			new HashMap<PacketType, Class<? extends Packet>>();
	// HashMap<>();

	static {
		// Register all classes that extend packet here so the factory knows about them.
		// Packets should never have the same packet type!
		PACKET_MAPPING.put(PacketType.Raw, PacketRaw.class);
		PACKET_MAPPING.put(PacketType.Register, PacketRegister.class);
		PACKET_MAPPING.put(PacketType.ValidateRegistration, PacketValidateRegistration.class);
		PACKET_MAPPING.put(PacketType.MasterServerResponse, PacketMasterServerResponse.class);
	}

	/**
	 * Produces a new <code>Packet</code> instance that corresponds to the given
	 * <code>PacketType</code>. If the <code>PacketType</code> could not be matched to a
	 * <code>Packet</code> class an <code>IllegalArgumentException</code> is thrown.
	 * @param type The type of packet to create.
	 * @return A new instance of a class that extends <code>Packet</code>.
	 * @throws ReflectiveOperationException If no new instance could be created.
	 */
	public static Packet createPacket(PacketType type) throws ReflectiveOperationException {
		if (!PACKET_MAPPING.containsKey(type)) {
			throw new IllegalArgumentException("Unkown packet type " + type);
		}

		try {
			return PACKET_MAPPING.get(type).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw e;
		}
	}
}
