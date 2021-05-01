package me.titan.customcommands.cmd.cmdedit;

import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.CommandTarget;
import me.titan.customcommands.cmd.lib.TitanCommand;
import me.titan.customcommands.config.CommandsConfig;
import me.titan.customcommands.container.AdvancedCustomCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.utils.Util;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class CmdSetWhoCanUse extends TitanCommand {
	public CmdSetWhoCanUse() {
		super("setWhoCanUse");
		setDrag(2);
		addRequiredArgs("cmd");
		addRequiredArgs("whoCanUse");
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
		CommandTarget t = Util.getEnum(add,CommandTarget.class);
		if(t == null){
			con.tell("&cInvalid command target, should be either player, console or all.");


			return false;
		}
		cmd.setTarget(t);

		CommandsConfig cconfig = CustomCommandsPlugin.getPlugin().getCommandsConfig();
		YamlConfiguration config = cconfig.getConfig();
		config.set(cmd.getName() + ".Who_Can_Use_Command", cmd.getTarget().name());
		try { config.save(cconfig.getFile()); } catch (IOException e) { e.printStackTrace(); }
		con.tell("&aYou have set the command " + cmds + "'s target to &c" + add + "&a." );
		return true;
	}
}
