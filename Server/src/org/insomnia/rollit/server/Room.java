package org.insomnia.rollit.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Room {
	public static final int MAX_PLAYERS_INFINITE = -1;

	private final Map<Integer, Player> players;
	private final String name;
	private final int roomId;
	private final int maxPlayers;
	private final NetworkHandler handler;

	public Room(String argName, int argRoomId, int argMaxPlayers, NetworkHandler argHandler) {
		this.players = new HashMap<Integer, Player>();
		this.name = argName;
		this.roomId = argRoomId;
		this.maxPlayers = argMaxPlayers;
		this.handler = argHandler;
	}

	public boolean hasSpace() {
		return maxPlayers == MAX_PLAYERS_INFINITE ? true : maxPlayers - players.size() > 0;
	}

	public String getName() {
		return name;
	}

	public int getRoomId() {
		return roomId;
	}

	public Collection<Player> getPlayers() {
		return players.values();
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public NetworkHandler getHandler() {
		return handler;
	}

	public boolean addPlayer(Player player) {
		boolean result = false;

		if (hasSpace() && !hasPlayer(player.getClientId())) {
			players.put(player.getClientId(), player);

			result = true;
		}

		return result;
	}

	public void removePlayer(int clientId) {
		players.remove(clientId);
	}

	public boolean hasPlayer(int clientId) {
		return players.containsKey(clientId);
	}
}
