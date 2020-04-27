package me.titan.customcommands.titancommands;

import me.titan.customcommands.customcommands.CustomCommandsGroup;
import me.titan.customcommands.customcommands.CustomSubCommand;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.ArrayList;
import java.util.List;

public class TitanCommandGroup extends SimpleCommandGroup {

	CustomCommandsGroup customCommand;

	public TitanCommandGroup(CustomCommandsGroup cc) {


		this.customCommand = cc;


	}

	@Override
	protected void registerSubcommands() {

		for (CustomSubCommand csc : customCommand.getSubCommands()) {
			if (csc.getCommand().isRegistered()) continue;
			registerSubcommand(csc.getCommand());

		}
	}


	@Override
	protected String[] getNoParamsHeader() {
		List<String> gg = new ArrayList<>();
		for (String g : customCommand.getNoParamsMsg()) {

			gg.add(g
					.replace("{ChatLine}", Common.chatLine()
							.replace("{PluginName}", SimplePlugin.getNamed()
									.replace("{Trademark}", "&8\u2122")
									.replace("{PluginVersion}", SimplePlugin.getVersion())
									.replace("{Credits}", getCredits())
									.replace("{ChatLineSmooth}", Common.chatLineSmooth()))));

		}

		return (String[]) gg.toArray();
	}

	@Override
	protected String[] getHelpHeader() {
		List<String> gg = new ArrayList<>();
		for (String g : customCommand.getNoParamsMsg()) {

			gg.add(g
					.replace("{ChatLine}", Common.chatLine()
							.replace("{PluginName}", SimplePlugin.getNamed()
									.replace("{Trademark}", "&8\u2122")
									.replace("{PluginVersion}", SimplePlugin.getVersion())
									.replace("{Credits}", getCredits())
									.replace("{ChatLineSmooth}", Common.chatLineSmooth()))));

		}

		return (String[]) gg.toArray();
	}
}
