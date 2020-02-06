package me.titan.customcommands.commands;

import me.titan.customcommands.common.CustomCommand;
import me.titan.customcommands.core.CommandsManager;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SetMinArgs extends SimpleSubCommand {
	protected SetMinArgs() {
		super("minargs");
		setMinArguments(2);
		setUsage("<Command> <Min Arguments>");
	}

	@Override
	protected void onCommand() {

		String name= args[0];
		int min = findNumber(1,"&cInvalid number.");

		if(!CommandsManager.exists(name)){

			returnTell("&cThis command does not exists.");
		}

		CustomCommand cc = CommandsManager.get(name);
		cc.setMinArgs(min);
		cc.reloadCommand();

		tell("&aSuccessfully set the min arguments to &c" + min+ "&a.");


	}
}
