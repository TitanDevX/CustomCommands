package me.titan.customcommands.core;

import de.leonhard.storage.Json;
import me.titan.customcommands.log.Logger;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class PluginUpdateManager {

	private static final List<String> features = Arrays.asList(
			"Added Command cool down."
			, "Added command conditions.",
			"Bug fixing."
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
	private static final int resourceId =74642;

	public void getVersion(final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(CustomCommandsPlugin.getPlugin(), () -> {
			try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource="
					+ resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {

				if (scanner.hasNext()) {
					consumer.accept(scanner.next());
				}
			} catch (IOException exception) {
				CustomCommandsPlugin.getPlugin().getLogger().info("Cannot look for updates: " + exception.getMessage());
			}
		});
	}

	public boolean isUpToDate(String vr){
		String current = CustomCommandsPlugin.getPlugin().getDescription().getVersion();
		int currentVersion = parseVersion(current);

		int ver = parseVersion(vr);
		return currentVersion >= ver;
	}
	public static int parseVersion(String str) {

		int result = 100;
		if (str.contains("SNAPSHOT")) {
			str = str.replace("-SNAPSHOT", "");
			result = 0;
		}

		String[] dots = str.split("\\.");


		int i = dots.length;
		for (String s : dots) {
			if(i == dots.length){
				result += Integer.parseInt(s) * 10;
			}else
			 result += Integer.parseInt(s) * i;

			i--;
		}
		return result;
	}

	public List<String> getFeaturesMessage() {
		//https://www.spigotmc.org/resources/customcommands-2-1-8-1-16.74642/
		String cv = CustomCommandsPlugin.getPlugin().getDescription().getVersion();
		List<String> msg = new ArrayList<>(Arrays.asList(
				"&6-----------------------------------------------------------------"
				, "&c&lThank you for updating the plugin!",
				"&6CustomCommands updated from &c" + lastVersion + " -> &b" + cv
				, "&e&lWhat's new in " + cv + ":"));
		msg.addAll(features);
		msg.add("");
		msg.add("");
		msg.add("&cNote: this message is only sent to players with permission customcommands.admin");
		msg.add("&6-----------------------------------------------------------------");

		return msg;

	}


}
