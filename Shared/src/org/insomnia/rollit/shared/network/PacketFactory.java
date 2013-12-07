package org.insomnia.rollit.shared.network;

import java.util.HashMap;
import java.util.Map;

/**
 * A <code>Packet</code> factory that will produce new <code>Packet</code> instances corresponding to a given <code>PacketType</code>. 
 * @author Ciske
 *
 */
public final class PacketFactory {
	// Jml not accepting legal Java syntax, who makes this shit.
	private static final Map<PacketType, Class<? extends Packet>> packetMapping = new HashMap<PacketType, Class<? extends Packet>>(); //HashMap<>();
	
	static {
		// Register all classes that extend packet here so the factory knows about them.
		// Packets should never have the same packet type!
		packetMapping.put(PacketType.Raw, PacketRaw.class);
	}
	
	/**
	 * Produces a new <code>Packet</code> instance that corresponds to the given <code>PacketType</code>.
	 * @param type The type of packet to create.
	 * @return <code>null</code> if no instance could be created. Otherwise a new instance of a class that extends <code>Packet</code> is returned.
	 */
	public static Packet createPacket(PacketType type) {
		Packet result = null;
		
		if(packetMapping.containsKey(type)) {
			try {
				result = packetMapping.get(type).newInstance();
			} catch (InstantiationException|IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
