package org.insomnia.rollit.server;

import org.insomnia.rollit.shared.network.PacketType;
import org.insomnia.rollit.shared.network.packets.PacketRaw;
import org.insomnia.rollit.shared.network.packets.PacketRegister;

public class NewTestManager extends NewManager {

	@PacketHandler(packetType = PacketType.Register)
	public void testHandler(PacketRegister packet) {
		System.out.println(packet.getName());
	}

	@PacketHandler(packetType = PacketType.Register)
	public void testHandler2(PacketRegister packet) {
		System.out.println(packet.getName().toUpperCase());
	}

	public static void main(String[] args) {
		NewManager test = new NewTestManager();

		test.PacketReceived(new PacketRegister("zal", null));
		test.PacketReceived(new PacketRaw());
		test.PacketReceived(new PacketRegister("dit", null));
		test.PacketReceived(new PacketRaw());
		test.PacketReceived(new PacketRaw());
		test.PacketReceived(new PacketRaw());
		test.PacketReceived(new PacketRegister("werken", null));
	}
}
