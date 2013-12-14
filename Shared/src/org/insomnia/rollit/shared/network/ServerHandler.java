package org.insomnia.rollit.shared.network;

public interface ServerHandler {
	public void listening(int port);

	public void listenFailed(int port);

	public void stopped(String reason);

	public void clientConnected(int clientId);

	public void clientRefused(String reason);

	public void clientDisconnected(int clientId);

	public void packetReceived(int clientId, Packet packet);

	public void packetDropped(int clientId, String reason);

	public void packetSend(int clientId, Packet packet);

	public void packetSendFailed(int clientId, Packet packet, String reason);
}
