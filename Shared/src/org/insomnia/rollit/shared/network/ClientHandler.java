package org.insomnia.rollit.shared.network;

public interface ClientHandler extends PacketBufferHandler {
	public void connected(String host, int port);

	public void connectFailed(String host, int port);

	public void disconnected(String reason);

	public void packetReceived(Packet packet);

	public void packetSend(Packet packet);

	public void packetSendFailed(Packet packet, String reason);
}
