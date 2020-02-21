package me.titan.customcommands.code;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.function.Function;

public enum Methods {

	GET_WORLD("getWorld", 1,(args) -> {
		return Bukkit.getWorld(args[0]);

	}, World.class);
	final String str;
	final int argsAmount;

	final Class<?> returnType;

	Function<String[],Object> function;

	Methods(String str, int argsAmount, Class<?> returnType) {

		this.str = str;
		this.argsAmount = argsAmount;
		this.returnType = returnType;

	}

	Methods(String str, int argsAmount, Function<String[], Object> f, Class<?> returnType) {
		this.str = str;
		this.argsAmount = argsAmount;
		this.returnType = returnType;
		this.function = f;
	}
	public static Object getInvoke(String m, String... args){
		return get(m).invoke(args);
	}

	public static Methods get(String m){
		for(Methods pm : values()){
			if(m.equalsIgnoreCase(pm.str)) return pm;
		}
		return null;
	}
	public static boolean isMethod(String m){
		return get(m) != null;
	}
	public Object invoke( String... args){



		if(args.length < argsAmount) return null;

		if(function != null){
			return function.apply(args);

		}
		return null;
	}
	public static Location getLocation(String locs) {
		locs = locs.replace(" ", "");
		String[] parts = locs.split(",");
		int x = Integer.parseInt(parts[0]);
		int y = Integer.parseInt(parts[1]);
		int z = Integer.parseInt(parts[2]);
		World w = Bukkit.getWorld(parts[3]);

		return new Location(w, x, y, z);
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

}
