package me.titan.customcommands.commands;

import me.titan.customcommands.core.CommandsManager;
import me.titan.customcommands.customcommands.CustomCommand;
import me.titan.customcommands.customcommands.CustomCommandsGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.remain.Remain;

public class DeleteCommandCommand extends SimpleSubCommand {
	protected DeleteCommandCommand() {
		super("delete");
		setMinArguments(1);
		setUsage("<Name>");
	}

	@Override
	protected void onCommand() {

		String name = args[0];

		if (CommandsManager.getInstance().commands.containsKey(name)) {
			CustomCommand cc = CommandsManager.getInstance().commands.get(name);
			CommandsManager.getInstance().commands.remove(cc.getName());
			Remain.unregisterCommand(cc.getName(), true);

			tell("&aDeleted the custom command &c" + name + "&a.");
		} else if (CommandsManager.getInstance().commandsGroups.containsKey(name)) {
			CustomCommandsGroup cc = CommandsManager.getInstance().commandsGroups.get(name);
			CommandsManager.getInstance().commandsGroups.remove(cc.getName());
			if (cc.getCommand().isRegistered()) {
				cc.getCommand().unregister();
			}
			tell("&aDeleted the custom command &c" + name + "&a with its subcommands.");

		} else {
			returnTell("&cThis command does not exist.");
		}


	}
}
