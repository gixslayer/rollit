package org.insomnia.rollit.shared.network;

/**
 * An interface that defines the methods a handler for a <code>PacketBuffer</code> must have.
 * 
 * @author Ciske
 * 
 */
public interface PacketBufferHandler {

	/**
	 * Called when a packet is dropped because it could no be deserialized.
	 * 
	 * @param reason The reason why the packet could not be deserialized.
	 */
	public void packetDropped(String reason);

}
