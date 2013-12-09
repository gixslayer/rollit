package org.insomnia.rollit.shared.network;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class to simplify packet deserialization.
 * @author ciske
 * 
 */
public final class PacketDeserializer {
	private final byte[] buffer;
	private final AtomicInteger bytesRead;
	private int offset;

	/**
	 * Creates a new packet deserializer.
	 * @param buffer The byte buffer to deserialize data from.
	 */
	public PacketDeserializer(byte[] buffer) {
		this.buffer = buffer;
		this.bytesRead = new AtomicInteger();
		this.offset = 0;
	}

	/**
	 * Deserializes the next <code>boolean</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public boolean getBool() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize(false));

		boolean result = PacketUtils.toBoolean(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next <code>byte</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public byte getByte() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize((byte) 0));

		byte result = PacketUtils.toByte(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next <code>short</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public short getShort() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize((short) 0));

		short result = PacketUtils.toShort(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next <code>char</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public char getChar() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize((char) 0));

		char result = PacketUtils.toChar(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next <code>int</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public int getInt() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize((int) 0));

		int result = PacketUtils.toInt(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next <code>float</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public float getFloat() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize((float) 0));

		float result = PacketUtils.toFloat(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next <code>long</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public long getLong() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize((long) 0));

		long result = PacketUtils.toLong(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next <code>double</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public double getDouble() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize((double) 0));

		double result = PacketUtils.toDouble(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next <code>String</code> value from the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public String getString() throws PacketFormatException {
		ensureAvaliable(PacketUtils.getSize((int) 0));

		int length = PacketUtils.toInt(buffer, offset);

		if (length < 0) {
			throw new PacketFormatException("String length cannot be negative (" + length + ")");
		}

		ensureAvaliable(PacketUtils.getSize((int) 0) + length);

		String result = PacketUtils.toString(buffer, offset, bytesRead);

		offset += bytesRead.get();

		return result;
	}

	/**
	 * Deserializes the next count bytes from the byte buffer and copies the result in a new array
	 * of bytes.
	 * @param count The amount of bytes to deserialize from the byte buffer.
	 * @return A copy of the next count bytes in the byte buffer.
	 * @throws PacketFormatException If the value could not be deserialized.
	 */
	public byte[] getBytes(int count) throws PacketFormatException {
		ensureAvaliable(count);

		byte[] result = Arrays.copyOfRange(buffer, offset, offset + count);

		offset += count;

		return result;
	}

	private void ensureAvaliable(int bytes) throws PacketFormatException {
		if (offset + bytes > buffer.length) {
			throw new PacketFormatException("Needed " + bytes + " bytes, but only had "
					+ ((offset + bytes) - buffer.length) + " bytes.");
		}
	}
}
