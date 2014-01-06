package org.insomnia.rollit.server;

public final class Player {
	private final int clientId;
	private final String name;
	private Room currentRoom;

	public Player(int argClientId, String argName) {
		this.clientId = argClientId;
		this.name = argName;
		this.currentRoom = null;
	}

	public int getClientId() {
		return clientId;
	}

	public String getName() {
		return name;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public boolean switchRoom(Room room) {
		boolean result = false;

		if (room.hasPlayer(clientId)) {
			// The player is already in the room and no switching is required.
			result = true;
		} else if (room.addPlayer(this)) {
			// Joined the room successfully, remove the player from his old room (if he had one).
			if (currentRoom != null) {
				currentRoom.removePlayer(clientId);
			}

			currentRoom = room;

			result = true;
		}

		return result;
	}

	public void moveToLobby() {
		RoomLobby lobby = Main.getInstance().getRoomHandler().getLobby();

		if (!switchRoom(lobby)) {
			// This should never occur.
			throw new RuntimeException("Could not move client " + clientId + " to lobby");
		}
	}
}
