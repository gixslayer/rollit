package org.insomnia.rollit.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Room {
	private final Map<Integer, Player> players;
	private final String name;
	private final int maxPlayers;
	private final boolean passwordProtected;
	private final String password;

	public Room(String argName, int argMaxPlayers) {
		this(argName, argMaxPlayers, false, null);
	}

	public Room(String argName, int argMaxPlayers, boolean argPasswordProtected, String argPassword) {
		this.players = new HashMap<Integer, Player>();
		this.name = argName;
		this.maxPlayers = argMaxPlayers;
		this.passwordProtected = argPasswordProtected;
		this.password = argPassword;
	}

	public boolean hasSpace() {
		return maxPlayers == -1 ? true : maxPlayers - players.size() > 0;
	}

	public boolean testPassword(String argPassword) {
		return !passwordProtected || password.equals(argPassword);
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

	public boolean isPasswordProtected() {
		return passwordProtected;
	}
}
