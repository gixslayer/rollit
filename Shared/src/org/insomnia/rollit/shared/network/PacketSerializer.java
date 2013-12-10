package org.insomnia.rollit.shared.network;

/**
 * A class to simplify packet serialization.
 * @author ciske
 * 
 */
public final class PacketSerializer {
	/**
	 * The default capacity in bytes of the underlying byte buffer.
	 */
	public static final int DEFAULT_CAPACITY = 4096;

	private byte[] buffer;
	private int offset;

	/**
	 * Creates a new packet serializer with the default capacity.
	 */
	public PacketSerializer() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates a new packet serializer.
	 * @param capacity The capacity of the underlying byte buffer.
	 */
	public PacketSerializer(int capacity) {
		this.buffer = new byte[capacity];
		this.offset = 0;
	}

	/**
	 * Returns a copy of the underlying buffer from offset 0 and with length the current amount of
	 * bytes written.
	 */
	public byte[] array() {
		byte[] result = new byte[offset];

		System.arraycopy(buffer, 0, result, 0, offset);

		return result;
	}

	/**
	 * Resets the current amount of bytes written to 0 effectively resetting the serializer. Does
	 * not actually clears or reallocates the underlying byte buffer.
	 */
	public void clear() {
		offset = 0;
	}

	/**
	 * Serializes a <code>boolean</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addBoolean(boolean value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes a <code>byte</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addByte(byte value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes a <code>short</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addShort(short value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes a <code>char</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addChar(char value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes an <code>int</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addInt(int value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes a <code>float</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addFloat(float value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes a <code>long</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addLong(long value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes a <code>double</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addDouble(double value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes a <code>String</code> value to the underlying byte buffer. If the underlying byte
	 * buffer doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addString(String value) {
		ensureAvailable(PacketUtils.getSize(value));

		offset += PacketUtils.getBytes(buffer, offset, value);
	}

	/**
	 * Serializes an array of bytes to the underlying byte buffer. If the underlying byte buffer
	 * doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 */
	public void addBytes(byte[] value) {
		addBytes(value, 0, value.length);
	}

	/**
	 * Serializes an array of bytes to the underlying byte buffer. If the underlying byte buffer
	 * doesn't have enough free room it is expanded to ensure enough room is available.
	 * @param value The value to serialize.
	 * @param argOffset The offset within the value to start from.
	 * @param length The amount of bytes to serialize from the value.
	 */
	public void addBytes(byte[] value, int argOffset, int length) {
		ensureAvailable(length);

		System.arraycopy(value, argOffset, buffer, this.offset, length);

		this.offset += length;
	}

	private void ensureAvailable(int bytes) {
		if (offset + bytes > buffer.length) {
			int bytesToAdd = (offset + bytes) - buffer.length;

			byte[] newBuffer = new byte[buffer.length + bytesToAdd];

			System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);

			buffer = newBuffer;
		}
	}
}
