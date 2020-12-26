package me.titan.customcommands.codeengines;

import com.google.common.collect.ArrayListMultimap;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;

import java.util.List;

public abstract class CodeTask {

	final String id;
	final CodeTaskType type;
	final String command;
	final boolean automatic;

	ArrayListMultimap<String, String> codes = ArrayListMultimap.create();

	ArrayListMultimap<String, String> codeCache = ArrayListMultimap.create();

	protected CodeTask(String id, CodeTaskType type, String command, boolean automatic) {
		this.id = id;
		this.type = type;
		this.command = command;
		this.automatic = automatic;
	}
	// True: Class.method/arg1,arg2>>(HowToRead):obj

	public List<String> getCache(String sec) {
		return codeCache.get(sec);
	}

	public void readCache(String cache) {

		String[] slashes = cache.split("/");
		String[] dots = slashes[0].split("\\.");

		String[] arrows = slashes[1].split(">>");

		String classs = dots[0];
		Class<?> clazz = null;
		try {
			clazz = Class.forName(classs);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String argsStr = arrows[0];
		String[] args = argsStr.trim().split(",");

		Object obj = null;
		String[] objs = arrows[1].split(":");
		String objStr = objs[1];
		String def = objs[0];

		if (def.equals("P")) {
			obj = Bukkit.getPlayer(objStr);
		} else if (def.equals("W")) {
			obj = Bukkit.getWorld(objStr);
		} else if (def.equals("LOC")) {
			obj = Util.getLocation(objStr);
		} else {
			obj = objStr;
		}
		CacheEngine.resolveMethod(clazz, obj, dots[1], args);
	}

	public abstract void execute();


	public enum CodeTaskType {

		IF, LOOP, TIMED, DELAYED, NORMA

	}

}
