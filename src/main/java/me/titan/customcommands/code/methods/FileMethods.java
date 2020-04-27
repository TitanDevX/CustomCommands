package me.titan.customcommands.code.methods;

import lombok.Getter;
import me.titan.customcommands.code.CodeMethod;
import me.titan.customcommands.code.Nothing;
import me.titan.customcommands.customcommands.ICustomCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.exception.FoException;

import java.io.File;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

@Getter
public enum FileMethods {


	WRITE("write", 1, (w, f, args) -> {
		FileUtil.write(f, Arrays.asList(args), StandardOpenOption.APPEND);
		return null;
	}, "write(lines..)", Nothing.class),
	LOAD_CONFIGURATION("loadConfig", 0, (w, f, args) -> {

		System.out.print("loadConfig: " + f.getPath());
		return FileUtil.loadConfigurationStrict(f);
	}, "loadConfig()", YamlConfiguration.class);


	final String str;
	final int argsAmount;

	final String usage;
	Class<?>[] argsClasses;
	Class<?> returnType;

	CodeMethod<File> function;

	FileMethods(String str, int argsAmount, String usage, Class<?>... argsClasses) {

		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.argsClasses = argsClasses;

	}

	FileMethods(String str, int argsAmount, CodeMethod<File> f, String usage, Class returnClass) {
		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.returnType = returnClass;
		this.function = f;
	}


	public static FileMethods get(String m) {
		for (FileMethods pm : values()) {
			if (m.equalsIgnoreCase(pm.str)) return pm;
		}
		return null;
	}

	public Object invoke(Player p, ICustomCommand command, String code, File f, String pr, String... args) {
		Methods.registerPremadeFunc(command, code, f, getFunction(), args, pr);


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
