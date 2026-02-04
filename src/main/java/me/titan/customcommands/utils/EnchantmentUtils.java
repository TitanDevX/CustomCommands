package me.titan.customcommands.utils;

import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

public class EnchantmentUtils {
	private static final HashMap<String, Enchantment> enchants = new HashMap();

	static {
		try {
			enchants.put("SHARPNESS", Enchantment.DAMAGE_ALL);

		} catch (NoSuchFieldError ignored) {

		}
		try {
			enchants.put("POWER", Enchantment.ARROW_DAMAGE);


		} catch (NoSuchFieldError ignored) {


		}
		try {
			enchants.put("FIRE_PROTECTION", Enchantment.PROTECTION_FIRE);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("FEATHER_FALLING", Enchantment.PROTECTION_FALL);

		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("PROTECTION", Enchantment.PROTECTION_ENVIRONMENTAL);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("BLAST_PROTECTION", Enchantment.PROTECTION_EXPLOSIONS);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("PROJECTILE_PROTECTION", Enchantment.PROTECTION_PROJECTILE);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("RESPIRATION", Enchantment.OXYGEN);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("INFINITY", Enchantment.ARROW_INFINITE);
		} catch (NoSuchFieldError ignored) {
		}

		try {
			enchants.put("AQUA_AFFINITY", Enchantment.WATER_WORKER);
		} catch (NoSuchFieldError ignored) {
		}

		try {
			enchants.put("UNBREAKING", Enchantment.DURABILITY);
		} catch (NoSuchFieldError ignored) {
		}

		try {
			enchants.put("SMITE", Enchantment.DAMAGE_UNDEAD);
		} catch (NoSuchFieldError ignored) {
		}

		try {
			enchants.put("EFFICIENCY", Enchantment.DIG_SPEED);
		} catch (NoSuchFieldError ignored) {
		}

		try {
			enchants.put("FIRE_ASPECT", Enchantment.FIRE_ASPECT);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("SILK_TOUCH", Enchantment.SILK_TOUCH);
		} catch (NoSuchFieldError ignored) {
		}

		try {
			enchants.put("FORTUNE", Enchantment.LOOT_BONUS_BLOCKS);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("LOOTING", Enchantment.LOOT_BONUS_MOBS);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("PUNCH", Enchantment.ARROW_KNOCKBACK);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("FLAME", Enchantment.ARROW_FIRE);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("KNOCKBACK", Enchantment.KNOCKBACK);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("THORNS", Enchantment.THORNS);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("MENDING", Enchantment.MENDING);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("DEPTH_STRIDER", Enchantment.DEPTH_STRIDER);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("FROST_WALKER", Enchantment.FROST_WALKER);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("CHANNELING", Enchantment.CHANNELING);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("RIPTIDE", Enchantment.RIPTIDE);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("BINDING_CURSE", Enchantment.BINDING_CURSE);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("IMPALING", Enchantment.IMPALING);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("LOYALTY", Enchantment.LOYALTY);
		} catch (NoSuchFieldError ignored) {
		}
		try {
			enchants.put("LUCK", Enchantment.LUCK);
		} catch (NoSuchFieldError ignored) {
		}
	}

	public EnchantmentUtils() {
	}

	public static Enchantment getByName(String enchantmentName) {
		return enchants.containsKey(enchantmentName) ? enchants.get(enchantmentName.toUpperCase()) : Enchantment.getByName(enchantmentName);
	}
}
