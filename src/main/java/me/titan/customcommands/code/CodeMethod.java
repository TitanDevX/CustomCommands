package me.titan.customcommands.code;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface CodeMethod<O> {
	Object apply(Player p, O o, String[] args);




}
