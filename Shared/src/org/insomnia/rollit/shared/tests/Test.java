package org.insomnia.rollit.shared.tests;

import org.insomnia.rollit.shared.network.PacketType;

public class Test {

	public static void main(String[] args) {
		PacketType type1 = PacketType.fromByte((byte) 0);

		System.out.println(type1);
	}

}
