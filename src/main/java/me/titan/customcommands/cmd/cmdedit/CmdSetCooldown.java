package me.titan.customcommands.cmd.cmdedit;

import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanCommand;
import me.titan.customcommands.config.CommandsConfig;
import me.titan.customcommands.container.AdvancedCustomCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.utils.TimeUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class CmdSetCooldown extends TitanCommand {
	public CmdSetCooldown() {
		super("setCooldown");
		setDrag(2);
		addRequiredArgs("cmd");
		addRequiredArgs("cooldown");
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
		cmd.setCooldown(TimeUtil.parseToken(add)/1000);

		CommandsConfig cconfig = CustomCommandsPlugin.getPlugin().getCommandsConfig();
		YamlConfiguration config = cconfig.getConfig();
		config.set(cmd.getName() + ".Cooldown", cmd.getCooldown());
		try { config.save(cconfig.getFile()); } catch (IOException e) { e.printStackTrace(); }

		con.tell("&aYou have set the command " + cmds + "'s cooldown to &c" + add + "&a." );
		return true;
	}
}
