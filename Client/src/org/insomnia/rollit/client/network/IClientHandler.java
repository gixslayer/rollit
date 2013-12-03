package org.insomnia.rollit.client.network;

import org.insomnia.rollit.shared.network.Packet;

public interface IClientHandler {
	public void connected(String host, int port);

	public void disconnected();

	public void dataReceived(int bytesReceived);

	public void dataSend(int bytesSend);

	public void packetReceived(Packet packet);

	public void packetSend(Packet packet);
}
