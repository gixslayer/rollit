package org.insomnia.rollit.shared.network.packets;

import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.PacketDeserializer;
import org.insomnia.rollit.shared.network.PacketFormatException;
import org.insomnia.rollit.shared.network.PacketSerializer;
import org.insomnia.rollit.shared.network.PacketType;

public final class PacketConnect extends Packet {
	private String name;

	public PacketConnect(String argName) {
		super(PacketType.Connect);

		this.name = argName;
	}

	protected byte[] serializeData() {
		PacketSerializer serializer = new PacketSerializer();

		serializer.addString(name);

		return serializer.array();
	}

	protected void deserializeData(byte[] data) throws PacketFormatException {
		PacketDeserializer deserializer = new PacketDeserializer(data);

		name = deserializer.getString();
	}

	public String getName() {
		return name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

}
