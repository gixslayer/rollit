package org.insomnia.rollit.server;

public final class RoomGame extends Room {
	public static final int GAME_MAX_PLAYERS = 4;

	public RoomGame(String argName) {
		super(argName, GAME_MAX_PLAYERS);
	}
}
