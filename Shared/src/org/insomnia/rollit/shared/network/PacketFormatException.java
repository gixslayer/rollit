package org.insomnia.rollit.shared.network;

/**
 * An exception that is thrown during packet (de)serialization.
 * 
 * @author ciske
 * 
 */
public class PacketFormatException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new packet format exception without setting any fields.
	 */
	public PacketFormatException() {

	}

	/**
	 * Creates a new packet format exception.
	 * 
	 * @param message The message that describes why the exception was thrown.
	 */
	public PacketFormatException(String message) {
		super(message);
	}

	/**
	 * Creates a new packet format exception.
	 * 
	 * @param cause The cause of the exception.
	 */
	public PacketFormatException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new packet format exception.
	 * 
	 * @param message The message that describes why the exception was thrown.
	 * @param cause The cause of the exception.
	 */
	public PacketFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
