package org.insomnia.rollit.server;

import java.util.HashMap;
import java.util.Map;

import org.insomnia.rollit.shared.network.PacketConnect;

public final class PlayerManager extends Manager {
	private final Map<Integer, Player> players;
	private final Manager.PacketHandler<PacketConnect> connectHandler =
			new Manager.PacketHandler<PacketConnect>(PacketConnect.class) {

				public void handlePacket(int clientId, PacketConnect packet) {
					System.out.println(packet.getName());
				}

			};

	public PlayerManager() {
		this.players = new HashMap<Integer, Player>();

		this.registerPacketHandler(connectHandler);
	}

	public boolean registerPlayer(int clientId, String name) {
		boolean result = false;

		if (!isPlayerRegistered(clientId)) {
			players.put(clientId, new Player(clientId, name));

			result = true;
		}

		return result;
	}

	public void unregisterPlayer(int clientId) {
		players.remove(clientId);
	}

	public boolean isPlayerRegistered(int clientId) {
		return players.containsKey(clientId);
	}

	public Player getPlayer(int clientId) {
		return players.get(clientId);
	}
}
