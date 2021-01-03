package me.titan.customcommands.listeners;

import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.core.PluginUpdateManager;
import me.titan.customcommands.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		if (p.hasPermission("customcommands.admin") && PluginUpdateManager.updated) {

			Common.tell(p, CustomCommandsPlugin.getPlugin().getUpdateManager().getFeaturesMessage());
		}
	}
}
