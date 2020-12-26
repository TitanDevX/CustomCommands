package me.titan.customcommands.cmd;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import me.titan.customcommands.Permissions;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanSubCommand;
import me.titan.customcommands.container.SingleCustomCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;

import java.io.File;

public class CmdCmdCreate extends TitanSubCommand {

	public Yaml defaults;
	CustomCommandsPlugin plugin;

	public CmdCmdCreate(CustomCommandsPlugin plugin) {
		super("createCommand");
		addAlias("createCmd", "newcmd");
		addRequiredArgs("label");

		setPermission(Permissions.CustomCommands_create.perm);
		this.plugin = plugin;
		defaults = LightningBuilder.fromFile(new File(plugin.getDataFolder(), "default.yml")).addInputStream(plugin.getResource("default.yml")).createYaml();
	}

	@Override
	protected void onCommand(CommandContext con) {


		String label = con.args[0];
		con.tell("&aCreating command in config...");
		plugin.getCommandsConfig().setPathPrefix(null);
		for (String str : defaults.keySet("ExampleCommand")) {

			plugin.getCommandsConfig().set(label + "." + str, defaults.get("ExampleCommand." + str));

		}
		SingleCustomCommand scc = new SingleCustomCommand(label);
		plugin.getCommandsRegistrar().registerCommand(scc);
		con.tell("&aSuccessfully created command " + label + ", now you can configure it in commands.yml!");


	}
}
