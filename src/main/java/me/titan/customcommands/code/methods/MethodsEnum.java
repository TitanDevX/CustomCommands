package me.titan.customcommands.code.methods;

import org.bukkit.entity.Player;

public interface MethodsEnum<T> {

	Class<?> getReturnType();

	String getName();

	String getUsage();

	int getMinimumArgs();

	void invoke(Player p, T ob, String... args);


}
