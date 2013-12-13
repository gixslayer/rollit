package org.insomnia.rollit.shared.network;

public class PacketRaw extends Packet {
	private static final byte[] DEFAULT_DATA = new byte[0];

	private byte[] data;

	public PacketRaw() {
		this(DEFAULT_DATA);
	}

	PacketRaw(byte[] argData) {
		super(PacketType.Raw);

		this.data = argData;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] argData) {
		this.data = argData;
	}

	protected byte[] serializeData() {
		return data;
	}

	protected void deserializeData(byte[] argData) {
		this.data = argData;
	}

}
