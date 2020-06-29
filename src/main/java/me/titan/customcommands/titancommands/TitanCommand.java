package me.titan.customcommands.titancommands;

import me.titan.customcommands.common.CustomCommandsReader;
import me.titan.customcommands.configurations.MessagesConfig;
import me.titan.customcommands.customcommands.CustomCommand;
import me.titan.customcommands.logger.Logger;
import me.titan.customcommands.newCode.NewCodeReader;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;
import org.mineacademy.fo.command.SimpleCommand;

public class TitanCommand extends SimpleCommand implements ITitanCommand {
	CustomCommand cc ;

	public TitanCommand(CustomCommand cc) {
		super(cc.getName(), Util.toLowerCaseList(cc.getAliases()));

		this.cc = cc;

			setPermission(cc.getPerms());

		setPermissionMessage(getPermissionMessage().equalsIgnoreCase("none") ? "" : getPermissionMessage());




			if(cc.getDelayObjects() != null) {
				setCooldown(cc.getDelayObjects().getFirst(), cc.getDelayObjects().getSecond());
			}
			setUsage(cc.getUsage());
			setMinArguments(cc.getMinArgs());

	}

	@Override
	protected void onCommand() {

		checkConsole();
		Logger.log("TitanCommand, onCommand:36", "Executing command " + cc.getName() + " for player " + getPlayer().getName());

		if(args.length < cc.getMinArgs()){

			if (!MessagesConfig.usageMsg.equalsIgnoreCase("none")) {
				returnTell(MessagesConfig.usageMsg.replace("{Usage}", cc.getUsage()));
			}
			return;
		}


		for(String cmd : cc.getPerformCommands()){

			cmd = CustomCommandsReader.replacePlaceholders(cmd, args, getPlayer(), "/" + cc.getUsage());
			if (cmd == null) continue;

			if (cmd.contains("/")) {
				getPlayer().performCommand(cmd.replaceFirst("/", ""));
			} else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
			Logger.log("TitanCommand, onCommand:60", "Executed command " + cmd + " fYor player " + getPlayer().getName());

		}
		for(String msg : cc.getReplyMessages()){
			tell(msg);
		}

		NewCodeReader ncr = new NewCodeReader(cc, cc.getName(), getPlayer(), args);
		for (String cmd : cc.getCodes()) {
			if (!cc.getCodeMethods().containsKey(cmd)) {
				cc.getCodeMethods().get(cmd).apply(getPlayer());
				continue;
			}
			ncr.readCode(cmd);
		}

	}


}
