package org.insomnia.rollit.server;

import java.util.HashMap;
import java.util.Map;

public final class RoomHandler extends NetworkHandler {
	private final RoomLobby lobby;
	private final Map<String, RoomGame> gameRooms;

	public RoomHandler() {
		this.lobby = new RoomLobby();
		this.gameRooms = new HashMap<String, RoomGame>();
	}
}
