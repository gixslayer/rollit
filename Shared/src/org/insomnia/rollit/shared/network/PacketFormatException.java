package org.insomnia.rollit.shared.network;

/**
 * An exception that is thrown during packet (de)serialization.
 * @author ciske
 *
 */
public class PacketFormatException extends Exception {
	private static final long serialVersionUID = 1L;

	public PacketFormatException() {
		
	}
	
	public PacketFormatException(String message) {
		super(message);
	}
	
	public PacketFormatException(Throwable cause) {
		super(cause);
	}
	
	public PacketFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
