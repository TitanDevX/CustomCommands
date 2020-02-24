package me.titan.customcommands.core;

import lombok.Getter;
import me.titan.customcommands.customcommands.CustomCommand;
import me.titan.customcommands.customcommands.CustomCommandsGroup;
import me.titan.customcommands.customcommands.CustomSubCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.remain.Remain;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandsManager {
	@Getter
	private final static CommandsManager instance = new CommandsManager();

	public Map<String, CustomCommand> commands = new HashMap<>();

	public Map<String, CustomCommandsGroup> commandsGroups = new HashMap<>();

	public static boolean exists(String name){
		return getInstance().commands.containsKey(name);
	}
	public static CustomCommand get(String name){
		return getInstance().commands.get(name);
	}
	public static void register(CustomCommand command){
		instance.commands.put(command.getName(),command);


		Common.runLater(10,() ->{
			command.setup();

			File f = FileUtil.getOrMakeFile("commands.yml");
			YamlConfiguration yc = FileUtil.loadConfigurationStrict(f);

			command.saveData(Objects.requireNonNull(yc.getConfigurationSection("Commands")));
			try {
				yc.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void register(CustomCommandsGroup command) {
		instance.commandsGroups.put(command.getName(), command);

		for (CustomSubCommand cmd : command.getSubCommands()) {
			cmd.setMainCommand(command);
		}

		Common.runLater(10, () -> {
			command.setup();

			File f = FileUtil.getOrMakeFile("commands.yml");
			YamlConfiguration yc = FileUtil.loadConfigurationStrict(f);

			command.saveData(Objects.requireNonNull(yc.getConfigurationSection("Commands")));
			try {
				yc.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void reload(CustomCommand cc){
		CommandsManager.getInstance().commands.remove(cc.getName());
		Remain.unregisterCommand(cc.getName(),true);

		CommandsManager.register(cc);
	}

	public static void reload(CustomCommandsGroup cc) {
		CommandsManager.getInstance().commandsGroups.remove(cc.getName());
		Remain.unregisterCommand(cc.getName(), true);

		CommandsManager.register(cc);
	}

	public static void reloadSubCommand(CustomCommand cc) {
		CommandsManager.getInstance().commands.remove(cc.getName());
		Remain.unregisterCommand(cc.getName(), true);

		CommandsManager.register(cc);
	}

}
