package org.insomnia.rollit.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.insomnia.rollit.shared.network.PacketType;
import org.insomnia.rollit.shared.network.packets.PacketRegister;

/**
 * Handles all player connections and registrations.
 * @author ciske
 * 
 */
public final class PlayerHandler extends NetworkHandler {
	private final ConcurrentMap<Integer, Player> players;

	public PlayerHandler() {
		this.players = new ConcurrentHashMap<Integer, Player>();
	}

	@PacketHandler(PacketType.Register)
	public void registerHandler(int clientId, PacketRegister packet) {
		if (!isPlayerRegistered(clientId)) {
			// Contact master server and verify registration on a new thread.
			QueryWorker worker = new QueryWorker(this, packet, clientId);

			new Thread(worker, "MasterServerQuery" + clientId).start();
		}
	}

	public void registerPlayer(int clientId, PacketRegister packet) {
		players.put(clientId, new Player(clientId, packet.getName()));
	}

	public void unregisterPlayer(int clientId) {
		players.remove(clientId);
	}

	public boolean isPlayerRegistered(int clientId) {
		return players.containsKey(clientId);
	}

	public Player getPlayer(int clientId) {
		return players.get(clientId);
	}

	/**
	 * Attempts to run a master server query to verify client credentials.
	 * @author ciske
	 * 
	 */
	private final class QueryWorker implements Runnable {
		private final PlayerHandler handler;
		private final PacketRegister packet;
		private final int clientId;

		private QueryWorker(PlayerHandler argHandler, PacketRegister argPacket, int argClientId) {
			this.handler = argHandler;
			this.packet = argPacket;
			this.clientId = argClientId;
		}

		public void run() {
			MasterServerQuery query = new MasterServerQuery();

			if (query.verifyRegistration(packet)) {
				// if the registration is correct register the client and send a reply packet.
				handler.registerPlayer(clientId, packet);

				// Main.getServerInstance().send(clientId, packet);
			} else {
				// if the registration isn't correct send the client a reply packet stating his
				// credentials did not verify with the master server and then drop him.

				// Main.getServerInstance().send(clientId, packet);
				Main.getServerInstance().disconnect(clientId);
			}
		}

	}
}
