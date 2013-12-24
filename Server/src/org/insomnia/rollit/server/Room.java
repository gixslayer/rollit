package org.insomnia.rollit.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Room {
	private final Map<Integer, Player> players;
	private final String name;
	private final int roomId;
	private final int maxPlayers;

	public Room(String argName, int argRoomId, int argMaxPlayers) {
		this.players = new HashMap<Integer, Player>();
		this.name = argName;
		this.roomId = argRoomId;
		this.maxPlayers = argMaxPlayers;
	}

	public boolean hasSpace() {
		return maxPlayers == -1 ? true : maxPlayers - players.size() > 0;
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

	public boolean addPlayer(Player player) {
		boolean result = false;

		if (hasSpace() && !hasPlayer(player)) {
			player.setCurrentRoom(this);
			players.put(player.getClientId(), player);

			result = true;
		}

		return result;
	}

	public void removePlayer(Player player) {
		players.remove(player.getClientId());

		player.setCurrentRoom(null);
	}

	public boolean hasPlayer(Player player) {
		return players.containsKey(player.getClientId());
	}
}
