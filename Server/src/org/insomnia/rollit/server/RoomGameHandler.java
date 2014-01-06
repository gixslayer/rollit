package org.insomnia.rollit.server;

/**
 * Handles all relevant network packets for a game room.
 * @author ciske
 * 
 */
public class RoomGameHandler extends NetworkHandler {
	private RoomGame room;

	public void setRoom(RoomGame argRoom) {
		this.room = argRoom;
	}

	private void destroyRoom() {
		main.getRoomHandler().destroyGameRoom(room.getRoomId());
	}
}
