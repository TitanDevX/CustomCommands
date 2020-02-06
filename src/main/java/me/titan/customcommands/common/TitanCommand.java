package me.titan.customcommands.common;

import me.titan.customcommands.code.CodeReader;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;
import org.mineacademy.fo.command.SimpleCommand;

public class TitanCommand extends SimpleCommand {
	CustomCommand cc ;
	protected TitanCommand(CustomCommand cc) {
		super(cc.name, Util.toLowerCaseList(cc.getAliases()));

		this.cc = cc;

			setPermission(cc.getPerms());
			if(cc.getDelayObjects() != null) {
				setCooldown(cc.getDelayObjects().getFirst(), cc.getDelayObjects().getSecond());
			}
			setUsage(cc.getUsage());
			setMinArguments(cc.getMinArgs());

	}

	@Override
	protected void onCommand() {

		checkConsole();

		if(args.length < cc.getMinArgs()){

			returnTell("&cUsage: /" + cc.getUsage() + ".");
		}


		for(String cmd : cc.getPerformCommands()){
			cmd = CustomCommandsReader.replacePlaceholders(cmd,args,getPlayer(), "/" + cc.getUsage());
			if(cmd == null) continue;

			if(cmd.contains("/")){
				getPlayer().performCommand(cmd);
			}else{
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
			}
		}
		for(String msg : cc.getReplyMessages()){
			tell(msg);
		}
		CodeReader.performCode(cc.getCodes(),getPlayer());
	}
}
