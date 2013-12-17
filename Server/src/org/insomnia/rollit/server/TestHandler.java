package org.insomnia.rollit.server;

import org.insomnia.rollit.shared.network.PacketType;
import org.insomnia.rollit.shared.network.packets.PacketRaw;
import org.insomnia.rollit.shared.network.packets.PacketRegister;

public class TestHandler extends NetworkHandler {

	@PacketHandler(PacketType.Register)
	public void testHandler(int clientId, PacketRegister packet) {
		System.out.println(packet.getName());
	}

	@PacketHandler(PacketType.Register)
	public void testHandler2(int clientId, PacketRegister packet) {
		System.out.println(packet.getName().toUpperCase());
	}

	public static void main(String[] args) {
		NetworkHandler test = new TestHandler();

		test.handlePacket(0, new PacketRegister("zal", null));
		test.handlePacket(0, new PacketRaw());
		test.handlePacket(1, new PacketRegister("dit", null));
		test.handlePacket(1, new PacketRaw());
		test.handlePacket(2, new PacketRaw());
		test.handlePacket(3, new PacketRaw());
		test.handlePacket(0, new PacketRegister("werken", null));
	}
}
