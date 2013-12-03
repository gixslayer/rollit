package org.insomnia.rollit.server;

import java.io.IOException;

import org.insomnia.rollit.server.network.Server;

public class Main {

	public static void main(String[] args) {
		try {
			new Server(4096);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}
}
