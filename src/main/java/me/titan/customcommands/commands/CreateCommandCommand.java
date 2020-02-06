package me.titan.customcommands.commands;

import me.titan.customcommands.common.CustomCommand;
import me.titan.customcommands.core.CommandsManager;
import org.mineacademy.fo.command.SimpleSubCommand;

public class CreateCommandCommand extends SimpleSubCommand {
	protected CreateCommandCommand() {
		super("create");
		setMinArguments(1);
		setUsage("<Name>");
	}

	@Override
	protected void onCommand() {

		String name= args[0];

		CommandsManager.register(new CustomCommand(name));

		tell("&aSuccessfully created a command: &c" + name + "&a. You can now edit it's settings via the config file." );


	}
}
