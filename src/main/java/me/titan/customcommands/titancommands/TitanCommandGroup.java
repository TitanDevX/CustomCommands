package me.titan.customcommands.titancommands;

import me.titan.customcommands.customcommands.CustomCommandsGroup;
import me.titan.customcommands.customcommands.CustomSubCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class TitanCommandGroup extends SimpleCommandGroup {

	CustomCommandsGroup customCommand;

	public TitanCommandGroup(CustomCommandsGroup cc) {


		this.customCommand = cc;

	}

	@Override
	protected void registerSubcommands() {

		for (CustomSubCommand csc : customCommand.getSubCommands()) {
			registerSubcommand(csc.getCommand());

		}
	}
}
