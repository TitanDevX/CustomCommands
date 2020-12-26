package me.titan.customcommands.config;

import me.titan.customcommands.log.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSettings extends TitanConfig {

	boolean debug;

	public MainSettings(JavaPlugin plugin) {
		super("config.yml", plugin);
		init();

	}

	@Override
	public void init() {
		debug = getOrSetDefault("Debug", false);
		Logger.debug = debug;
	}
}
