package org.insomnia.rollit.server;

import java.util.HashMap;
import java.util.Map;

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

		// Attach the lobby handler.
		main.attachNetworkHandler(lobby.getHandler());
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

	public boolean createGameRoom() {
		// TODO: Actually set the locals below to sensible values. If no sensible values could be
		// assigned (EG no unique roomId could be generated) false should be returned to indicate
		// the room creation has failed.
		// Perhaps this method should be private as only clients request to create rooms and that
		// request is handled by this class.
		String roomName = "";
		int roomId = 0;
		int maxPlayers = 0;

		RoomGame room = new RoomGame(roomName, roomId, maxPlayers);

		main.attachNetworkHandler(room.getHandler());

		gameRooms.put(roomId, room);

		return true;
	}

	public void destroyGameRoom(int roomId) {
		if (hasGameRoom(roomId)) {
			RoomGame room = getGameRoom(roomId);

			room.destroy();
			main.detachNetworkHandler(room.getHandler());

			gameRooms.remove(roomId);
		}
	}
}
