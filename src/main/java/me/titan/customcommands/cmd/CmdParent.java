package me.titan.customcommands.cmd;

import me.titan.customcommands.Permissions;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;

public class CmdParent extends TitanCommand {
	CustomCommandsPlugin plugin;
	public CmdParent(CustomCommandsPlugin plugin) {
		super("customcommands");
		aliases("cuc", "ccmd", "customcmd");
		setPermission(Permissions.CustomCommands_admin.perm);
		this.plugin = plugin;
	}

	@Override
	public void registerSubCommands() {

		registerSubCommand(new CmdCmdCreate(plugin));
		registerSubCommand(new CmdReload(plugin));
	}

	@Override
	protected boolean onCommand(CommandContext context) {
		return true;
	}
}
