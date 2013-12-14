package org.insomnia.rollit.shared.network;

public interface MasterServerHandler extends PacketBufferHandler {
	public void listening(int port);

	public void listenFailed(int port);

	public void stopped(String reason);

	public void packetReceived(MasterServerClient client, Packet packet);

	public void packetSendFailed(String reason);

}
