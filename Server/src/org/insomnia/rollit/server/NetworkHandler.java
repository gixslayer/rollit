package org.insomnia.rollit.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.PacketFactory;
import org.insomnia.rollit.shared.network.PacketType;

/**
 * Provides an abstract base for components that have to handle incoming packets. Handlers for
 * specific packet types can be created by adding the <code>PacketHandler</code> annotation.
 * 
 * @author Ciske
 * 
 */
public abstract class NetworkHandler {
	// Switched from the reflective Method to the new MethodHandle for faster invocation.
	private final Map<PacketType, List<MethodHandle>> packetHandlers;

	/**
	 * Validates and registers all packet handlers. If a packet handler failed to validate a
	 * <code>RuntimeException</code> is thrown.
	 */
	public NetworkHandler() {
		this.packetHandlers = new HashMap<PacketType, List<MethodHandle>>();

		for (Method method : this.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(PacketHandler.class)) {
				PacketHandler handler = method.getAnnotation(PacketHandler.class);
				PacketType packetType = handler.value();

				validateHandler(packetType, method);
				registerHandler(packetType, method);
			}
		}

	}

	/**
	 * Ensures a packet handler meets the requirements given by the <code>PacketHandler</code>
	 * annotation. If a requirement is not met a <code>RuntimeException</code> is thrown.
	 * 
	 * @param packetType The packet type specified in the annotation that belongs to the method.
	 * @param method The method to validate.
	 */
	private void validateHandler(PacketType packetType, Method method) {
		validateModifiers(method);
		validateArgumentCount(method);
		validateFirstArgument(method);
		validateSecondArgument(packetType, method);
	}

	/**
	 * Ensures a packet handler has the public modifier and doesn't have the abstract or static
	 * modifier. If a requirement is not met a <code>RuntimeException</code> is thrown.
	 * 
	 * @param method The method to validate.
	 */
	private void validateModifiers(Method method) {
		int modifiers = method.getModifiers();

		if (!Modifier.isPublic(modifiers)) {
			throw new RuntimeException("Handler " + method + " has to be public");
		} else if (Modifier.isAbstract(modifiers)) {
			throw new RuntimeException("Handler " + method + " cannot be abstract");
		} else if (Modifier.isStatic(modifiers)) {
			throw new RuntimeException("Handler " + method + " cannot be static");
		}
	}

	/**
	 * Ensures a packet handler has exactly 2 arguments. If the argument count doesn't equal 2 a
	 * <code>RuntimeException</code> is thrown.
	 * 
	 * @param method The method to validate.
	 */
	private void validateArgumentCount(Method method) {
		int argumentCount = method.getParameterTypes().length;

		if (argumentCount != 2) {
			throw new RuntimeException("Handler " + method
					+ " must have exactly 2 arguments (found " + argumentCount + ")");
		}
	}

	/**
	 * Ensures a packet handler has a first argument of type <code>int</code>. If the first argument
	 * type doesn't equal <code>int</code> a <code>RuntimeException</code> is thrown.
	 * 
	 * @param method The method to validate.
	 */
	private void validateFirstArgument(Method method) {
		Class<?> parameterType = method.getParameterTypes()[0];

		if (parameterType != int.class) {
			throw new RuntimeException("Handler " + method
					+ " must have a first argument of type int (found " + parameterType + ")");
		}
	}

	/**
	 * Ensures a packet handler has a second argument that matches the type that belongs to a packet
	 * instance of packetType. If the second argument type doesn't equal that type a
	 * <code>RuntimeException</code> is thrown.
	 * 
	 * @param packetType The packet type specified in the annotation that belongs to the method.
	 * @param method The method to validate.
	 */
	private void validateSecondArgument(PacketType packetType, Method method) {
		Class<?> parameterType = method.getParameterTypes()[1];

		try {
			Packet packet = PacketFactory.createPacket(packetType);

			if (parameterType != packet.getClass()) {
				throw new RuntimeException("Handler " + method
						+ " must have a second argument of type " + packet.getClass() + " (found "
						+ parameterType + ")");
			}

		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Could not validate second argument of handler " + method
					+ " as a packet instance for packet type " + packetType
					+ " could not be created: " + e.getMessage());
		}
	}

	/**
	 * Registers a new packet handler. When this method is called the handler should be validated
	 * and should not cause any exceptions when invoked.
	 * 
	 * @param packetType The packet type specified in the annotation that belongs to the method.
	 * @param method The method to register.
	 */
	private void registerHandler(PacketType packetType, Method method) {
		if (!packetHandlers.containsKey(packetType)) {
			packetHandlers.put(packetType, new ArrayList<MethodHandle>());
		}

		Lookup lookup = MethodHandles.lookup();

		try {
			MethodHandle handle = lookup.unreflect(method);

			packetHandlers.get(packetType).add(handle);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to get method handle for handler " + method + ": "
					+ e.getMessage());
		}
	}

	/**
	 * Handles an incoming packet by invoking all registered packet handlers for the specific packet
	 * type. If a handler could not be invoked a <code>RuntimeException</code> is thrown.
	 * 
	 * @param clientId The client that send the packet.
	 * @param packet The actual packet send by the client.
	 */
	public void handlePacket(int clientId, Packet packet) {
		PacketType packetType = packet.getType();

		if (packetHandlers.containsKey(packetType)) {
			for (MethodHandle methodHandle : packetHandlers.get(packetType)) {
				try {
					methodHandle.invoke(this, clientId, packet);
				} catch (Throwable e) {
					throw new RuntimeException("Exception while invoking handler " + methodHandle
							+ ": " + e.getMessage());
				}
			}
		}
	}

	/**
	 * Indicates whether the given packet from a specific client should be send to any registered
	 * handlers.
	 * @param clientId The client who send the packet.
	 * @param packet The packet instance that was deserialized.
	 * @return <code>true</code> to pass the packet onto all registered packet handlers.
	 * <code>false</code> to ignore this packet.
	 */
	public boolean shouldHandlePacket(int clientId, Packet packet) {
		return true;
	}

	/**
	 * An annotation used to indicate a method has to handle packets of a specific type. A method
	 * must be public and cannot be static or abstract. A method also has to have exactly 2
	 * arguments. The first argument must be of type <code>int</code> while the second argument has
	 * to match the type of the specified <code>PacketType</code>. If the above conditions are not
	 * met a <code>RuntimeException</code> will be thrown when the method is validated. Any
	 * exception thrown during the execution of the handler are not handled and will result in a
	 * <code>RuntimeException</code>.
	 * 
	 * @author Ciske
	 * 
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface PacketHandler {
		abstract PacketType value();
	}
}
