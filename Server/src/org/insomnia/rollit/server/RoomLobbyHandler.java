package org.insomnia.rollit.server;

import org.insomnia.rollit.shared.network.Packet;

/**
 * Handles all relevant network packets for the lobby room.
 * @author ciske
 * 
 */
public class RoomLobbyHandler extends NetworkHandler {
	public boolean shouldHandlePacket(int clientId, Packet packet) {
		return false;
	}
}
