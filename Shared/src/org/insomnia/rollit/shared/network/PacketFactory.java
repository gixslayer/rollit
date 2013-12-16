package org.insomnia.rollit.shared.network;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.insomnia.rollit.shared.network.packets.PacketMasterServerResponse;
import org.insomnia.rollit.shared.network.packets.PacketRaw;
import org.insomnia.rollit.shared.network.packets.PacketRegister;
import org.insomnia.rollit.shared.network.packets.PacketValidateRegistration;

/**
 * A <code>Packet</code> factory that will produce new <code>Packet</code> instances corresponding
 * to a given <code>PacketType</code>.
 * 
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
		registerPacket(PacketType.Raw, PacketRaw.class);
		registerPacket(PacketType.Register, PacketRegister.class);
		registerPacket(PacketType.ValidateRegistration, PacketValidateRegistration.class);
		registerPacket(PacketType.MasterServerResponse, PacketMasterServerResponse.class);
	}

	/**
	 * Registers a packet class to a packet type so the factory can create a new instance of this
	 * packet class.
	 * 
	 * @param type The <code>PacketType</code> that belongs to the packet.
	 * @param packetClass The class of the packet.
	 * @throws IllegalArgumentException If the packet class did not meet the requirements given by
	 * the factory or the <code>PacketType</code> is already registered.
	 */
	private static void registerPacket(PacketType type, Class<? extends Packet> packetClass) {
		if (!isAcceptablePacketClass(packetClass)) {
			throw new IllegalArgumentException("Class " + packetClass
					+ " did not contain a public constructor with 0 arguments");
		}
		if (PACKET_MAPPING.containsKey(type)) {
			throw new IllegalArgumentException("Packet type " + type + " is already registered");
		}

		PACKET_MAPPING.put(type, packetClass);
	}

	/**
	 * Checks whether a packet class meets the requirements given by the packet factory. A packet
	 * class must have a public constructor with 0 arguments.
	 * 
	 * @param packetClass The packet class to perform the check on.
	 */
	private static boolean isAcceptablePacketClass(Class<? extends Packet> packetClass) {
		boolean result = false;

		if (!Modifier.isAbstract(packetClass.getModifiers())) {
			for (Constructor<?> con : packetClass.getConstructors()) {
				if (con.getParameterTypes().length == 0 && Modifier.isPublic(con.getModifiers())) {
					result = true;
				}
			}
		}

		return result;
	}

	/**
	 * Produces a new <code>Packet</code> instance that corresponds to the given
	 * <code>PacketType</code>. If the <code>PacketType</code> could not be matched to a
	 * <code>Packet</code> class an <code>IllegalArgumentException</code> is thrown.
	 * 
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
