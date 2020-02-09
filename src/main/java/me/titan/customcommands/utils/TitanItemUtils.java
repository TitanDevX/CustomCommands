package me.titan.customcommands.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.model.SimpleEnchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TitanItemUtils {
	public static String getItemName(ItemStack item) {
		String name = ItemUtil.bountify(item.getType().name());
		if (!item.hasItemMeta()) return name;
		if (!item.getItemMeta().hasDisplayName()) return name;
		return item.getItemMeta().getDisplayName();
	}

	public static List<String> getLore(ItemStack item) {
		List<String> lore = new ArrayList<>();
		if (!item.hasItemMeta()) return lore;
		if (!item.getItemMeta().hasLore()) return lore;
		return item.getItemMeta().getLore();
	}

	public static List<SimpleEnchant> getEnchants(ItemStack item) {
		List<SimpleEnchant> result = new ArrayList<>();
		if (item.getEnchantments().isEmpty()) return result;
		for (Map.Entry<Enchantment, Integer> en : item.getEnchantments().entrySet()) {
			result.add(new SimpleEnchant(en.getKey(), en.getValue()));
		}
		return result;
	}
}
