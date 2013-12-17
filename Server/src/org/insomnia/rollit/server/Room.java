package org.insomnia.rollit.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Room {
	private final Map<Integer, Player> players;
	private final String name;
	private final int maxPlayers;

	public Room(String argName, int argMaxPlayers) {
		this.players = new HashMap<Integer, Player>();
		this.name = argName;
		this.maxPlayers = argMaxPlayers;
	}

	public boolean hasSpace() {
		return maxPlayers == -1 ? true : maxPlayers - players.size() > 0;
	}

	public String getName() {
		return name;
	}

	public Collection<Player> getPlayers() {
		return players.values();
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}
}
