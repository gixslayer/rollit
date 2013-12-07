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
	 */
	public boolean getBool() {
		boolean result = PacketUtils.toBoolean(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}
	
	/**
	 * Deserializes the next <code>byte</code> value from the byte buffer.
	 */
	public byte getByte() {
		byte result = PacketUtils.toByte(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}
	
	/**
	 * Deserializes the next <code>short</code> value from the byte buffer.
	 */
	public short getShort() {
		short result = PacketUtils.toShort(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}
	
	/**
	 * Deserializes the next <code>char</code> value from the byte buffer.
	 */
	public char getChar() {
		char result = PacketUtils.toChar(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}
	
	/**
	 * Deserializes the next <code>int</code> value from the byte buffer.
	 */
	public int getInt() {
		int result = PacketUtils.toInt(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}
	
	/**
	 * Deserializes the next <code>float</code> value from the byte buffer.
	 */
	public float getFloat() {
		float result = PacketUtils.toFloat(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}
	
	/**
	 * Deserializes the next <code>long</code> value from the byte buffer.
	 */
	public long getLong() {
		long result = PacketUtils.toLong(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}
	
	/**
	 * Deserializes the next <code>double</code> value from the byte buffer.
	 */
	public double getDouble() {
		double result = PacketUtils.toDouble(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}
	
	/**
	 * Deserializes the next <code>String</code> value from the byte buffer.
	 */
	public String getString() {
		String result = PacketUtils.toString(buffer, offset, bytesRead);
		
		offset += bytesRead.get();
		
		return result;
	}

	/**
	 * Deserializes the next count bytes from the byte buffer and copies the result in a new array of bytes.
	 * @param count The amount of bytes to deserialize from the byte buffer.
	 * @return A copy of the next count bytes in the byte buffer.
	 */
	public byte[] getBytes(int count) {
		byte[] result = Arrays.copyOfRange(buffer, offset, offset + count);
		
		offset += count;
		
		return result;
	}
}
