package org.insomnia.rollit.server;

public final class RoomLobby extends Room {
	public static final String LOBBY_ROOM_NAME = "Lobby";
	public static final int LOBBY_ROOM_ID = 0;

	public RoomLobby() {
		super(LOBBY_ROOM_NAME, LOBBY_ROOM_ID, -1);
	}
}
