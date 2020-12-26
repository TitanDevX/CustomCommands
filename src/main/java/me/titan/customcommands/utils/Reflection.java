package me.titan.customcommands.utils;

import org.bukkit.Bukkit;

public class Reflection {
	static String serverVersion;

	public static Class<?> getNMSClass(String name) {
		if (serverVersion == null) {
			serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		}
		try {
			return Class.forName("net.minecraft.server." + serverVersion + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Class<?> getOBCClass(String name) {
		if (serverVersion == null) {
			serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		}
		try {
			return Class.forName("org.bukkit.craftbukkit." + serverVersion + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
