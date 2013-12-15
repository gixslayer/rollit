package org.insomnia.rollit.shared.network;

public final class PacketMasterServerResponse extends Packet {
	public static final int SUCCESS = 0;
	public static final int REG_NAME_TAKEN = 1;
	public static final int REG_NAME_INVALID = 2;
	public static final int VAL_FAILED = 3;

	private int response;

	public PacketMasterServerResponse() {
		this(0);
	}

	public PacketMasterServerResponse(int argResponse) {
		super(PacketType.MasterServerResponse);

		this.setResponse(argResponse);
	}

	protected byte[] serializeData() {
		PacketSerializer serializer = new PacketSerializer();

		serializer.addInt(response);

		return serializer.array();
	}

	protected void deserializeData(byte[] data) throws PacketFormatException {
		PacketDeserializer deserializer = new PacketDeserializer(data);

		response = deserializer.getInt();
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int argResponse) {
		this.response = argResponse;
	}

}
