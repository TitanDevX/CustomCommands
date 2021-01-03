package me.titan.customcommands.core;

import de.leonhard.storage.Json;
import me.titan.customcommands.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginUpdateManager {

	private static final List<String> features = Arrays.asList(
			"Added new required arg suffix: message suffix."
	);
	public static boolean updated = false;
	private final Json json;
	String lastVersion;

	private PluginUpdateManager(Json json) {
		this.json = json;
		String current = CustomCommandsPlugin.getPlugin().getDescription().getVersion();

		if (!json.contains("LastVersion")) {
			updated = true;
		} else {
			lastVersion = json.getString("LastVersion");
			int lastV = parseVersion(lastVersion);
			int currentVersion = parseVersion(current);
			if (lastV < currentVersion) {
				updated = true;
			}


		}
		json.set("LastVersion", current);
		if (updated) {
			Logger.getInstance().logEmpty(getFeaturesMessage().toArray(new String[0]));
		}


	}

	public static PluginUpdateManager init(CustomCommandsPlugin plugin) {
		Json json = new Json(new File(plugin.getDataFolder(), "plugindata.json"));
		return new PluginUpdateManager(json);
	}

	public static int parseVersion(String str) {
		int result = 100;
		if (str.contains("SNAPSHOT")) {
			str = str.replace("-SNAPSHOT", "");
			result = 0;
		}

		String[] dots = str.split("\\.");


		for (String s : dots) {
			result += Integer.parseInt(s);
		}
		return result;
	}

	public List<String> getFeaturesMessage() {

		String cv = CustomCommandsPlugin.getPlugin().getDescription().getVersion();
		List<String> msg = new ArrayList<>(Arrays.asList(
				"&6-----------------------------------------------------------------"
				, "&c&lThank you for updating the plugin!",
				"&6CustomCommands updated from &c" + lastVersion + " -> &b" + cv
				, "&e&lWhat's new in " + cv + ":"));
		msg.addAll(features);
		msg.add("&cNote: this message is only sent to players with permission customcommands.admin");
		msg.add("&6-----------------------------------------------------------------");

		return msg;

	}


}
