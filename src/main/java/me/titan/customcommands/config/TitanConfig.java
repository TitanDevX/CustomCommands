package me.titan.customcommands.config;

import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TitanConfig extends Config {


	public TitanConfig(String fileName, JavaPlugin plugin) {
		super(LightningBuilder.fromFile(new File(plugin.getDataFolder(), fileName)).addInputStream(plugin.getResource(fileName)).createConfig());
	}

	public void init() {
	}
}
