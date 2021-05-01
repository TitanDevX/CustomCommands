package me.titan.customcommands.cmd;

import me.titan.customcommands.Permissions;
import me.titan.customcommands.cmd.cmdedit.*;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanSubCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;

public class CmdCmdEdit extends TitanSubCommand {

	CustomCommandsPlugin plugin;

	public CmdCmdEdit(CustomCommandsPlugin plugin) {
		super("edit");

		setPermission(Permissions.CustomCommands_edit.perm);
		this.plugin = plugin;
	}

	@Override
	public void registerSubCommands() {
		registerSubCommand(new CmdAddExecuteCommand());
		registerSubCommand(new CmdSetWhoCanUse());
		registerSubCommand(new CmdSetPermission());
		registerSubCommand(new CmdSetCooldown());
		registerSubCommand(new CmdSetUses());
		registerSubCommand(new CmdSetCooldown());
		registerSubCommand(new CmdAddReplyMsgCommand());
	}

	@Override
	protected void onCommand(CommandContext con) {



		registerSubCommands();
		// cuc edit s
		if(con.args.length < 1){

			sendHelp(con);
			return;

		}
		String arg = con.args[0];
	}
	public void sendHelp(CommandContext con){

		con.tell("&b-----------------------------------------------------",
				"&a/cuc edit addExecuteCommand <cmd> <new execute command to add>",
				"&a/cuc edit addReplyMessage <cmd> <new reply message to add>",
				"&a/cuc edit setPermission <cmd> <permission>",
				"&a/cuc edit setCooldown <cmd> <cooldown>",
				"&a/cuc edit setUses <cmd> <uses>",
				"&a/cuc edit setWhoCanUse <cmd> <whoCanUse (all, players, console)>",
				"&a/cuc edit addRequiredArg <cmd> <arg name> <arg type ( can be onlinePlayer, offlinePlayer, text, message, number)>," +
						"&a/cuc edit addOptionalArg <arg name> <arg type>",
				"&b-----------------------------------------------------");

	}
}
