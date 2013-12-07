package org.insomnia.rollit.shared.network;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides static methods to aid in packet serialization.
 * @author ciske
 *
 */
public final class PacketUtils {
	private static final int BYTE_SIZE = 1; // 8 bit signed.
	private static final int BOOLEAN_SIZE = 1; // Only needs one bit but is stored as a byte.
	private static final int SHORT_SIZE = 2; // 16 bit signed.
	private static final int CHAR_SIZE = 2; // 16 bit.
	private static final int INT_SIZE = 4; // 32 bit signed.
	private static final int FLOAT_SIZE = 4; // 32 bit.
	private static final int LONG_SIZE = 8; // 64 bit signed.
	private static final int DOUBLE_SIZE = 8; // 64 bit.
	private static final int BIGGEST_SIZE = 8; // The biggest size a single data type can be.
	
	private static final ByteBuffer byteBuffer = ByteBuffer.allocate(BIGGEST_SIZE);
	private static final Charset charset = Charset.forName("UTF-8");
	
	//////// Deserialize
	
	/**
	 * Deserializes a <code>boolean</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>boolean</code> at the given offset in the byte buffer.
	 */
	public static boolean toBoolean(byte[] buffer, int offset) {
		return toBoolean(buffer, offset, null);
	}
	
	/**
	 * Deserializes a <code>boolean</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>boolean</code> at the given offset in the byte buffer.
	 */
	public static boolean toBoolean(byte[] buffer, int offset, AtomicInteger bytesRead) {
		if(bytesRead != null) {
			bytesRead.set(BOOLEAN_SIZE);
		}
		
		return buffer[offset] == 0x1;
	}
	
	/**
	 * Deserializes a <code>byte</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>byte</code> at the given offset in the byte buffer.
	 */
	public static byte toByte(byte[] buffer, int offset) {
		return toByte(buffer, offset, null);
	}
	
	/**
	 * Deserializes a <code>byte</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>byte</code> at the given offset in the byte buffer.
	 */
	public static byte toByte(byte[] buffer, int offset, AtomicInteger bytesRead) {
		if(bytesRead != null) {
			bytesRead.set(BYTE_SIZE);
		}
		
		return buffer[offset];
	}
	
	/**
	 * Deserializes a <code>short</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>short</code> at the given offset in the byte buffer.
	 */
	public static short toShort(byte[] buffer, int offset) {
		return toShort(buffer, offset, null);
	}
	
	/**
	 * Deserializes a <code>short</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>short</code> at the given offset in the byte buffer.
	 */
	public static short toShort(byte[] buffer, int offset, AtomicInteger bytesRead) {
		if(bytesRead != null) {
			bytesRead.set(SHORT_SIZE);
		}
		
		byteBuffer.put(buffer, offset, SHORT_SIZE);
		byteBuffer.flip();
		
		ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
		
		short value = shortBuffer.get();
		
		byteBuffer.clear();
		
		return value;
	}
	
	/**
	 * Deserializes a <code>char</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>char</code> at the given offset in the byte buffer.
	 */
	public static char toChar(byte[] buffer, int offset) {
		return toChar(buffer, offset, null);
	}
	
	/**
	 * Deserializes a <code>char</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>char</code> at the given offset in the byte buffer.
	 */
	public static char toChar(byte[] buffer, int offset, AtomicInteger bytesRead) {
		if(bytesRead != null) {
			bytesRead.set(CHAR_SIZE);
		}
		
		byteBuffer.put(buffer, offset, CHAR_SIZE);
		byteBuffer.flip();
		
		CharBuffer charBuffer = byteBuffer.asCharBuffer();
		
		char value = charBuffer.get();
		
		byteBuffer.clear();
		
		return value;
	}
	
	/**
	 * Deserializes an <code>int</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>int</code> at the given offset in the byte buffer.
	 */
	public static int toInt(byte[] buffer, int offset) {
		return toInt(buffer, offset, null);
	}
	
	/**
	 * Deserializes an <code>int</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>int</code> at the given offset in the byte buffer.
	 */
	public static int toInt(byte[] buffer, int offset, AtomicInteger bytesRead) {
		if(bytesRead != null) {
			bytesRead.set(INT_SIZE);
		}
		
		byteBuffer.put(buffer, offset, INT_SIZE);
		byteBuffer.flip();
		
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		
		int value = intBuffer.get();
		
		byteBuffer.clear();
		
		return value;
	}
	
	/**
	 * Deserializes a <code>float</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>float</code> at the given offset in the byte buffer.
	 */
	public static float toFloat(byte[] buffer, int offset) {
		return toFloat(buffer, offset, null);
	}
	
	/**
	 * Deserializes a <code>float</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>float</code> at the given offset in the byte buffer.
	 */
	public static float toFloat(byte[] buffer, int offset, AtomicInteger bytesRead) {
		if(bytesRead != null) {
			bytesRead.set(FLOAT_SIZE);
		}
		
		byteBuffer.put(buffer, offset, FLOAT_SIZE);
		byteBuffer.flip();
		
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		
		float value = floatBuffer.get();
		
		byteBuffer.clear();
		
		return value;
	}

	/**
	 * Deserializes a <code>long</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>long</code> at the given offset in the byte buffer.
	 */
	public static long toLong(byte[] buffer, int offset) {
		return toLong(buffer, offset, null);
	}
	
	/**
	 * Deserializes a <code>long</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>long</code> at the given offset in the byte buffer.
	 */
	public static long toLong(byte[] buffer, int offset, AtomicInteger bytesRead) {
		if(bytesRead != null) {
			bytesRead.set(LONG_SIZE);
		}
		
		byteBuffer.put(buffer, offset, LONG_SIZE);
		byteBuffer.flip();
		
		LongBuffer longBuffer = byteBuffer.asLongBuffer();
		
		long value = longBuffer.get();
		
		byteBuffer.clear();
		
		return value;
	}
	
	/**
	 * Deserializes a <code>double</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>double</code> at the given offset in the byte buffer.
	 */
	public static double toDouble(byte[] buffer, int offset) {
		return toDouble(buffer, offset, null);
	}
	
	/**
	 * Deserializes a <code>double</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>double</code> at the given offset in the byte buffer.
	 */
	public static double toDouble(byte[] buffer, int offset, AtomicInteger bytesRead) {
		if(bytesRead != null) {
			bytesRead.set(DOUBLE_SIZE);
		}
		
		byteBuffer.put(buffer, offset, DOUBLE_SIZE);
		byteBuffer.flip();
		
		DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
		
		double value = doubleBuffer.get();
		
		byteBuffer.clear();
		
		return value;
	}
	
	/**
	 * Deserializes a <code>String</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @return The <code>String</code> at the given offset in the byte buffer.
	 */
	public static String toString(byte[] buffer, int offset) {
		return toString(buffer, offset, null);
	}
	
	/**
	 * Deserializes a <code>String</code> value from a byte buffer.
	 * @param buffer The byte buffer to deserialize from.
	 * @param offset The offset within the buffer to deserialize from.
	 * @param bytesRead Will contain the amount of bytes read from the buffer.
	 * @return The <code>String</code> at the given offset in the byte buffer.
	 */
	public static String toString(byte[] buffer, int offset, AtomicInteger bytesRead) {
		int length = toInt(buffer, offset);
		
		if(bytesRead != null) {
			bytesRead.set(length + INT_SIZE);
		}
		
		return new String(buffer, offset + INT_SIZE, length, charset);
	}
	
	//////// Serialize
	
	/**
	 * Serializes a <code>boolean</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, boolean value) {
		buffer[offset] = (byte) (value ? 0x1 : 0x0);
		
		return BOOLEAN_SIZE;
	}
	
	/**
	 * Serializes a <code>byte</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, byte value) {
		buffer[offset] = value;
		
		return BYTE_SIZE;
	}
	
	/**
	 * Serializes a <code>short</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, short value) {
		ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
		
		shortBuffer.put(value);
		
		byte[] temp = byteBuffer.array();
		
		byteBuffer.clear();
		
		System.arraycopy(temp, 0, buffer, offset, SHORT_SIZE);
		
		return SHORT_SIZE;
	}
	
	/**
	 * Serializes a <code>char</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, char value) {
		CharBuffer charBuffer = byteBuffer.asCharBuffer();
		
		charBuffer.put(value);
		
		byte[] temp = byteBuffer.array();
		
		byteBuffer.clear();
		
		System.arraycopy(temp, 0, buffer, offset, CHAR_SIZE);
		
		return CHAR_SIZE;
	}
	
	/**
	 * Serializes an <code>int</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, int value) {
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		
		intBuffer.put(value);
		
		byte[] temp = byteBuffer.array();
		
		byteBuffer.clear();
		
		System.arraycopy(temp, 0, buffer, offset, INT_SIZE);
		
		return INT_SIZE;
	}
	
	/**
	 * Serializes a <code>float</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, float value) {
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		
		floatBuffer.put(value);
		
		byte[] temp = byteBuffer.array();
		
		byteBuffer.clear();
		
		System.arraycopy(temp, 0, buffer, offset, FLOAT_SIZE);
		
		return FLOAT_SIZE;
	}
	
	/**
	 * Serializes a <code>long</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, long value) {
		LongBuffer longBuffer = byteBuffer.asLongBuffer();
		
		longBuffer.put(value);
		
		byte[] temp = byteBuffer.array();
		
		byteBuffer.clear();
		
		System.arraycopy(temp, 0, buffer, offset, LONG_SIZE);

		return LONG_SIZE;
	}
	
	/**
	 * Serializes a <code>double</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, double value) {
		DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
		
		doubleBuffer.put(value);
		
		byte[] temp = byteBuffer.array();
		
		byteBuffer.clear();
		
		System.arraycopy(temp, 0, buffer, offset, DOUBLE_SIZE);
		
		return DOUBLE_SIZE;
	}

	/**
	 * Serializes a <code>String</code> value to a byte buffer.
	 * @param buffer The byte buffer to serialize to.
	 * @param offset The offset within the buffer to serialize to.
	 * @param value The value to serialize.
	 * @return The amount of bytes written.
	 */
	public static int getBytes(byte[] buffer, int offset, String value) {
		byte[] temp = value.getBytes(charset);
			
		getBytes(buffer, offset, temp.length);
			
		System.arraycopy(temp, 0, buffer, offset + INT_SIZE, temp.length);
		
		return temp.length + INT_SIZE;
	}
	
	//////// Type size
	
	/**
	 * Returns the serialized size of a <code>boolean</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(boolean value) {
		return BOOLEAN_SIZE;
	}
	
	/**
	 * Returns the serialized size of a <code>byte</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(byte value) {
		return BYTE_SIZE;
	}
	
	/**
	 * Returns the serialized size of a <code>short</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(short value) {
		return SHORT_SIZE;
	}
	
	/**
	 * Returns the serialized size of a <code>char</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(char value) {
		return CHAR_SIZE;
	}
	
	/**
	 * Returns the serialized size of an <code>int</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(int value) {
		return INT_SIZE;
	}
	
	/**
	 * Returns the serialized size of a <code>float</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(float value) {
		return FLOAT_SIZE;
	}
	
	/**
	 * Returns the serialized size of a <code>long</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(long value) {
		return LONG_SIZE;
	}
	
	/**
	 * Returns the serialized size of a <code>double</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(double value) {
		return DOUBLE_SIZE;
	}
	
	/**
	 * Returns the serialized size of a <code>String</code> value.
	 * @param value An arbitrary value. 
	 */
	public static int getSize(String value) {
		byte[] temp = value.getBytes(charset);
		
		return temp.length + INT_SIZE;
	}
}
