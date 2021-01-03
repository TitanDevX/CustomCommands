package me.titan.customcommands.cmd;

import de.leonhard.storage.Yaml;
import me.titan.customcommands.Permissions;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanSubCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;

public class CmdReload extends TitanSubCommand {

	CustomCommandsPlugin plugin;
	public Yaml defaults;

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
		try {
			plugin.getCommandsConfig().forceReload();

		} catch (Exception ex) {
			con.tell("&cYaml formatting issues in commands.yml. check console for more details.");
			return;
		}
		plugin.getCommandsConfig().init();

		try {
			plugin.getMessagesConfig().forceReload();
		} catch (Exception ex) {
			con.tell("&cYaml formatting issues in messages.yml. check console for more details.");
			return;
		}
		plugin.getMessagesConfig().init();
		con.tell("&6Successfully reloaded the plugin. and loaded " + plugin.getCommandsBoard().size() + " commands.");

	}
}
