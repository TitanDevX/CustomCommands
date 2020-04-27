package me.titan.customcommands.code.methods;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.titan.customcommands.code.CodeMethod;
import me.titan.customcommands.code.Nothing;
import me.titan.customcommands.code.cache.PremadeFunction;
import me.titan.customcommands.customcommands.ICustomCommand;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum Methods implements MethodsEnum<Nothing> {

	GET_WORLD("getWorld", "", 1, (args) -> {
		return Bukkit.getWorld(args[0]);

	}, World.class),
	GET_PLAYER("getPlayer", "", 1, (args) -> {
		return Bukkit.getPlayer(args[0]);
	}, Player.class),
	GET_FILE("getFile", "", 1, (args) -> {
		return FileUtil.getFile(args[0].replace("__", "."));
	}, File.class),
	LOG("Log", "", 1, (args) -> {
		Common.log(args);
		return "";
	}, Object.class);

	final String name;
	final String usage;
	final int minimumArgs;
	final Function<String[], Object> function;
	final Class<?> returnType;


	public static Object getInvoke(String m, String... args){
		return get(m).invoke(args);
	}

	public static Methods get(String m){
		for(Methods pm : values()){
			if (m.equalsIgnoreCase(pm.name)) return pm;
		}
		return null;
	}

	public static boolean isMethod(String m){
		return get(m) != null;
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

	public static void registerPremadeFunc(ICustomCommand command, String code, Object object, CodeMethod method, String[] args, String pr) {
		command.getCodeMethods().put(code, new PremadeFunction(args, object, method, pr));
	}

	public static String join(String[] args){
		return join(args,args.length);
	}

	public static String join(String[] args, int amount){
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < amount; i++){

			String arg = args[i];
			if(!result.toString().isEmpty()){
				result.append(", ");
			}
			result.append(arg);
		}
		return result.toString();
	}

	public Object invoke(String... args) {


		if (args.length < minimumArgs) return null;

		if (function != null) {
			List<String> gg = new ArrayList<>();
			for (String arg : args) {
				arg = arg.replace("'", "");
				gg.add(arg);
			}
			String[] oof = new String[0];
			args = gg.toArray(oof);
			return function.apply(args);

		}
		return null;
	}

	@Override
	public void invoke(Player p, Nothing ob, String... args) {

	}
}
