package me.titan.customcommands.cmd;

import de.leonhard.storage.Yaml;
import me.titan.customcommands.Permissions;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanSubCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.data.player.PlayerCache;

public class CmdReload extends TitanSubCommand {

	public Yaml defaults;
	CustomCommandsPlugin plugin;

	public CmdReload(CustomCommandsPlugin plugin) {
		super("reload");
		addAlias("rl");

		setPermission(Permissions.CustomCommands_reload.perm);
		this.plugin = plugin;
	}

	@Override
	protected void onCommand(CommandContext con) {

		if (!plugin.getCommandsConfig().getFile().exists()) {
			plugin.saveResource(plugin.getCommandsConfig().getFile().getName(), false);
		}
		PlayerCache.players.clear();
		plugin.getCommandsConfig().forceReload();
		plugin.getCommandsConfig().init();
		plugin.getMessagesConfig().forceReload();
		plugin.getMessagesConfig().init();
		con.tell("&6Successfully reloaded the plugin. and loaded " + plugin.getCommandsBoard().size() + " commands.");

	}
}