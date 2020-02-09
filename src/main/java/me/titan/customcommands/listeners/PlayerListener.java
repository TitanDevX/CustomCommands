package me.titan.customcommands.listeners;

import me.titan.customcommands.common.ItemCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if (item == null) return;

		ItemCommand ic = ItemCommand.getByItem(item);
		if (ic == null) return;
		ic.onClick(p);

	}
}
