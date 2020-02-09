package me.titan.customcommands.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.SimpleEnchant;
import org.mineacademy.fo.remain.CompMaterial;

import javax.annotation.RegEx;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Util {

	public static List<String> toLowerCaseList(List<String> list){
		List<String> s = new ArrayList<>();
		for(String str : list){
			s.add(str.toLowerCase());
		}
		return s;

	}

	public static int getIntegerParsed(String fullStr, @RegEx String splitation, int intIndex ){
		String[] s = fullStr.split(splitation);

		return Integer.parseInt(s[intIndex]);
	}
	public static String getStringIndex(String fullStr, @RegEx String splitation, int intIndex ){
		String[] s = fullStr.split(splitation);

		return s[intIndex];
	}
	public static <E extends Enum<E>> E getEnum(String name, Class<E> clazz){
		name = name.replace(" ", "_");
		name = name.toUpperCase();

		return Enum.valueOf(clazz, name);
	}

	public static ItemStack deserializeItemStack(ConfigurationSection s) {

		String dn = s.getString("Display_Name");
		CompMaterial type = CompMaterial.fromString(s.getString("Type"));
		int data = s.getInt("Data");
		List<String> strs = s.getStringList("Enchantments");
		List<SimpleEnchant> enchantments = new ArrayList<>();

		// PROTECTION-1
		for (String enchs : strs) {
			String[] parts = enchs.split("-");
			Enchantment en = Enchantment.getByKey(NamespacedKey.minecraft(parts[0]));
			int level = Integer.parseInt(parts[1]);

			enchantments.add(new SimpleEnchant(en, level));
		}
		boolean glow = s.getBoolean("glow");
		List<String> lore = s.getStringList("Lore");
		return ItemCreator.of(type, dn).lores(lore).glow(glow).enchants(enchantments).damage(data).build().make();
	}

	public static String getItemName(ItemStack item) {
		String name = ItemUtil.bountify(item.getType().name());
		if (!item.hasItemMeta()) return name;
		if (!item.getItemMeta().hasDisplayName()) return name;
		return item.getItemMeta().getDisplayName();
	}

	public static SerializedMap serializeItemStack(ItemStack item) {

		SerializedMap s = new SerializedMap();
		String dn = getItemName(item);
		boolean hasMeta = item.hasItemMeta();

		if (hasMeta) {
			if (item.getItemMeta().hasDisplayName()) {
				s.put("Display_Name", dn);
			}
			if (item.getItemMeta().hasLore()) {
				s.put("Lore", item.getItemMeta().getLore());
			}
		}
		s.put("Type", CompMaterial.fromMaterial(item.getType()).toString());
		if (item.getDurability() > 0) {
			s.put("Data", (int) item.getDurability());
		}
		if (!item.getEnchantments().isEmpty()) {
			List<String> strs = s.getStringList("Enchantments");
			for (Map.Entry<Enchantment, Integer> en : item.getEnchantments().entrySet()) {
				strs.add(en.getKey().getKey().getKey() + "-" + en.getValue());
			}
			s.put("Enchantments", strs);
		}
		s.put("Glow", !item.getEnchantments().isEmpty());


		return s;
	}
}
