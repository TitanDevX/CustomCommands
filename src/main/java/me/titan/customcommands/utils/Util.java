package me.titan.customcommands.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Util {

	public static List<String> toLowerCaseList(List<String> list) {
		List<String> s = new ArrayList<>();
		for (String str : list) {
			s.add(str.toLowerCase());
		}
		return s;

	}

	public static int getIntegerParsed(String fullStr, String splitation, int intIndex) {
		String[] s = fullStr.split(splitation);

		return Integer.parseInt(s[intIndex]);
	}

	public static String getStringIndex(String fullStr, String splitation, int intIndex) {
		String[] s = fullStr.split(splitation);

		return s[intIndex];
	}

	public static <E extends java.lang.Enum<E>> E getEnum(String name, Class<E> clazz) {
		name = name.replace(" ", "_");
		name = name.toUpperCase();


		return java.lang.Enum.valueOf(clazz, name);
	}

	public static boolean isBetween(String str, String str2, String del) {
		String[] parts = str.split(del);
		for (int i = 1; i < parts.length; i = i + 2) {
			String s = parts[i];
			if (s.equals(str2)) {
				return true;
			}

		}
		return false;
	}

	public static boolean isBetweenBrackets(String str, String str2, String del1, String del2) {
		if (!str.contains(del1) || !str.contains(del2)) return false;

		int f = str.indexOf(del1);
		int e = str.indexOf(del2);

		String n = str.substring(f, e);


		return n.equals(str2);
	}
	public static Location getLocation(String locs) {
		locs = locs.replace(" ", "");
		String[] parts = locs.split(",");
		double x = Util.toDouble(parts[0]);
		double y = Util.toDouble(parts[1]);
		double z = Util.toDouble(parts[2]);
		World w = Bukkit.getWorld(parts[3]);

		return new Location(w, x, y, z);
	}
	public static Float toFloat(String str) {
		return Float.parseFloat(str.replace("__", "."));
	}

	public static Double toDouble(String str) {
		return Double.parseDouble(str.replace("__", "."));
	}

	public static boolean isInteger(String raw) {
		try {

			Integer.parseInt(raw);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
//
//	public static ItemStack deserializeItemStack(ConfigurationSection s) {
//
//		String dn = s.getString("Display_Name");
//		CompMaterial type = CompMaterial.fromString(s.getString("Type"));
//		int data = s.getInt("Data");
//		List<String> strs = s.getStringList("Enchantments");
//		List<SimpleEnchant> enchantments = new ArrayList<>();
//
//		// PROTECTION-1
//		for (String enchs : strs) {
//			String[] parts = enchs.split("-");
//			Enchantment en = Enchantment.getByKey(NamespacedKey.minecraft(parts[0]));
//			int level = Integer.parseInt(parts[1]);
//
//			enchantments.add(new SimpleEnchant(en, level));
//		}
//		boolean glow = s.getBoolean("glow");
//		List<String> lore = s.getStringList("Lore");
//		return ItemCreator.of(type, dn).lores(lore).glow(glow).enchants(enchantments).damage(data).build().make();
//	}
//
//	public static String getItemName(ItemStack item) {
//		String name = ItemUtil.bountify(item.getType().name());
//		if (!item.hasItemMeta()) return name;
//		if (!item.getItemMeta().hasDisplayName()) return name;
//		return item.getItemMeta().getDisplayName();
//	}
//
//	public static SerializedMap serializeItemStack(ItemStack item) {
//
//		SerializedMap s = new SerializedMap();
//		String dn = getItemName(item);
//		boolean hasMeta = item.hasItemMeta();
//
//		if (hasMeta) {
//			if (item.getItemMeta().hasDisplayName()) {
//				s.put("Display_Name", dn);
//			}
//			if (item.getItemMeta().hasLore()) {
//				s.put("Lore", item.getItemMeta().getLore());
//			}
//		}
//		s.put("Type", CompMaterial.fromMaterial(item.getType()).toString());
//		if (item.getDurability() > 0) {
//			s.put("Data", (int) item.getDurability());
//		}
//		if (!item.getEnchantments().isEmpty()) {
//			List<String> strs = s.getStringList("Enchantments");
//			for (Map.Entry<Enchantment, Integer> en : item.getEnchantments().entrySet()) {
//				strs.add(en.getKey().getKey().getKey() + "-" + en.getValue());
//			}
//			s.put("Enchantments", strs);
//		}
//		s.put("Glow", !item.getEnchantments().isEmpty());
//
//
//		return s;
//	}
//
	/**
	 * @param source
	 * @param first
	 * @param second
	 * @return
	 */
	public static String getSubStringBetween(String source, String first, String second) {
		int index1 = source.indexOf(first);
		int index2 = source.lastIndexOf(second);

		return source.substring(index1, index2).replace("'", "");
	}

	public static void conoleError(String... msgs) {
		for (String gg : msgs) {
			Logger.getLogger("Minecraft").severe(gg);
		}
	}
}
