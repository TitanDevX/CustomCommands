package me.titan.customcommands.core;

import me.titan.customcommands.cmd.CmdParent;
import me.titan.customcommands.cmd.Messages;
import me.titan.customcommands.config.CommandsConfig;
import me.titan.customcommands.config.ConditionsConfig;
import me.titan.customcommands.config.MainSettings;
import me.titan.customcommands.config.MessagesConfig;
import me.titan.customcommands.container.CustomCommand;
import me.titan.customcommands.listeners.PlayerListener;
import me.titan.customcommands.log.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CustomCommandsPlugin extends JavaPlugin {


	CommandsRegistrar commandsRegistrar;

	CommandsBoard commandsBoard;

	MainSettings mainSettings;

	CommandsConfig commandsConfig;

	MessagesConfig messagesConfig;

	ConditionsConfig conditionsConfig;
	CmdParent parentCmd;

	private static CustomCommandsPlugin instance;
	private final String supportedVersions = "1.8-1.16.5";

	public CommandsRegistrar getCommandsRegistrar() {
		return commandsRegistrar;
	}

	boolean shouldStopEnabling;

	long currentTime;
	private PluginUpdateManager updateManager;

	public CommandsBoard getCommandsBoard() {
		return commandsBoard;
	}

	public MessagesConfig getMessagesConfig() {
		return messagesConfig;
	}

	public static CustomCommandsPlugin getPlugin() {
		return instance;
	}

	@Override
	public void onEnable() {

		instance = this;


		Logger.getInstance().forceLog("Custom Commands plugin made with <3 by TitanDev, loading...");


		currentTime = System.currentTimeMillis();

		tryCatchThrow(() -> commandsRegistrar = new CommandsRegistrar(),
				new Throwable("An exception occurred while initialing the commands registrar" +
						", this might be because you are running an unsupported version!" +
						" supported versions: " + supportedVersions));
		if (shouldStopEnabling) return;

		tryCatchThrow(() -> commandsBoard = new CommandsBoard(),
				new Throwable("An exception occurred while initializing commands board."));
		if (shouldStopEnabling) return;
		tryCatchThrow(() -> mainSettings = new MainSettings(this),
				new Throwable("An exception occurred while initializing main config."));
		if (shouldStopEnabling) return;
		tryCatchThrow(() -> commandsConfig = new CommandsConfig(this),
				new Throwable("An exception occurred while initializing commands config."));
		if (shouldStopEnabling) return;
		tryCatchThrow(() -> messagesConfig = new MessagesConfig(this, Messages.class),
				new Throwable("An exception occurred while initializing messages config."));
		if (shouldStopEnabling) return;
		tryCatchThrow(() -> conditionsConfig = new ConditionsConfig(this),
				new Throwable("An exception occurred while initializing conditions config."));
		if (shouldStopEnabling) return;
		tryCatchThrow(() -> parentCmd = new CmdParent(this)
				, new Throwable("An exception occurred while initializing main command."));

		if (shouldStopEnabling) return;

		tryCatchThrow(() -> commandsRegistrar.registerCommand(parentCmd),
				new Throwable("An exception occurred while registering the main command!"));


		if (shouldStopEnabling) return;

		this.updateManager = PluginUpdateManager.init(this);



		Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);

		updateManager.getVersion((vr) -> {
			if(!updateManager.isUpToDate(vr)){
				Logger.getInstance().logEmpty("----------------------------------",
						"You are running an outdated version of CustomCommands!",
						"Please update to " + vr + "!","----------------------------------"
				);
			}
		});
		onPreEnable();

	}

	public PluginUpdateManager getUpdateManager() {
		return updateManager;
	}

	public void onPreEnable() {

		_$printLogo();
		long timeToEnable = (System.currentTimeMillis() - currentTime) / 1000L;
		Logger.getInstance().forceLog("Plugin successfully loaded in " + timeToEnable + "s.");

	}

	public ConditionsConfig getConditionsConfig() {
		return conditionsConfig;
	}

	public boolean checkPlaceHolderApiHook() {
		return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}

	public void tryCatchThrow(Runnable run, Throwable exp) {

		try {
			run.run();
		} catch (Exception p) {

			try {
				setEnabled(false);
				shouldStopEnabling = true;
				throw exp;
			} catch (Throwable e) {
				e.addSuppressed(p);
				e.printStackTrace();
			}
		}

	}

	public void unregisterAllCommands() {
		for (CustomCommand cmd : commandsBoard.values()) {
			commandsRegistrar.unregisterCommand(cmd.getTitanCommand());
		}
		commandsBoard.clear();
		commandsBoard.byId.clear();
	}

	public CommandsConfig getCommandsConfig() {
		//messagesConfig.init();
		return commandsConfig;
	}

	public CmdParent getParentCmd() {
		return parentCmd;
	}

	public void makeFile(String name, boolean extract) {

		if (extract) {
			saveResource(name, false);
			return;
		}
		File f = new File(this.getDataFolder(), name);
		if (!f.exists()) {
			f.mkdir();
		}

	}

	public void runLater(long delay, Runnable task) {

		Bukkit.getScheduler().runTaskLater(this, task, delay);

	}

	private final void _$printLogo() {
		System.out.println("\n" +
				"------------------------------------------------------------------------------------------------------" +
				" _____           _                    _____                                           _     \n" +
				"/  __ \\         | |                  /  __ \\                                         | |    \n" +
				"| /  \\/_   _ ___| |_ ___  _ __ ___   | /  \\/ ___  _ __ ___  _ __ ___   __ _ _ __   __| |___ \n" +
				"| |   | | | / __| __/ _ \\| '_ ` _ \\  | |    / _ \\| '_ ` _ \\| '_ ` _ \\ / _` | '_ \\ / _` / __|\n" +
				"| \\__/\\ |_| \\__ \\ || (_) | | | | | | | \\__/\\ (_) | | | | | | | | | | | (_| | | | | (_| \\__ \\\n" +
				" \\____/\\__,_|___/\\__\\___/|_| |_| |_|  \\____/\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|\\__,_|___/\n" +
				"                                                                                            \n" +
				"------------------------------------------------------------------------------------------------------");

	}

	@Override
	public void onDisable() {


	}

}
