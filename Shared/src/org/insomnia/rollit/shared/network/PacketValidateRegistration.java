package org.insomnia.rollit.shared.network;

public final class PacketValidateRegistration extends Packet {
	/**
	 * Size of the password hash in bytes.
	 */
	public static final int HASH_LENGTH = 16;
	// MD5 is 128bit/16byte, use another one? (probably requires external libs).

	private String name;
	private byte[] hash;

	public PacketValidateRegistration(String argName, byte[] argHash) {
		super(PacketType.ValidateRegistration);

		this.name = argName;
		this.hash = argHash;
	}

	protected byte[] serializeData() {
		PacketSerializer serializer = new PacketSerializer();

		serializer.addString(name);
		serializer.addBytes(hash);

		return serializer.array();
	}

	protected void deserializeData(byte[] data) throws PacketFormatException {
		PacketDeserializer deserializer = new PacketDeserializer(data);

		name = deserializer.getString();
		hash = deserializer.getBytes(HASH_LENGTH);
	}

	public String getName() {
		return name;
	}

	public void setName(String argName) {
		this.name = argName;
	}

	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] argHash) {
		if (argHash.length != HASH_LENGTH) {
			throw new IllegalArgumentException("argHash length must equal " + HASH_LENGTH);
		}

		this.hash = argHash;
	}
}
