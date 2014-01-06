package org.insomnia.rollit.server;

/**
 * A room in which the game is played.
 * @author ciske
 * 
 */
public final class RoomGame extends Room {
	public static final int GAME_MAX_PLAYERS = 4;
	public static final int GAME_MIN_PLAYERS = 2;

	public RoomGame(String argName, int argRoomId) {
		this(argName, argRoomId, GAME_MAX_PLAYERS);
	}

	public RoomGame(String argName, int argRoomId, int maxPlayers) {
		super(argName, argRoomId, maxPlayers, new RoomGameHandler());

		// Very ugly, but it's not possible to pass this as an argument in the RoomGameHandler
		// constructor.
		((RoomGameHandler) this.getHandler()).setRoom(this);
	}

	public void destroy() {
		// Move all players to the lobby.
		for (Player player : getPlayers()) {
			player.moveToLobby();
		}
	}
}
