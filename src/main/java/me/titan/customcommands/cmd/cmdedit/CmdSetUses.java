package me.titan.customcommands.cmd.cmdedit;

import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanCommand;
import me.titan.customcommands.config.CommandsConfig;
import me.titan.customcommands.container.AdvancedCustomCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class CmdSetUses extends TitanCommand {
	public CmdSetUses() {
		super("setUses");
		setDrag(2);
		addRequiredArgs("cmd");
		addRequiredArgs("uses");
	}

	@Override
	protected boolean onCommand(CommandContext con) {

		String cmds = con.args[0];
		if(CustomCommandsPlugin.getPlugin().getCommandsBoard().get(cmds.toLowerCase()) == null){
			con.tell("&cInvalid command.");
			return false;
		}
		AdvancedCustomCommand cmd = (AdvancedCustomCommand) CustomCommandsPlugin.getPlugin().getCommandsBoard().get(cmds.toLowerCase());

		String add = con.args[1];
		int uses = Integer.parseInt(add);
		cmd.setUses(uses);

		CommandsConfig cconfig = CustomCommandsPlugin.getPlugin().getCommandsConfig();
		YamlConfiguration config = cconfig.getConfig();
		config.set(cmd.getName() + ".Uses", cmd.getUses());
		try { config.save(cconfig.getFile()); } catch (IOException e) { e.printStackTrace(); }
		con.tell("&aYou have set the command " + cmds + "'s max uses to &c" + add + "&a." );
		return true;
	}
}
