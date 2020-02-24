package me.titan.customcommands.customcommands;

import lombok.Getter;
import lombok.Setter;
import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.titancommands.TitanCommandGroup;
import org.bukkit.configuration.ConfigurationSection;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomCommandsGroup extends YamlSectionConfig {

	String name;

	List<String> aliases = new ArrayList<>();

	List<CustomSubCommand> subCommands = new ArrayList<>();
	TitanCommandGroup command;


	public CustomCommandsGroup(String sectionPrefix) {
		super(sectionPrefix);
		this.name = sectionPrefix;


	}

	public void saveData(ConfigurationSection sec) {

		sec.set(getName() + ".Aliases", aliases);
		for (CustomSubCommand csc : subCommands) {
			csc.saveData(sec);
		}
	}

	public void setup() {

		for (CustomSubCommand csc : subCommands) {

			csc.setup();
		}
		this.command = new TitanCommandGroup(this);

		CustomCommandsPlugin.getInstance.registerTitanCommandGroup(command, name, aliases);

	}
}
