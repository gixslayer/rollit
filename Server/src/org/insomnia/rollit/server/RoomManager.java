package org.insomnia.rollit.server;

import java.util.HashMap;
import java.util.Map;

public final class RoomManager extends Manager {
	private final RoomLobby lobby;
	private final Map<String, RoomGame> gameRooms;

	public RoomManager() {
		this.lobby = new RoomLobby();
		this.gameRooms = new HashMap<String, RoomGame>();
	}
}
