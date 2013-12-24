package org.insomnia.rollit.server;

/**
 * A room in which the game is played.
 * @author ciske
 * 
 */
public final class RoomGame extends Room {
	public static final int GAME_MAX_PLAYERS = 4;

	private boolean shouldRemove;

	public RoomGame(String argName, int argRoomId) {
		super(argName, argRoomId, GAME_MAX_PLAYERS);

		this.shouldRemove = false;
	}

	public boolean shouldRemove() {
		return shouldRemove;
	}
}
