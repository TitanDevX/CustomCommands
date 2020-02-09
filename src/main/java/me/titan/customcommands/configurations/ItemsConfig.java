package me.titan.customcommands.configurations;

import me.titan.customcommands.common.ItemCommand;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlStaticConfig;

public class ItemsConfig extends YamlStaticConfig {
	private final static void init() {
		Common.log("Registering Items...");

		ItemCommand.items.clear();

		if (getConfig().getConfigurationSection("Items") == null) return;

		for (String name : getConfig().getConfigurationSection("Items").getKeys(false)) {
			ItemCommand.fromSection(name, getConfig().getConfigurationSection("Items." + name));
		}


	}

	@Override
	protected void load() throws Exception {
		this.createFileAndLoad("items.yml");
	}
}
