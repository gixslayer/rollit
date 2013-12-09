package org.insomnia.rollit.shared.network;

public class PacketRaw extends Packet {
	private static final byte[] DEFAULT_DATA = new byte[0];

	private byte[] data;

	PacketRaw() {
		this(DEFAULT_DATA);
	}

	PacketRaw(byte[] data) {
		super(PacketType.Raw);

		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	protected byte[] serializeData() {
		return data;
	}

	protected void deserializeData(byte[] data) {
		this.data = data;
	}

}
