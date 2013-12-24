package org.insomnia.rollit.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Controls the switching/creation and lifetime of rooms.
 * @author ciske
 * 
 */
public final class RoomHandler extends NetworkHandler {
	private final RoomLobby lobby;
	private final Map<Integer, RoomGame> gameRooms;

	public RoomHandler() {
		this.lobby = new RoomLobby();
		this.gameRooms = new HashMap<Integer, RoomGame>();
	}

	public RoomLobby getLobby() {
		return lobby;
	}

	public boolean hasGameRoom(int roomId) {
		return gameRooms.containsKey(roomId);
	}

	public RoomGame getGameRoom(int roomId) {
		if (!hasGameRoom(roomId)) {
			throw new IllegalArgumentException("No game room with roomId " + roomId + " exists");
		}

		return gameRooms.get(roomId);
	}

	public void removeMarkedRooms() {
		Iterator<Entry<Integer, RoomGame>> iterator = gameRooms.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Integer, RoomGame> entry = iterator.next();

			if (entry.getValue().shouldRemove()) {
				iterator.remove();
			}
		}
	}
}
