package me.titan.customcommands.code.methods;

import io.lumine.utils.config.file.YamlConfiguration;
import lombok.Getter;
import me.titan.customcommands.code.CodeMethod;
import me.titan.customcommands.customcommands.ICustomCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.exception.FoException;

@Getter
public enum ConfigMethods {


	GET_STRING("getString", 1, (w, f, args) -> {

		return f.getString(args[0]);
	}, "getString(path)", Boolean.class);


	final String str;
	final int argsAmount;

	final String usage;
	final Class<?>[] argsClasses;

	CodeMethod<YamlConfiguration> function;

	ConfigMethods(String str, int argsAmount, String usage, Class<?>... argsClasses) {

		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.argsClasses = argsClasses;

	}

	ConfigMethods(String str, int argsAmount, CodeMethod<YamlConfiguration> f, String usage, Class<?>... argsClasses) {
		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.argsClasses = argsClasses;
		this.function = f;
	}


	public static ConfigMethods get(String m) {
		for (ConfigMethods pm : values()) {
			if (m.equalsIgnoreCase(pm.str)) return pm;
		}
		return null;
	}

	public Object invoke(Player p, ICustomCommand command, String code, YamlConfiguration f, String pr, String... args) {
		Methods.registerPremadeFunc(command, code, p, getFunction(), args, pr);

		if (args.length < argsAmount) {

			Common.throwError(new FoException("Invalid arguments amount for method: " + this.str + " usage: " + this.usage));
			return null;
		}

		if (function != null) {
			return function.apply(p, f, args);

		}

		return null;
	}

	public void doTeleport(Player p, String locs) {
		String[] parts = locs.split(", ");
		int x = Integer.parseInt(parts[0]);
		int y = Integer.parseInt(parts[1]);
		int z = Integer.parseInt(parts[2]);
		World w = Bukkit.getWorld(parts[3]);

		p.teleport(new Location(w, x, y, z));


	}


}
