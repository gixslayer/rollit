package org.insomnia.rollit.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.insomnia.rollit.shared.network.Packet;
import org.insomnia.rollit.shared.network.PacketFactory;
import org.insomnia.rollit.shared.network.PacketType;

public abstract class NewManager {
	private final Map<PacketType, List<Method>> packetHandlers;

	public NewManager() {
		this.packetHandlers = new HashMap<PacketType, List<Method>>();

		for (Method method : this.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(PacketHandler.class)) {
				registerHandler(method);
			}
		}

	}

	private void registerHandler(Method method) {
		PacketHandler handler = method.getAnnotation(PacketHandler.class);
		PacketType packetType = handler.packetType();
		Class<?> methodParameters[] = method.getParameterTypes();

		if (methodParameters.length != 1) {
			throw new RuntimeException("Wrong argument count");
		}

		Class<?> parameterType = methodParameters[0];

		try {
			Packet packet = PacketFactory.createPacket(packetType);

			if (parameterType != packet.getClass()) {
				throw new RuntimeException("Invalid argument type");
			}

			if (!packetHandlers.containsKey(packetType)) {
				packetHandlers.put(packetType, new ArrayList<Method>());
			}

			packetHandlers.get(packetType).add(method);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Could not create packet instance: " + e.getMessage());
		}
	}

	public void PacketReceived(Packet packet) {
		PacketType packetType = packet.getType();

		if (packetHandlers.containsKey(packetType)) {
			for (Method method : packetHandlers.get(packetType)) {
				try {
					method.invoke(this, packet);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new RuntimeException("Could not invoke handler: " + e.getMessage());
				}
			}
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface PacketHandler {
		PacketType packetType();
	}
}
