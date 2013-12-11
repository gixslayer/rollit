package org.insomnia.rollit.server;

public final class RoomGame extends Room {
	public static final int GAME_MAX_PLAYERS = 4;

	public RoomGame(String argName) {
		this(argName, false, null);
	}

	public RoomGame(String argName, boolean argPasswordProtected, String argPassword) {
		super(argName, GAME_MAX_PLAYERS, argPasswordProtected, argPassword);
	}
}
