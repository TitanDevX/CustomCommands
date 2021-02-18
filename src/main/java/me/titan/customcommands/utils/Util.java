package me.titan.customcommands.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Util {
	private static final long MIN = 60;
	private static final long HOURS =MIN*60;
	private static final long DAYS = HOURS*24;
	private static final long WEEKS = DAYS*7;
	private static final long MONTHS = WEEKS*4;
	private static final long YEARS = MONTHS*12;
	public static List<String> toLowerCaseList(List<String> list) {
		List<String> s = new ArrayList<>();
		for (String str : list) {
			s.add(str.toLowerCase());
		}
		return s;

	}
	public static String formatTime(long seconds){
		long second = seconds % 60L;
		long minute = seconds / 60L;
		String hourMsg = "";
		if (minute >= 60L) {
			long hour = seconds / 60L / 60L;
			minute %= 60L;
			hourMsg = hour + (hour == 1L ? " hour" : " hours") + " ";
		}

		return hourMsg + (minute != 0L ? minute : "") + (minute > 0L ? (minute == 1L ? " minute" : " minutes") + " " : "") + Long.parseLong(String.valueOf(second)) + (Long.parseLong(String.valueOf(second)) == 1L ? " second" : " seconds");

	}
	public static long toSecondsFromHumanFormatShort(String string){

		// 1h. 1h,2min
		if(isInteger(string)){
			return Long.parseLong(string);
		}

		String str = string.replace(" ","");
		List<String> strsToCheck = new ArrayList<>();
		if(str.contains(",")){

			strsToCheck.addAll(Arrays.asList(str.split(",")));

		}else{
			strsToCheck.add(str);
		}
		long total = 0;
		for(String s : strsToCheck){

			String time = s.substring(s.length()-1);
			if(time.equals("n")){
				time = s.substring(s.length()-3);
			}
			int num = Integer.parseInt(s.replace(time,""));

			long subtotal = 0;
			if(time.equalsIgnoreCase("h")){
				subtotal = HOURS;
			}else
			if(time.equalsIgnoreCase("min")){
				subtotal = MIN;
			}else
			if(time.equalsIgnoreCase("d")){
				subtotal = DAYS;
			}else
			if(time.equalsIgnoreCase("w")){
				subtotal = WEEKS;
			}else
			if(time.equalsIgnoreCase("mo")) {
				subtotal = MONTHS;
			}

			if(subtotal == 0){
				total = total + num;
				continue;
			}
			total = total + subtotal * num;

		}
		Predicate<String> pre = s -> {
			List<String> matches = Arrays.asList("minutes","hours","seconds","days","weeks","months");

			for(int i = 0;i<matches.size();i++){
				String match = matches.get(i);
				if(StringUtils.contains(s,match) || StringUtils.contains(s,match.substring(0,match.length()-1))){
					return true;
				}
			}
			return false;
		};
		if(pre.test(string)){
			String[] ss = string.split(" ");
			String time = ss[0];
			int num = Integer.parseInt(ss[1]);

			long subtotal = 1;
			if(StringUtils.contains(time, "hour")){
				subtotal = HOURS;
			}

			if(StringUtils.contains(time, "minute")){
				subtotal = MIN;
			}
			if(StringUtils.contains(time, "day")){
				subtotal = DAYS;
			}
			if(StringUtils.contains(time, "week")){
				subtotal = WEEKS;
			}
			if(StringUtils.contains(time, "month")){
				subtotal = MONTHS;
			}

			total = total + subtotal * num;
		}
		return total;

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


		try{
			return  java.lang.Enum.valueOf(clazz, name);

		}catch (IllegalArgumentException ex){
			return null;
		}
	}
	public static String joinList(String del, Object[] list){

		StringBuilder b = new StringBuilder();
		for(Object ob : list){

			if(ob != list[0]) {
				b.append(del);
			}
			b.append(ob.toString());

		}
		return b.toString();
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
