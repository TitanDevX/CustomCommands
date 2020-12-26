package me.titan.customcommands.cmd.lib;

import me.titan.customcommands.cmd.Messages;
import me.titan.customcommands.utils.Common;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandContext {

	final public CommandSender sender;
	final public Player player;
	final public CommandRequirements.CmdCheckResult ReqCheckResult;
	public String[] args;

	public boolean foundError = false;


	public CommandContext(CommandSender sender, Player player, CommandRequirements.CmdCheckResult argCheckResult, String[] args) {
		this.sender = sender;
		this.player = player;
		this.ReqCheckResult = argCheckResult;
		this.args = args;
	}

	public static CommandContext of(CommandSender sender, CommandRequirements.CmdCheckResult c, String[] args) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		return new CommandContext(sender, p, c, args);
	}

	public boolean isPlayer() {
		return player != null;
	}

	public void tell(String... msg) {

		Common.tell(sender, msg);
	}

	public <T> T readArg(int index, Class<T> clazz, T defaults) {

		String arg = args[index];
		if (clazz == int.class || clazz == Integer.class) {
			int i = readInt(arg);
			return i == -1 ? defaults : clazz.cast(i);
		} else if (clazz == OfflinePlayer.class) {
			return clazz.cast(Bukkit.getOfflinePlayer(arg));
		} else if (clazz == Player.class) {
			Player p = Bukkit.getPlayer(arg);
			if (p == null) {
				if (defaults == null) {
					tell(Messages.Player_Is_Not_Online.getReplaced("{arg}", arg));
					foundError = true;
					return null;
				} else {
					return defaults;
				}
			}
			return clazz.cast(p);
		}
		return defaults;
	}

	public OfflinePlayer readOfflinePlayer(String arg) {
		return Bukkit.getOfflinePlayer(arg);
	}

	public Player reaPlayer(String arg) {
		Player p = Bukkit.getPlayer(arg);
		if (p == null) {

			tell(Messages.Player_Is_Not_Online.getReplaced("{arg}", arg));
			foundError = true;
			return null;

		}
		return p;
	}

	public int readInt(String arg) {
		if (!Util.isInteger(arg)) {
			foundError = true;
			return -1;
		}

		return Integer.parseInt(arg);
	}
}
