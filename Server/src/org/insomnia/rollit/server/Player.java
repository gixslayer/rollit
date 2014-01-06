package org.insomnia.rollit.server;

public final class Player {
	private final int clientId;
	private final String name;
	private Room currentRoom;

	public Player(int argClientId, String argName) {
		this.clientId = argClientId;
		this.name = argName;
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

	public void setCurrentRoom(Room room) {
		currentRoom = room;
	}

	public boolean switchRoom(Room room) {
		boolean result = false;

		if (room.addPlayer(this)) {
			currentRoom = room;

			result = true;
		}

		return result;
	}

	public void moveToLobby() {
		RoomLobby lobby = Main.getInstance().getRoomHandler().getLobby();

		if (!lobby.hasPlayer(this)) {
			// TODO: Verify that joining the lobby should never fail.
			lobby.addPlayer(this);
			setCurrentRoom(lobby);
		}
	}
}
