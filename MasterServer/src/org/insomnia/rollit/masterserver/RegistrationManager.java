package org.insomnia.rollit.masterserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.insomnia.rollit.shared.network.packets.PacketRegister;

public final class RegistrationManager {
	private final Map<String, byte[]> registrations;

	public RegistrationManager() {
		this.registrations = new ConcurrentHashMap<String, byte[]>();
	}

	public boolean loadData() {
		boolean result = true;

		File file = new File("registrations.db");

		if (file.exists()) {
			try (InputStream fileInStream = new FileInputStream("registrations.db");
					DataInputStream inStream = new DataInputStream(fileInStream)) {

				while (fileInStream.available() != 0 && result) {
					String name = inStream.readUTF();
					byte[] hash = new byte[PacketRegister.HASH_LENGTH];

					if (inStream.read(hash) != PacketRegister.HASH_LENGTH) {
						result = false;
					} else {
						registrations.put(name, hash);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
			}
		}

		return result;
	}

	public void saveData() {
		try (OutputStream fileOutStream = new FileOutputStream("registrations.db");
				DataOutputStream outStream = new DataOutputStream(fileOutStream)) {
			for (Entry<String, byte[]> entry : registrations.entrySet()) {
				outStream.writeUTF(entry.getKey());
				outStream.write(entry.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isRegistered(String name) {
		return registrations.containsKey(name);
	}

	public boolean isNameValid(String name) {
		// TODO: make constraints for a player name.
		return true;
	}

	public boolean validate(String name, byte[] hash) {
		return isRegistered(name) && Arrays.equals(registrations.get(name), hash);
	}

	public void register(String name, byte[] hash) {
		registrations.put(name, hash);
	}
}
