package org.insomnia.rollit.server;

import org.insomnia.rollit.shared.network.Packet;

/**
 * Handles all relevant network packets for the lobby room.
 * 
 * @author ciske
 * 
 */
public class RoomLobbyHandler extends NetworkHandler {
	private RoomLobby room;

	public void setRoom(RoomLobby argRoom) {
		this.room = argRoom;
	}

	public boolean shouldHandlePacket(int clientId, Packet packet) {
		return room.hasPlayer(clientId);
	}
}
