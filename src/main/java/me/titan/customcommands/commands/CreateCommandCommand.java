package me.titan.customcommands.commands;

import me.titan.customcommands.core.CommandsManager;
import me.titan.customcommands.customcommands.CustomCommand;
import me.titan.customcommands.customcommands.CustomCommandsGroup;
import me.titan.customcommands.customcommands.CustomSubCommand;
import org.mineacademy.fo.command.SimpleSubCommand;

public class CreateCommandCommand extends SimpleSubCommand {
	protected CreateCommandCommand() {
		super("create");
		setMinArguments(1);
		setUsage("<Name> [Sub Commands (split with ',')]");
	}

	@Override
	protected void onCommand() {

		String name= args[0];

		if (args.length > 1) {
			String[] subs = args[1].split(",");
			CustomCommandsGroup group = new CustomCommandsGroup(name);


			for (String sub : subs) {

				CustomSubCommand subCommand = new CustomSubCommand(sub);
				group.getSubCommands().add(subCommand);
			}
			CommandsManager.register(group);
			returnTell("&aSuccessfully created a commands group &c(" + name + ")&a with sub commands: &c" + args[1].replace(",", ", ") + ".");
		}

		CommandsManager.register(new CustomCommand(name));

		tell("&aSuccessfully created a command: &c" + name + "&a. You can now edit it's settings via the config file." );


	}
}
