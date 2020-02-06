package me.titan.customcommands.commands;

import me.titan.customcommands.common.CustomCommand;
import me.titan.customcommands.core.CommandsManager;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SetPermissionCommand extends SimpleSubCommand {
	protected SetPermissionCommand() {
		super("perms");
		setMinArguments(2);
		setUsage("<Command> <Permission Node>");
	}

	@Override
	protected void onCommand() {

		String name= args[0];
		String perms = args[1];

		if(!CommandsManager.exists(name)){

			returnTell("&cThis command does not exists.");
		}

		CustomCommand cc = CommandsManager.get(name);
		cc.setPerms(perms);

		cc.reloadCommand();

		tell("&aSuccessfully set the perms to &c" + perms + "&a.");


	}
}
