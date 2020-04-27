package me.titan.customcommands.commands;

import org.mineacademy.fo.command.ReloadCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class CustomCommandsCommands extends SimpleCommandGroup {
	@Override
	protected void registerSubcommands() {
	registerSubcommand(new CreateCommandCommand());
	registerSubcommand(new ReloadCommand());
	registerSubcommand(new SetMinArgs());
	registerSubcommand(new SetPermissionCommand());
		registerSubcommand(new SetItemCommand());
		registerSubcommand(new GiveItemCommand());
		registerSubcommand(new AddSubCommandsCommand());
		registerSubcommand(new DeleteCommandCommand());

	}
}
