package me.titan.customcommands.configurations;

import me.titan.customcommands.common.CustomCommandsReader;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlStaticConfig;

public class CommandsConfig extends YamlStaticConfig {
	private final static void init() {
		Common.log("Registering Custom Commands...");

		pathPrefix("Commands");

		CustomCommandsReader.readCommands(getConfig().getConfigurationSection("Commands"));

	}

	@Override
	protected void load() throws Exception {
		this.createFileAndLoad("commands.yml");
	}
}
