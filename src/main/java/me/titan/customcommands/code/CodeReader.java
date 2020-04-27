//package me.titan.customcommands.code;
//
//import lombok.RequiredArgsConstructor;
//import me.titan.customcommands.code.methods.FileMethods;
//import me.titan.customcommands.code.methods.Methods;
//import me.titan.customcommands.code.methods.PlayerMethods;
//import me.titan.customcommands.utils.Util;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.entity.Player;
//import org.mineacademy.fo.Common;
//import org.mineacademy.fo.Valid;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.BiFunction;
//
//@RequiredArgsConstructor
//
//public class CodeReader {
//
//
//	final List<String> codes;
//	final Player p;
//	String[] commandArgs;
//
//	List<CodeVariable> vars = new ArrayList<>();
//	public static void performCode(List<String> codes,Player p, String[] args) {
//
//
//		CodeReader codeReader = new CodeReader(codes, p);
//		codeReader.commandArgs = args;
//
//		codeReader.setupVariables();
//		for (String s : codes) {
//			CodeVariable var = codeReader.readVariable(s);
//
//			if(var != null) {
//				codeReader.vars.add(var);
//				System.out.print(codeReader.vars);
//				continue;
//			}
//			codeReader.readCode(s);
//		}
//	}
//	public void setupVariables(){
//
//		for(int i = 0; i < commandArgs.length;i++){
//
//			String arg = commandArgs[i];
//
//			String varName = "arg" + i;
//
//			CodeVariable var = new CodeVariable(varName, CodeVariable.VariableType.TEXT);
//			var.setValue(arg);
//			vars.add(var);
//
//		}
//
//	}
//	public CodeVariable readVariable(String code){
//		// var player p = getPlayer("name")
//
//
//		if(code.startsWith("var")) {
//			String[] equals = code.split("=");
//
//			System.out.print(code);
//			String[] defParts = equals[0].split(" ");
//			CodeVariable codeVariable;
//			if (defParts.length == 3) {
//				String types = defParts[1];
//				String name = defParts[2];
//				CodeVariable.VariableType type = Util.getEnum(types, CodeVariable.VariableType.class);
//
//				System.out.print("type: " + type.toString());
//				codeVariable = new CodeVariable(name, type);
//
//			} else {
//				String name = defParts[1];
//				CodeVariable.VariableType type = CodeVariable.VariableType.VAR;
//				codeVariable = new CodeVariable(name, type);
//			}
//			System.out.print("read!!");
//			readVariableDefine(equals[1].replace(" ", ""), codeVariable);
//		return codeVariable;
//
//		}else {
//			return null;
//		}
//	}
//
//	public void readVariableDefine(String equals2, CodeVariable var) {
//		String[] Dots = equals2.split("\\.");
//		String object = "";
//
//		String dots0 = "";
//		if (Dots.length > 1) {
//			dots0 = Dots[0];
//		}else {
//		dots0 = equals2;
//		}
//
//
//		System.out.print("0:" + dots0);
//			if (dots0.contains("(")) {
//				String method = dots0;
//				String argsStr= "";
//				int bow1 = method.indexOf("(");
//				if(dots0.contains(")")) {
//					int bow2 = method.indexOf(")");
//
//					argsStr = method.substring(bow1, bow2);
//
//				}else if(Dots.length > 1 && Dots[1].contains(")")){
//					dots0 = Dots[0] + "." + Dots[1];
//					method = dots0;
//					bow1 = method.indexOf("(");
//					int bow2 = method.indexOf(")");
//
//					argsStr = method.substring(bow1, bow2);
//				}
//				method = method.replace(argsStr, "").replace(")", "");
//				System.out.print(method + " GGG");
//				Methods methods = Methods.get(method);
//				System.out.print("methods: " + methods);
//				if (methods != null) {
//
//					System.out.print("methods not null");
//					if (var.getType().getClazz().equals(methods.getReturnType())) {
//						var.setValue(methods.invoke(getArgs(dots0)));
//						System.out.print("dots" + dots0);
//					}
//
//				}
//
//
//
//
//			}else if(!vars.isEmpty()){
//					for(CodeVariable vari : vars){
//						if(dots0.equalsIgnoreCase(vari.getName()) && vari.getType() != CodeVariable.VariableType.FILE) {
//							equals2 = equals2.replace(dots0,vari.getValueStringAdvanced());
//							readVariableDefine(equals2,var);
//							return;
//						}
//					}
//				}
//
//			// var Config c = f.loadConfig()
//			if(Dots.length >= 2){
//
//			}
//
//
//			System.out.print(var.getName() + " " + var.getValue());
//	}
//	public void nextLevelVariableReader(CodeVariable var, String[] Dots){
//		for(int i = 1; i<Dots.length;i++) {
//			Object fd = getFirstDot(Dots[i-1]);
//			if (fd != null) {
//				if (fd instanceof File) {
//
//					FileMethods fm = FileMethods.get();
//
//					System.out.print("fd is file dotsi: " + Dots[i]);
//					if(fm != null){
//						if(var.getType().getClazz().equals(fm.getReturnType())){
//							var.setValue(fm.invoke(p,((File) fd), getArgs(dots0)));
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public Object getFirstDot(String dot){
//		if(Bukkit.getPlayer(dot) != null){
//			return Bukkit.getPlayer(dot);
//		}else if(Bukkit.getWorld(dot) != null){
//			return Bukkit.getWorld(dot);
//		}
//		for(CodeVariable var : vars){
//			if(dot.equalsIgnoreCase(var.getName())){
//
//				System.out.print(var.getName());
//
//				return var.getValue();
//
//			}
//		}
//		return null;
//	}
//
//	public String readCode(String code) {
//		// var File f = getFile("gg.yml");
//
//		int repeats = 1;
//		if (code.contains(":")) {
//			String[] blocks = code.split(":");
//			//do(10 times):
//			if (code.startsWith("do")) {
//				String doBlock = blocks[0];
//				int bow1 = doBlock.indexOf("(");
//				int bow2 = doBlock.indexOf(")");
//
//				String times = doBlock.substring(bow1, bow2);
//
//
//				times = times.replace("times", "").replace("(","");
//				times = times.replace(" ", "");
//				int timesNum = Integer.parseInt(times);
//				repeats = timesNum;
//				code = code.replace(blocks[0],"").replace(":","").replace(" ","");
//
//			}
//		}
//		String result = "";
//
//			String[] Dots = code.split("\\.");
//			String object = "";
//			if (Dots.length > 1) {
//				if (Dots[0].equalsIgnoreCase("player")) {
//					object = "player";
//					for (int i = 0; i < repeats; i++) {
//
//						result = readPlayerCode(code, getMethod(Dots));
//					}
//
//				}else {
//					if (Dots[0].contains("(")) {
//						String method = Dots[0];
//						int bow1 = method.indexOf("(");
//						int bow2 = method.indexOf(")");
//						String argsStr = method.substring(bow1, bow2);
//
//						method = method.replace(argsStr, "").replace(")", "");
//						Methods methods = Methods.get(method);
//						if (methods != null) {
//
//
//							if (methods.getReturnType()== World.class) {
//
//								for (int i = 0; i < repeats; i++) {
//
//									readWorldCode(code, Dots[1], (World) methods.invoke(getArgs(Dots[0])));
//								}
//							}else if(methods.getReturnType() == Object.class){
//								methods.invoke(getArgs(Dots[0]));
//							}
//
//
//						} else{
//							World w = Bukkit.getWorld(Dots[0]);
//							if (w != null) {
//								for (int i = 0; i < repeats; i++) {
//
//									String finalCode = code;
//									Common.runLater(5, () -> {
//										readWorldCode(finalCode, getMethod(Dots), w);
//									});
//
//								}
//							}
//						}
//
//					}else if(isPapularObjects(Dots[0])){
//
//						return readPapularObjects(Dots[0], repeats,Dots);
//
//					}else if(!vars.isEmpty()){
//
//						for(CodeVariable var : vars){
//							if(Dots[0].equalsIgnoreCase(var.getName())){
//
//								System.out.print(var.getName());
//								code = code.replace(Dots[0], var.getValueStringAdvanced());
//								System.out.print(code);
//								return readCode(code);
//
//							}
//						}
//
//					}
//					}
//
//
//
//
//		}
//		return result;
//
//	}
//	public boolean isPapularObjects(String code){
//
//		return Bukkit.getWorld(code) != null || Bukkit.getPlayer(code) != null;
//
//	}
//	public String readPapularObjects(String code, int repeats, String[] Dots){
//
//		if(Bukkit.getWorld(code) != null){
//			World w = Bukkit.getWorld(code);
//
//			if (w != null) {
//				for (int i = 0; i < repeats; i++) {
//
//					String finalCode = code;
//					Common.runLater(5, () -> {
//						readWorldCode(finalCode, getMethod(Dots), w);
//					});
//				}
//			}
//
//		}else if(Bukkit.getPlayer(code) != null){
//			Player p = Bukkit.getPlayer(code);
//
//			if (p != null) {
//				for (int i = 0; i < repeats; i++) {
//
//					String finalCode = code;
//					Common.runLater(5, () -> {
//						readPlayerCode(finalCode, getMethod(Dots));
//					});
//				}
//			}
//		}
//		return null;
//	}
//	public String readWorldCode(String code, String method, World w){
//		int bow1 = method.indexOf("(");
//		int bow2 = method.indexOf(")");
//		String argsStr = method.substring(bow1, bow2);
//		String oldStr = argsStr;
//
//		argsStr = setupArgsString(argsStr);
//
//		//argsStr = argsStr.substring(1,argsStr.length() - 1);
//		argsStr = argsStr.replaceFirst("\\(", "").replaceFirst("\\)", "");
//
//
//		String argsStrReplaced = method.replace(oldStr, "").replace("(", "").replace(")", "");
//
//
//		if (argsStrReplaced.equalsIgnoreCase("")) {
//			argsStrReplaced = argsStr;
//		}
//		argsStr = argsStr.replace(" ", "");
//		String[] args = argsStr.replace(" ","").split(",");
//
//		Object b = WorldMethods.get(argsStrReplaced).invoke(p, w, args);
//		return b + "";
//	}
//	public String readPlayerCode(String code, String method) {
//
//
//		int bow1 = method.indexOf("(");
//		int bow2 = method.indexOf(")");
//		String argsStr = method.substring(bow1, bow2);
//		String oldStr = argsStr;
//
//		argsStr = setupArgsString(argsStr);
//
//		//argsStr = argsStr.substring(1,argsStr.length() - 1);
//		argsStr = argsStr.replaceFirst("\\(", "").replaceFirst("\\)", "");
//
//
//		String argsStrReplaced = method.replace(oldStr, "").replace("(", "").replace(")", "");
//
//
//		if (argsStrReplaced.equalsIgnoreCase("")) {
//			argsStrReplaced = argsStr;
//		}
//		String[] args = argsStr.replace(" ","").split(",");
//		Object b = PlayerMethods.get(argsStrReplaced).invoke(p, args);
//		return b + "";
//
//	}
//
//	public String[] getArgs(String code){
//		int arrow1 = code.indexOf("(");
//		int arrow2 = code.indexOf(")");
//
//		String argsStr = code.substring(arrow1,arrow2).replace(")","").replace("(","");
//
//		argsStr = setupArgsString(argsStr);
//		String[] args = argsStr.replace(" ","").split(",");
//
//		return args;
//	}
//	public String readArg(String code) {
//		String[] Dots = code.split("\\.");
//		String object = "";
//		if (Dots.length > 1) {
//			if (Dots[0].equalsIgnoreCase("player")) {
//				object = "player";
//
//			}
//
//
//		}
//		String method = getMethod(Dots);
//
//		method = "getHealth";
//		return PlayerMethods.getInvoke(method, p) + "";
//		//player.getHealth
//
//	}
//
//	public String getMethod(String[] Dots) {
//		if (Dots.length >= 2) {
//
//
//			String method = Dots[1];
//			if (Dots.length >= 3) {
//				method = "";
//				for (int i = 1; i < Dots.length; i++) {
//					String dot = i == 1 ? "" : ".";
//					method = method + dot + Dots[i];
//				}
//
//			}
//			return method;
//		}
//		return "";
//	}
//
//
//	public String setupArgsString(String argsStr) {
//
//
//		String[] args = argsStr.contains(",")
//				? argsStr.split(",")
//				: argsStr.contains(" , ")
//				? argsStr.split(" , ")
//				: argsStr.split(", ");
//
//		for (int i = 0; i < args.length; i++) {
//			// player.setHealth(player.getHealth * 10 * 1.2)
//
//			String arg = args[i];
//			arg = arg.replace("(", "").replace(")", "");
//
//			if(arg.equalsIgnoreCase("player.getLocation")){
//				Location loc = p.getLocation();
//				String narg = loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getWorld().getName();
//				argsStr = argsStr.replace(arg, narg);
//
//			}
//			if(!vars.isEmpty()) {
//				for (CodeVariable var : vars) {
//					if (arg.equalsIgnoreCase(var.getName())) {
//
//
//						String narg = var.getValueStringAdvanced();
//
//						argsStr = argsStr.replace(arg, narg);
//					}
//				}
//			}
//
//			if (arg.contains("*")) {
//
//				String[] stars = arg.split("\\*");
//
//				Double result = Double.parseDouble(stars[0]);
//				for (int o = 1; o < stars.length; o++) {
//					String star = stars[o];
//
//					String oldStar = star;
//					if (star.contains(" ")) {
//						star = star.replace(" ", "");
//					}
//
//					if (star.contains(".") && !Valid.isDecimal(star)) {
//						double dx = Double.parseDouble(readArg(star));
//						result *= dx;
//					} else {
//						result *= Double.parseDouble(star);
//
//					}
//
//				}
//				argsStr = argsStr.replace(arg, result + "");
//				argsStr = argsStr.replace(arg, result + "");
//
//
//			} else if (arg.contains("-")) {
//
//				String[] stars = arg.split("-");
//
//				Double result = Double.parseDouble(stars[0]);
//				for (int o = 1; o < stars.length; o++) {
//					String star = stars[o];
//
//					String oldStar = star;
//					if (star.contains(" ")) {
//						star = star.replace(" ", "");
//					}
//
//					if (star.contains(".") && !Valid.isDecimal(star)) {
//						double dx = Double.parseDouble(readArg(star));
//						result -= dx;
//					} else {
//						result -= Double.parseDouble(star);
//
//					}
//
//				}
//				argsStr = argsStr.replace(arg, result + "");
//
//
//			} else if (arg.contains("+")) {
//
//				String[] stars = arg.split("\\+");
//
//				Double result = Double.parseDouble(stars[0]);
//				for (int o = 1; o < stars.length; o++) {
//					String star = stars[o];
//
//					String oldStar = star;
//					if (star.contains(" ")) {
//						star = star.replace(" ", "");
//					}
//
//					if (star.contains(".") && !Valid.isDecimal(star)) {
//						double dx = Double.parseDouble(readArg(star));
//						result += dx;
//					} else {
//						result += Double.parseDouble(star);
//
//					}
//
//				}
//				argsStr = argsStr.replace(arg, result + "");
//
//			} else if (arg.contains("/")) {
//
//				String[] stars = arg.split("/");
//
//				Double result = Double.parseDouble(stars[0]);
//				for (int o = 1; o < stars.length; o++) {
//					String star = stars[o];
//
//					String oldStar = star;
//					if (star.contains(" ")) {
//						star = star.replace(" ", "");
//					}
//
//					if (star.contains(".") && !Valid.isDecimal(star)) {
//						double dx = Double.parseDouble(readArg(star));
//						result /= dx;
//					} else {
//						result /= Double.parseDouble(star);
//
//					}
//
//				}
//				argsStr = argsStr.replace(arg, result + "");
//
//			} else if (arg.contains(".") && !Valid.isDecimal(arg)) {
//				double dx = Double.parseDouble(readArg(arg));
//
//				argsStr = argsStr.replace(arg, dx + "");
//
//			} else {
//				if (arg.equalsIgnoreCase("player") && p != null) {
//					argsStr = argsStr.replace(arg, p.getName());
//				}
//			}
//
//		}
//
//
//		return argsStr;
//	}
//
//	public  Double performOperation(String regex, String arg, BiFunction<Double, Double, Double> f) {
//		if (arg.contains(regex)) {
//
//			String[] stars = arg.split(regex);
//
//			Double result = 1d;
//			for (String star : stars) {
//				String oldStar = star;
//				if (star.contains(" ")) {
//					star = star.replace(" ", "");
//				}
//				if (star.contains(".") && !Valid.isDecimal(star)) {
//					double dx = Double.parseDouble(readArg(star));
//					result = f.apply(result, dx);
//				} else {
//					result = Double.parseDouble(star);
//
//				}
//
//			}
//			return result;
//
//
//		}
//		return 0d;
//	}
//
//
//
//
//	public static Location getLocation(String locs) {
//		locs = locs.replace(" ", "");
//		String[] parts = locs.split(",");
//		int x = Integer.parseInt(parts[0]);
//		int y = Integer.parseInt(parts[1]);
//		int z = Integer.parseInt(parts[2]);
//		World w = Bukkit.getWorld(parts[3]);
//
//		return new Location(w, x, y, z);
//	}
//}
