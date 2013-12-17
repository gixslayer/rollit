package org.insomnia.rollit.server;

import java.util.HashMap;
import java.util.Map;

public final class PlayerHandler extends NetworkHandler {
	private final Map<Integer, Player> players;

	public PlayerHandler() {
		this.players = new HashMap<Integer, Player>();
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
