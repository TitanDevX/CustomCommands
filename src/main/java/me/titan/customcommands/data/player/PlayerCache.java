package me.titan.customcommands.data.player;

import de.leonhard.storage.Json;
import me.titan.customcommands.container.AdvancedCustomCommand;
import me.titan.customcommands.container.CustomCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache {


	UUID id;

	public static Map<UUID, PlayerCache> players = new HashMap<>();

	public static Json json;
	Map<Integer, Integer> commandUses = new HashMap<>();

	Map<Integer, Long> commandCooldowns = new HashMap<>();

	static {
		json = new Json(new File(CustomCommandsPlugin.getPlugin().getDataFolder(), "data/players.json"));

		File oldStorage = new File(CustomCommandsPlugin.getPlugin().getDataFolder(), "players.json");
		if (oldStorage.exists()) {
			Json oj = new Json(oldStorage);
			for (String d : oj.singleLayerKeySet("")) {
				json.set(d, oj.get(d));
			}
		}
		oldStorage.delete();
	}

	public PlayerCache(UUID id) {
		this.id = id;

		loadData();

	}

	public static PlayerCache getPlayerCache(UUID id) {
		PlayerCache pc = players.get(id);
		if (pc == null) {
			pc = new PlayerCache(id);
			players.put(id, pc);
		}
		return pc;
	}

	public static PlayerCache getPlayerCache(Player p) {
		return getPlayerCache(p.getUniqueId());
	}

	public void loadData() {
		json.setPathPrefix(null);
		if (!json.contains(id.toString())) return;

		json.setPathPrefix(id.toString());
		for (String str : json.singleLayerKeySet(json.getPathPrefix() + ".commandUses")) {
			int cmdId = Integer.parseInt(str);
			commandUses.put(cmdId, json.getInt("commandUses." + str));
		}
		for (String str : json.singleLayerKeySet(json.getPathPrefix() + ".commandCooldowns")) {
			int cmdId = Integer.parseInt(str);
			commandCooldowns.put(cmdId, json.getLong("commandCooldowns." + str));
		}
	}

	public void saveData() {

		json.setPathPrefix(id.toString());
		for (Map.Entry<Integer, Integer> en : commandUses.entrySet()) {
			json.set("commandUses." + en.getKey(), en.getValue());
		}
		for (Map.Entry<Integer, Long> en : commandCooldowns.entrySet()) {
			json.set("commandCooldowns." + en.getKey(), en.getValue());
		}
	}

	public long getCooldown(CustomCommand cmd){
		return commandCooldowns.getOrDefault(cmd.getId(),0L);
	}

	public int getUses(CustomCommand cmd) {
		return commandUses.getOrDefault(cmd.getId(), 0);
	}

	public int getUsesAd(AdvancedCustomCommand cmd) {
		Player p = Bukkit.getPlayer(id);
		int cuses = cmd.getUses();
		if (cmd.getUsesPerPermission() != null) {
			for (Map.Entry<String, Integer> en : cmd.getUsesPerPermission().entrySet()) {
				if (p.hasPermission(en.getKey())) {
					cuses = en.getValue();
					break;
				}
			}
		}
		return cuses;
	}

	public void setUses(CustomCommand cmd, int uses) {
		commandUses.put(cmd.getId(), uses);
		saveData();
	}

	public void IncreaseUses(CustomCommand cmd) {
		setUses(cmd, getUses(cmd) + 1);
	}

	public Map<Integer, Long> getCommandCooldowns() {
		return commandCooldowns;
	}
	public long checkCooldown(AdvancedCustomCommand cmd) {

		Player p = Bukkit.getPlayer(id);
		if(p.hasPermission(cmd.getName().toLowerCase() + ".cooldown.bypass")) return -1;
		long cooldown = commandCooldowns.getOrDefault(cmd.getId(), -1L);

		if(cooldown == -1L) return -1;
		// 10 - 5
		long current = System.currentTimeMillis()/1000;
		long dif = current-cooldown;
		System.out.println("COOL " + cmd.getCooldown() + " "+ current + " " + cooldown + " " + dif + " " + commandCooldowns);
		if(dif > cmd.getCooldown()){
			return -1;
		}else
		 return cmd.getCooldown()-dif;
	}
	public boolean checkUses(AdvancedCustomCommand cmd) {

		int uses = getUses(cmd);
		int cuses = getUsesAd(cmd);
		if (cuses == -1) return true;
			return cuses > uses;

	}
}
