package me.titan.customcommands.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Common {

	public static void tell(CommandSender sender, String... msgs) {
		for (String msg : msgs) {

			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));

		}
	}

	public static void tell(CommandSender sender, List<String> msgs) {
		for (String msg : msgs) {

			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));

		}
	}
}
