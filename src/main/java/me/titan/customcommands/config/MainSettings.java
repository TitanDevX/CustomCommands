package me.titan.customcommands.config;

import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.log.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MainSettings extends TitanConfig {

	boolean debug;

	boolean override;

	public MainSettings(JavaPlugin plugin) {
		super("config.yml", plugin);
		init();

	}

	@Override
	public void init() {
		debug = (boolean) getOrSetDefault("Debug", false);
		Logger.debug = debug;
		override = getBoolean("Override_Other_Commands");
		if (override) {
			CustomCommandsPlugin plugin = CustomCommandsPlugin.getPlugin();
			new BukkitRunnable() {
				@Override
				public void run() {
					plugin.getCommandsConfig().forceReload();
					plugin.getCommandsConfig().init();
					plugin.getMessagesConfig().forceReload();
					plugin.getMessagesConfig().init();
				}
			}.runTaskLater(CustomCommandsPlugin.getPlugin(), 20 * 5);
		}
	}
}
