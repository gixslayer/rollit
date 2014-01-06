package org.insomnia.rollit.server;

import org.insomnia.rollit.shared.network.Packet;

/**
 * Handles all relevant network packets for a game room.
 * 
 * @author ciske
 * 
 */
public class RoomGameHandler extends NetworkHandler {
	private RoomGame room;

	public void setRoom(RoomGame argRoom) {
		this.room = argRoom;
	}

	public boolean shouldHandlePacket(int clientId, Packet packet) {
		return room.hasPlayer(clientId);
	}

	private void destroyRoom() {
		main.getRoomHandler().destroyGameRoom(room.getRoomId());
	}
}
