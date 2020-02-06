package me.titan.customcommands.code;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;

import java.util.List;
import java.util.function.BiFunction;

public class CodeReader {

	public static void performCode(List<String> codes, Player p) {

		for (String s : codes) {
			readCode(s, p);
		}
	}

	public static String readCode(String code, Player p) {
		int repeats = 1;
		if (code.contains(":")) {
			String[] blocks = code.split(":");
			//do(10 times):
			if (code.startsWith("do")) {
				String doBlock = blocks[0];
				int bow1 = doBlock.indexOf("(");
				int bow2 = doBlock.indexOf(")");

				String times = doBlock.substring(bow1, bow2);


				times = times.replace("times", "").replace("(","");
				times = times.replace(" ", "");
				int timesNum = Integer.parseInt(times);
				repeats = timesNum;
				code = code.replace(blocks[0],"").replace(":","").replace(" ","");

			}
		}
		String result = "";
		System.out.print(repeats);

			String[] Dots = code.split("\\.");
			String object = "";
			if (Dots.length > 1) {
				if (Dots[0].equalsIgnoreCase("player")) {
					object = "player";
					for (int i = 0; i < repeats; i++) {

						result = readPlayerCode(code, getMethod(Dots), p);
					}

				}else {
					if (Dots[0].contains("(")) {
						String method = Dots[0];
						int bow1 = method.indexOf("(");
						int bow2 = method.indexOf(")");
						String argsStr = method.substring(bow1, bow2);

						method = method.replace(argsStr, "").replace(")", "");
						Methods methods = Methods.get(method);
						if (methods != null) {


							if (methods.returnType == World.class) {

								for (int i = 0; i < repeats; i++) {

									readWorldCode(code, Dots[1], (World) methods.invoke(getArgs(Dots[0])), p);
								}
							}

						} else {
							World w = Bukkit.getWorld(Dots[0]);
							if (w != null) {
								for (int i = 0; i < repeats; i++) {

									String finalCode = code;
									Common.runLater(5, () -> {
										readWorldCode(finalCode, getMethod(Dots), w, p);
									});

								}
							}
						}
					}else{


						World w = Bukkit.getWorld(Dots[0]);

						if (w != null) {
							for (int i = 0; i < repeats; i++) {

								String finalCode = code;
								Common.runLater(5, () -> {
									readWorldCode(finalCode, getMethod(Dots), w, p);
								});
							}
						}
					}
					}




		}
		return result;

	}
	public static String readWorldCode(String code, String method, World w, Player p){
		int bow1 = method.indexOf("(");
		int bow2 = method.indexOf(")");
		String argsStr = method.substring(bow1, bow2);
		String oldStr = argsStr;

		argsStr = setupArgsString(argsStr, p);

		//argsStr = argsStr.substring(1,argsStr.length() - 1);
		argsStr = argsStr.replaceFirst("\\(", "").replaceFirst("\\)", "");


		String argsStrReplaced = method.replace(oldStr, "").replace("(", "").replace(")", "");


		if (argsStrReplaced.equalsIgnoreCase("")) {
			argsStrReplaced = argsStr;
		}
		argsStr = argsStr.replace(" ", "");
		String[] args = argsStr.replace(" ","").split(",");

		Object b = Methods.WorldMethods.get(argsStrReplaced).invoke(p,w, args);
		return b + "";
	}
	public static String readPlayerCode(String code, String method, Player p) {


		int bow1 = method.indexOf("(");
		int bow2 = method.indexOf(")");
		String argsStr = method.substring(bow1, bow2);
		String oldStr = argsStr;

		argsStr = setupArgsString(argsStr, p);

		//argsStr = argsStr.substring(1,argsStr.length() - 1);
		argsStr = argsStr.replaceFirst("\\(", "").replaceFirst("\\)", "");


		String argsStrReplaced = method.replace(oldStr, "").replace("(", "").replace(")", "");


		if (argsStrReplaced.equalsIgnoreCase("")) {
			argsStrReplaced = argsStr;
		}
		String[] args = argsStr.replace(" ","").split(",");
		Object b = Methods.PlayerMethods.get(argsStrReplaced).invoke(p, args);
		return b + "";

	}

	public static String[] getArgs(String code){
		int arrow1 = code.indexOf("(");
		int arrow2 = code.indexOf(")");

		String argsStr = code.substring(arrow1,arrow2).replace(")","").replace("(","");

		argsStr = setupArgsString(argsStr, null);
		String[] args = argsStr.replace(" ","").split(",");

		return args;
	}
	public static String readArg(String code, Player p) {
		String[] Dots = code.split("\\.");
		String object = "";
		if (Dots.length > 1) {
			if (Dots[0].equalsIgnoreCase("player")) {
				object = "player";

			}


		}
		String method = getMethod(Dots);

		method = "getHealth";
		return Methods.PlayerMethods.getInvoke(method, p) + "";
		//player.getHealth

	}

	public static String getMethod(String[] Dots) {
		if (Dots.length >= 2) {


			String method = Dots[1];
			if (Dots.length >= 3) {
				method = "";
				for (int i = 1; i < Dots.length; i++) {
					String dot = i == 1 ? "" : ".";
					method = method + dot + Dots[i];
				}

			}
			return method;
		}
		return "";
	}


	public static String setupArgsString(String argsStr, Player p) {


		String[] args = argsStr.contains(",")
				? argsStr.split(",")
				: argsStr.contains(" , ")
				? argsStr.split(" , ")
				: argsStr.split(", ");

		for (int i = 0; i < args.length; i++) {
			// player.setHealth(player.getHealth * 10 * 1.2)

			String arg = args[i];
			arg = arg.replace("(", "").replace(")", "");

			if(arg.equalsIgnoreCase("player.getLocation")){
				Location loc = p.getLocation();
				String narg = loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getWorld().getName();
				argsStr = argsStr.replace(arg, narg);

			}

			if (arg.contains("*")) {

				String[] stars = arg.split("\\*");

				Double result = Double.parseDouble(stars[0]);
				for (int o = 1; o < stars.length; o++) {
					String star = stars[o];

					String oldStar = star;
					if (star.contains(" ")) {
						star = star.replace(" ", "");
					}

					if (star.contains(".") && !Valid.isDecimal(star)) {
						double dx = Double.parseDouble(readArg(star, p));
						result *= dx;
					} else {
						result *= Double.parseDouble(star);

					}

				}
				argsStr = argsStr.replace(arg, result + "");
				argsStr = argsStr.replace(arg, result + "");


			} else if (arg.contains("-")) {

				String[] stars = arg.split("-");

				Double result = Double.parseDouble(stars[0]);
				for (int o = 1; o < stars.length; o++) {
					String star = stars[o];

					String oldStar = star;
					if (star.contains(" ")) {
						star = star.replace(" ", "");
					}

					if (star.contains(".") && !Valid.isDecimal(star)) {
						double dx = Double.parseDouble(readArg(star, p));
						result -= dx;
					} else {
						result -= Double.parseDouble(star);

					}

				}
				argsStr = argsStr.replace(arg, result + "");


			} else if (arg.contains("+")) {

				String[] stars = arg.split("\\+");

				Double result = Double.parseDouble(stars[0]);
				for (int o = 1; o < stars.length; o++) {
					String star = stars[o];

					String oldStar = star;
					if (star.contains(" ")) {
						star = star.replace(" ", "");
					}

					if (star.contains(".") && !Valid.isDecimal(star)) {
						double dx = Double.parseDouble(readArg(star, p));
						result += dx;
					} else {
						result += Double.parseDouble(star);

					}

				}
				argsStr = argsStr.replace(arg, result + "");

			} else if (arg.contains("/")) {

				String[] stars = arg.split("/");

				Double result = Double.parseDouble(stars[0]);
				for (int o = 1; o < stars.length; o++) {
					String star = stars[o];

					String oldStar = star;
					if (star.contains(" ")) {
						star = star.replace(" ", "");
					}

					if (star.contains(".") && !Valid.isDecimal(star)) {
						double dx = Double.parseDouble(readArg(star, p));
						result /= dx;
					} else {
						result /= Double.parseDouble(star);

					}

				}
				argsStr = argsStr.replace(arg, result + "");

			} else if (arg.contains(".") && !Valid.isDecimal(arg)) {
				double dx = Double.parseDouble(readArg(arg, p));

				argsStr = argsStr.replace(arg, dx + "");

			} else {
				if (arg.equalsIgnoreCase("player") && p != null) {
					argsStr = argsStr.replace(arg, p.getName());
				}
			}

		}


		return argsStr;
	}

	public static Double performOperation(String regex, String arg, BiFunction<Double, Double, Double> f, Player p) {
		if (arg.contains(regex)) {

			String[] stars = arg.split(regex);

			Double result = 1d;
			for (String star : stars) {
				String oldStar = star;
				if (star.contains(" ")) {
					star = star.replace(" ", "");
				}
				if (star.contains(".") && !Valid.isDecimal(star)) {
					double dx = Double.parseDouble(readArg(star, p));
					result = f.apply(result, dx);
				} else {
					result = Double.parseDouble(star);

				}

			}
			return result;


		}
		return 0d;
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
}
