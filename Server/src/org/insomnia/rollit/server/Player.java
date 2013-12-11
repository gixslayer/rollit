package org.insomnia.rollit.server;

public final class Player {
	private final int clientId;
	private final String name;

	public Player(int argClientId, String argName) {
		this.clientId = argClientId;
		this.name = argName;
	}

	public int getClientId() {
		return clientId;
	}

	public String getName() {
		return name;
	}
}
