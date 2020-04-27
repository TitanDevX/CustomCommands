package me.titan.customcommands.core;

import me.titan.customcommands.commands.CustomCommandsCommands;
import me.titan.customcommands.common.ItemCommand;
import me.titan.customcommands.configurations.CommandsConfig;
import me.titan.customcommands.configurations.Config;
import me.titan.customcommands.configurations.ItemsConfig;
import me.titan.customcommands.configurations.MessagesConfig;
import me.titan.customcommands.customcommands.CustomCommand;
import me.titan.customcommands.listeners.PlayerListener;
import me.titan.customcommands.titancommands.TitanCommand;
import me.titan.customcommands.titancommands.TitanCommandGroup;
import me.titan.lib.TitanLib;
import org.mineacademy.fo.FileUtil;
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


		registerEvents(new PlayerListener());
		FileUtil.getOrMakeFile("log.log");

		FileUtil.extract("codes.yml");

	}

	@Override
	protected void onPluginStop() {
		getInstance = null;

		for (ItemCommand item : ItemCommand.items.values()) {
			item.save();
		}
	}

	public void registerTitanCommand(TitanCommand cmd){
		registerCommand(cmd);

	}

	public void registerTitanCommandGroup(TitanCommandGroup cmd, String name, List<String> aliases) {
		registerCommands(name, aliases, cmd);
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
		return Arrays.asList(Config.class, CommandsConfig.class, ItemsConfig.class, MessagesConfig.class);
	}

	@Override
	public SimpleCommandGroup getMainCommand() {
		return mainCmd;
	}
}
