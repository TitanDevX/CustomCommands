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
			"Fixed bugs with command cooldown."
			, "Added custom commands in game edit commands!",
			"Changed default commands.yml",
			"Added bypass permission for cmd cooldown.",
			"Other Bugs fixing."
	);
	public static boolean updated = false;
	private final Json json;
	String lastVersion;

	private PluginUpdateManager(Json json) {
		this.json = json;
		String current = CustomCommandsPlugin.getPlugin().getDescription().getVersion();

		updated = false;
		if (!json.contains("LastVersion")) {
			updated = true;
		} else {
			lastVersion = json.getString("LastVersion");
			int c = compare(lastVersion, current);
			System.out.println("V " + c + " " + updated);
			if (c == -1) {
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


		int c = compare(current, vr);
		return c >= 0;
	}
	public static int compare(String v1, String v2){

		String[] args1 = v1.split("\\.");
		String[] args2 = v2.split("\\.");

		int l1 = args1.length-1;
		int l2 = args2.length-1;
		// 2.3.1 - 2
		// 2.3 - 1
		// 2.1.1 - 2
		for(int i =0;i<5;i++){
			if(l1 < i && l2 >= i){
				return -1;
			}else if(l2 < i && l1 >= i){
				return 1;
			}
			if(l2 < i && l1 < i){
				return 0;
			}
			int arg1 = Integer.parseInt(args1[i]);
			int arg2 = Integer.parseInt(args2[i]);

				if(arg1 > arg2){
					return 1;
				}else if(arg2 > arg1){
					return -1;
				}

		}
		return 0;

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
