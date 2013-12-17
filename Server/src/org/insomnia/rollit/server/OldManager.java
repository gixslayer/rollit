package org.insomnia.rollit.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.insomnia.rollit.shared.network.Packet;

public abstract class OldManager {
	private final Map<Class<? extends Packet>, List<PacketHandler<?>>> packetHandlers;

	public OldManager() {
		this.packetHandlers = new HashMap<Class<? extends Packet>, List<PacketHandler<?>>>();
	}

	protected void registerPacketHandler(PacketHandler<?> handler) {
		Class<? extends Packet> packetClass = handler.getPacketClass();

		if (packetHandlers.containsKey(packetClass)) {
			packetHandlers.get(packetClass).add(handler);
		} else {
			List<PacketHandler<?>> handlerList = new ArrayList<PacketHandler<?>>();
			handlerList.add(handler);

			packetHandlers.put(packetClass, handlerList);
		}
	}

	@SuppressWarnings("unchecked")
	public void handlePacket(int clientId, Packet packet) {
		Class<? extends Packet> packetClass = packet.getClass();

		if (packetHandlers.containsKey(packetClass)) {
			List<PacketHandler<?>> handlerList = packetHandlers.get(packetClass);

			for (@SuppressWarnings("rawtypes")
			PacketHandler handler : handlerList) {
				handler.handlePacket(clientId, packet);
			}
		}
	}

	abstract class PacketHandler<T extends Packet> {
		private Class<T> packetClass;

		public PacketHandler(Class<T> argPacketClass) {
			this.packetClass = argPacketClass;
		}

		public Class<T> getPacketClass() {
			return packetClass;
		}

		public abstract void handlePacket(int clientId, T packet);
	}
}
