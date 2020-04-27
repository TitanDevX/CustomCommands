package me.titan.customcommands.commands;

import me.titan.customcommands.core.CommandsManager;
import me.titan.customcommands.customcommands.CustomCommandsGroup;
import me.titan.customcommands.customcommands.CustomSubCommand;
import org.mineacademy.fo.command.SimpleSubCommand;

public class AddSubCommandsCommand extends SimpleSubCommand {
	protected AddSubCommandsCommand() {
		super("addSubCommands|addSubCmd|subcmd");
		setMinArguments(2);
		setUsage("<Name> <Sub Commands (split with ',')>");
	}

	@Override
	protected void onCommand() {

		String name = args[0];
		if (!CommandsManager.getInstance().commandsGroups.containsKey(name)) {
			returnTell("&cThis command doesn't exist.");
		}
		CustomCommandsGroup group = CommandsManager.getInstance().commandsGroups.get(name);


		String[] subs = args[1].split(",");


		for (String sub : subs) {

			CustomSubCommand subCommand = new CustomSubCommand(sub);
			group.getSubCommands().add(subCommand);
		}
		CommandsManager.register(group);
		returnTell("&aSuccessfully added sub commands: &c" + args[1].replace(",", ", ") + "&a to the commands group &c" + name + "&a.");


	}
}
