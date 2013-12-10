package org.insomnia.rollit.shared.rollit;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a ball that can be put in a socket.
 * @author ciske
 * 
 */
public enum Ball {
	/**
	 * Used to indicate an empty socket.
	 */
	None((byte) 0),
	/**
	 * Used to indicate a blue ball.
	 */
	Blue((byte) 1),
	/**
	 * Used to indicate a red ball.
	 */
	Red((byte) 2),
	/**
	 * Used to indicate a yellow ball.
	 */
	Yellow((byte) 3),
	/**
	 * Used to indicate a green ball.
	 */
	Green((byte) 4);

	private byte value;

	Ball(byte argValue) {
		this.value = argValue;
	}

	/**
	 * Returns the unique identifier of a ball.
	 */
	public byte getValue() {
		return value;
	}

	// Maps each element in the enum to the corresponding unique identifier
	private static final Map<Byte, Ball> BALL_MAPPING = new HashMap<Byte, Ball>();

	// Build the mapping.
	static {
		for (Ball ball : Ball.values()) {
			BALL_MAPPING.put(ball.getValue(), ball);
		}
	}

	/**
	 * Returns the <code>Ball</code> that has the same identifier as given by value. If no matching
	 * <code>Ball</code> entry is found, an <code>IllegalArgumentException</code> is thrown.
	 * @param value The unique identifier of the <code>Ball</code> entry to return.
	 */
	public static Ball fromByte(byte value) {
		if (!BALL_MAPPING.containsKey(value)) {
			throw new IllegalArgumentException("Unkown ball type " + value);
		}

		return BALL_MAPPING.get(value);
	}
}
