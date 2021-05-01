package me.titan.customcommands.cmd.cmdedit;

import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.TitanCommand;
import me.titan.customcommands.config.CommandsConfig;
import me.titan.customcommands.container.AdvancedCustomCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Arrays;

public class CmdAddReplyMsgCommand extends TitanCommand {
	public CmdAddReplyMsgCommand() {
		super("addReplyMessage");
		setDrag(2);
		addRequiredArgs("cmd");
		addRequiredArgs("to add");
	}

	@Override
	protected boolean onCommand(CommandContext con) {

		String cmds = con.args[0];
		if(CustomCommandsPlugin.getPlugin().getCommandsBoard().get(cmds.toLowerCase()) == null){
			con.tell("&cInvalid command.");
			return false;
		}
		AdvancedCustomCommand cmd = (AdvancedCustomCommand) CustomCommandsPlugin.getPlugin().getCommandsBoard().get(cmds.toLowerCase());

		String add = String.join(" ", Arrays.copyOfRange(con.args,1,con.args.length));
		cmd.getReplyMessages().add(add);

		CommandsConfig cconfig = CustomCommandsPlugin.getPlugin().getCommandsConfig();
		YamlConfiguration config = cconfig.getConfig();
		config.set(cmd.getName() + ".ReplyMessages", cmd.getReplyMessages());
		try { config.save(cconfig.getFile()); } catch (IOException e) { e.printStackTrace(); }

		con.tell("&aYou have added a reply message to the command " + cmds + ":","&a" + add);
		return true;
	}
}
