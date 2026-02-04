package me.titan.customcommands.cmd;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import me.titan.customcommands.Permissions;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanSubCommand;
import me.titan.customcommands.container.SingleCustomCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class CmdCmdCreate extends TitanSubCommand {

	final CustomCommandsPlugin plugin;
	public Yaml defaults;

	public CmdCmdCreate(CustomCommandsPlugin plugin) {
		super("createCommand");
		addAlias("createCmd", "newcmd");
		addRequiredArgs("label");

		setPermission(Permissions.CustomCommands_create.perm);
		this.plugin = plugin;
		File f= new File(plugin.getDataFolder(), "default.yml");

		if(f.exists()){
			f.delete();
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				defaults = LightningBuilder.fromFile(f).addInputStream(plugin.getResource("default.yml")).createYaml();

			}
		}.runTaskLater(plugin,5);
	}

	@Override
	protected void onCommand(CommandContext con) {


		String label = con.args[0];
		con.tell("&aCreating command in config...");
		plugin.getCommandsConfig().setPathPrefix(null);
		for (String str : defaults.keySet("ExampleCommand")) {

			plugin.getCommandsConfig().setNoSave(label + "." + str, defaults.get("ExampleCommand." + str));

		}
		plugin.getCommandsConfig().save();
		SingleCustomCommand scc = (SingleCustomCommand) CustomCommandsPlugin.getPlugin().getCommandsConfig().loadCommand(label,false,"");

		con.tell("&aSuccessfully created command " + label + ", now you can configure it in commands.yml!");


	}
}
