package me.titan.customcommands.core;

import me.titan.customcommands.commands.CustomCommandsCommands;
import me.titan.customcommands.common.Config;
import me.titan.customcommands.common.CustomCommand;
import me.titan.customcommands.common.TitanCommand;
import me.titan.lib.TitanLib;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class CustomCommandsPlugin extends SimplePlugin {
	public static CustomCommandsPlugin getInstance;

	CustomCommandsCommands mainCmd = new CustomCommandsCommands();
	@Override
	protected void onPluginStart() {
		getInstance = this;
		TitanLib.setPlugin(this);



	}

	@Override
	protected void onPluginStop() {
		getInstance = null;
	}

	public void registerTitanCommand(TitanCommand cmd){
		registerCommand(cmd);
	}

	@Override
	protected void onPluginReload() {

		for(CustomCommand cc : CommandsManager.getInstance().commands.values()){
			Remain.unregisterCommand(cc.getName(),true);
		}
		for(CustomCommand cc : CommandsManager.getInstance().commands.values()) {
			cc.setup();
		}

		}

	@Override
	public List<Class<? extends YamlStaticConfig>> getSettings() {
		return Arrays.asList(Config.class);
	}

	@Override
	public SimpleCommandGroup getMainCommand() {
		return mainCmd;
	}
}
