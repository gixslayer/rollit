package org.insomnia.rollit.client;

import java.io.IOException;

import org.insomnia.rollit.client.network.Client;
import org.insomnia.rollit.client.network.IClientHandler;
import org.insomnia.rollit.shared.network.Packet;

public class Main implements IClientHandler {
	// private static Client client;

	public static void main(String[] args) {
		try {
			new Client("127.0.0.1", 4096, new Main());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public void connected(String host, int port) {
		System.out.println("Client connected to " + host + ":" + port);
	}

	public void disconnected() {
		System.out.println("Client disconnected");
	}

	public void dataReceived(int bytesReceived) {
		System.out.println("Client received " + bytesReceived + " bytes");
	}

	public void dataSend(int bytesSend) {
		System.out.println("Client send " + bytesSend + " bytes");
	}

	public void packetReceived(Packet packet) {
		// TODO Auto-generated method stub

	}

	public void packetSend(Packet packet) {
		// TODO Auto-generated method stub

	}
}
