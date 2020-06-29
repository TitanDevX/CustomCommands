package me.titan.customcommands.newCode;

import lombok.Getter;
import lombok.Setter;
import me.titan.customcommands.DataShortcut;
import me.titan.customcommands.code.CodeVariable;
import me.titan.customcommands.code.VariablesGroup;
import me.titan.customcommands.code.methods.FileMethods;
import me.titan.customcommands.code.methods.Methods;
import me.titan.customcommands.code.methods.PlayerMethods;
import me.titan.customcommands.code.methods.WorldMethods;
import me.titan.customcommands.customcommands.ICustomCommand;
import me.titan.customcommands.logger.Logger;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class NewCodeReader {

	final Player p;
	String[] args;
	String cmdName;
	ICustomCommand command;

	Map<String, String> predefinedVars = new HashMap<>();
	List<CodeVariable> variables = new ArrayList<>();
	String currentCode;

	/*


	var Player p
	 vars*p => methods.getPlayer(TitanDev);
	var World w = vars*p.getWorld
	var Location loc = (vars*w, 1, 2, 1)
	vars*p.teleport(vars*loc)

	var File f = methods.getFile(files/file.yml)
	vars*f.write(gg)
	methods.getFile(files/file2.yml).write(gg2)


	player.setHealth(!player@getHealth() * 2!)
	event::PlayerDeath  {

	var Player p = vars*event*player
	var Player k = vars*event*killer



	}


	 */
	List<VariablesGroup> variableGroups = new ArrayList<>();

	public NewCodeReader(ICustomCommand command, String cmdName, Player p, String[] args) {
		this.p = p;
		this.command = command;
		this.cmdName = cmdName;
//player.setHealth(methods_getPlayer("_88i")_sendMessage("gg")"gg"
		this.args = args;
		predefinedVars.put("player", "methods.getPlayer('" + p.getName() + "')");

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			predefinedVars.put("arg" + i, arg.replace(".", "__"));
		}


	}

	public String replacePredefinedVars(String code) {

//		if(code.contains("player_")){
//			code.replace("player_", predefinedVars.get("player_"));
//		}
		for (Map.Entry<String, String> en : predefinedVars.entrySet()) {


			if (code.contains(en.getKey())) {

				code = code.replace(en.getKey(), en.getValue());
			}


		}
		if (code.contains(".")) {
			String[] dots = getDots(code, false);

			if (code.startsWith("var") && !code.startsWith("vars*") && code.contains("=")) {

				String s = code.replace(" ", "").split("=")[1];
				dots = getDots(s, false);

			}

			for (String dot : dots) {
				if (Bukkit.getWorld(dot) != null) {
					World w = Bukkit.getWorld(dot);

					code = code.replace(dot, "methods.getWorld(" + w.getName() + ")");
				}
			}
		}
		return code;
	}

	public String[] getDots(String str, boolean args) {
		List<String> dots = new ArrayList<String>();

		String[] ar = str.split("\\.");
		for (int i = 0; i < ar.length; i++) {
			String dot = ar[i];

//			if(Util.isBetween(str,dot,"'")){
//				//dots.add(dot);
//			}
//			if(!args){
//				// methods.getPlayer('.gg').setHealth =
//				if(!Util.isBetweenBrackets(str,dot,"(",")")){
//
//				}
//			}
			dots.add(dot);
		}

		String[] gg = new String[0];
		return dots.toArray(gg);
	}

	public void readCode(String code) {

		currentCode = code;
		Logger.log("CodeReader, readCode:130", "Reading code " + code + "...");
		if (code.contains("//")) {
			int in = code.indexOf("/");
			code = code.replace(code.substring(in), "");

			Logger.log("CodeReader, readCode:135", "clearing comments from " + code + ", which are: " + code.substring(in));

////			int gg = code.lastIndexOf(")") + 1;
////
////			code = code.replace(code.substring(gg), "");

		}

		Logger.log("CodeReader, readCode:145", "Replacing replacePredefined Vars in code " + code);

		code = replacePredefinedVars(code);


		if (code.startsWith("var") && !code.startsWith("vars*")) {
			Logger.log("CodeReader, readCode:151", "Reading variable in code " + code);

			readVariable(code);
		} else if (code.startsWith("vars*") && code.contains("=>")) {

			Logger.log("CodeReader, readCode:156", "Setting variable in code " + code);

			setVariable(code);
		} else if (code.startsWith("vars*")) {


			String[] dots = getDots(code, false);
			code = code.replace(dots[0], replaceWithVarDef(dots[0]));

			readCode(code);

		} else {
			Logger.log("CodeReader, readCode:168", "Reading normal code in code " + code);

			readDef(code, "normalcod");
		}

	}

	public String replaceWithVarDef(String code) {
		String[] stars = code.split("\\*");
		VariablesGroup current = null;
		for (int i = 1; i < stars.length; i++) {
			String star = stars[i];
			CodeVariable cv = null;
			if (i == stars.length - 1) {

				if (current != null) {

					DataShortcut ds = current.getVariable(star);

					if (ds == null) {

						logError("Unable to find a variable with name: " + star + " in vars group :" + current.getName() + " . please create one with this format: 'var TYPE = VALUE'.", code);
						return code;
					}

				} else {
					// vars*event*player
					String name = star;
					cv = getVariable(name);
					if (cv == null) {

						logError("Unable to find a variable with name: " + name + ". please create one with this format: 'var TYPE = VALUE'.", code);
						return code;
					}
				}
				return code.replace("vars*", "").replace(star, cv.getValueString());
			}

			VariablesGroup vg = getGroup(star);
			if (vg == null) {
				logError("Unable to find variables group with name: " + star + ". in order to have a variables group defened the code must be inside a block such as event block (event::EVENT_TYPE => { <code> })", code);

				return code;
			}
			current = vg;


		}
		return code;
	}

	public Object getVarDef(String code) {
		String[] stars = code.split("\\*");
		VariablesGroup current = null;
		for (int i = 1; i < stars.length; i++) {
			String star = stars[i];
			CodeVariable cv = null;
			if (i == stars.length - 1) {

				if (current != null) {

					DataShortcut ds = current.getVariable(star);

					if (ds == null) {

						logError("Unable to find a variable with name: " + star + " in vars group :" + current.getName() + " . please create one with this format: 'var TYPE = VALUE'.", code);
						return code;
					}

				} else {
					// vars*event*player
					String name = star;
					cv = getVariable(name);
					if (cv == null) {

						logError("Unable to find a variable with name: " + name + ". please create one with this format: 'var TYPE = VALUE'.", code);
						return code;
					}
				}
				return cv.getValue();
			}

			VariablesGroup vg = getGroup(star);
			if (vg == null) {
				logError("Unable to find variables group with name: " + star + ". in order to have a variables group defened the code must be inide a block such as event block (event::EVENT_TYPE => { <code> })", code);

				return code;
			}
			current = vg;


		}
		return code;
	}

	public VariablesGroup getGroup(String name) {
		for (VariablesGroup vg : variableGroups) {
			if (vg.getName().equalsIgnoreCase(name)) {
				return vg;
			}
		}
		return null;
	}

	public void setVariable(String code) {
		String[] parts = code.split("=>");
		String name = parts[0].replace("vars*", "");
		CodeVariable cv = getVariable(name);
		if (cv == null) {

			logError("Unable to find a variable with name: " + name + ". please create one with this format: 'var TYPE = VALUE'.", code);
			return;
		}
		Object def = readDef(parts[1], "setvardef");

		cv.setValue(def);


	}

	public void readVariable(String code) {
		String[] args = null;
		String[] parts = null;

		if (code.contains("=") && !code.contains("=>")) {

			parts = code.split("=");
			args = parts[0].split(" ");


		} else if (code.contains("=>")) {
			parts = code.split("=>");
			args = parts[0].split(" ");
		} else {
			args = code.split(" ");
		}
		String typeS = args[1];
		String nameS = args[2];
		CodeVariable.VariableType type = Util.getEnum(typeS, CodeVariable.VariableType.class);

		CodeVariable cv = new CodeVariable(nameS, type);
		variables.add(cv);


		if (parts != null) {

			String def = parts[1];
			Object o = readDef(def, "vardef:" + nameS + ":" + type.toString());
			cv.setValue(o);


		}
	}

	public Object readDef(String code, String propose) {

		code = code.replace(" ", "");
		if (code.contains(".")) {
			String[] dots = getDots(code, false);
			Player t = null;
			World w = null;
			File f = null;


			//
			if (dots.length >= 2) {

				if (dots[0].equalsIgnoreCase("methods")) {

					String ms = dots[1];
					String argsStr = getArgsString(ms, propose);

					ms = ms.replace(ms.substring(ms.indexOf('(')), "");


					Methods m = Methods.get(ms);
					if (m == null) {
						logError("Invalid method: " + ms + ".", code);
						return "null";
					}
					Object o = m.invoke(argsStr.split(" "));


					if (o instanceof Player) {


						t = (Player) o;
						//code = code.replace( dots[1], "getPlayer(")
					} else if (m.getReturnType() == World.class) {
						w = (World) o;
					} else if (m.getReturnType() == File.class) {
						f = (File) o;
					}
					if (dots.length >= 3) {


						// methods.getPlayer(TitanDev).teleport(vars*loc)
						for (int i = 2; i < dots.length; i++) {

							if (t != null) {

								ms = dots[i];
								argsStr = getArgsString(ms, propose);

								ms = ms.replace(ms.substring(ms.indexOf('(')), "");

								PlayerMethods pm = PlayerMethods.get(ms);


								if (pm == null) {
									logError("Invalid player method: " + ms + ".", code);
									return "null";
								}


								o = pm.invoke(t, command, currentCode, propose, argsStr.split(","));
								if (pm.getReturnType() == Player.class) {
									t = (Player) o;
								} else if (pm.getReturnType() == World.class) {
									w = (World) o;
								} else if (pm.getReturnType() == File.class) {
									f = (File) o;
								}

							} else if (w != null) {
								ms = dots[i];
								argsStr = getArgsString(ms, propose);
								ms = ms.replace(ms.substring(ms.indexOf('(')), "");

								WorldMethods pm = WorldMethods.get(ms);

								if (pm == null) {
									logError("Invalid world method: " + ms + ".", code);
									return "null";
								}
								o = pm.invoke(p, command, w, currentCode, propose, argsStr.split(" "));
								if (pm.getReturnType() == Player.class) {
									t = (Player) o;
								} else if (pm.getReturnType() == World.class) {
									w = (World) o;
								} else if (pm.getReturnType() == File.class) {
									f = (File) o;
								}
							} else if (f != null) {
								ms = dots[i];
								argsStr = getArgsString(ms, propose);
								ms = ms.replace(ms.substring(ms.indexOf('(')), "");

								FileMethods pm = FileMethods.get(ms);

								if (pm == null) {
									logError("Invalid file method: " + ms + ".", code);
									return "null";
								}
								o = pm.invoke(p, command, currentCode, f, propose, argsStr.split(" "));
								if (pm.getReturnType() == Player.class) {
									t = (Player) o;
								} else if (pm.getReturnType() == World.class) {
									w = (World) o;
								} else if (pm.getReturnType() == File.class) {
									f = (File) o;
								}
							}
						}

						if (o != null) {
							if (o instanceof Player) {
								t = (Player) o;
								return t;
							} else if (o instanceof World) {
								w = (World) o;
								return w;
							} else if (o instanceof File) {
								f = (File) o;
								return f;
							} else {
								return o;
							}
						}
					} else {

						return o + "";
					}
				}

			}

		} else if (code.contains("vars*")) {


			return getVarDef(code);

		} else {

			return code;
		}

		return code;
	}

	public String getArgsString(String method, String propose) {
		int fi = method.indexOf('(');
		int si = method.indexOf(')');


		String argsString = method.substring(fi, si).replace("(", "").replace(")", "");


		argsString = argsString.replace(" ", "");
		String[] args = argsString.split(",");

		// player.teleport(player.getLocation)
		// player.teleport(vars*loc)
		// player.teleport(world, 1, 1, 1)
		// player.teleport(methods.getWorld(gg), 1, 1 ,1)
		for (String arg : args) {

			if (arg.startsWith("!")) {

				String math = arg.replace("!", "");


				argsString = argsString.replace(arg, doMath(math));

			}

			if (arg.contains(".")) {
				argsString = argsString.replace(arg, readDef(arg, propose) + "");

			} else if (arg.startsWith("vars")) {
				String vn = arg.replace("vars*", "");
				CodeVariable v = getVariable(vn);
				argsString = argsString.replace(arg, v.getValueString());
			} else if (arg.contains("!")) {
				// player.setHealth(!player.getHealth() * 2!)


			}

		}
		return argsString;

	}

	public String doMath(String math) {
		double current = 0;
		math = math.replace("__", ".");
		// player.setHealth(!player.getHealth() x 20 / 10!)

		if (math.contains("x")) {
			String[] ps = math.split("x");

			String curren = "";
			for (String p : ps) {
				if (p.equals("vars")) {
					curren = "vars";
					continue;
				}
				if (!curren.isEmpty()) {
					p = curren + "*" + p;
					curren = "";
				}
				Object def = null;
				if (Valid.isInteger(p) || Valid.isDecimal(p)) {

					def = Util.toDouble(p);
				} else
					def = readDef(p, "mathcalc");

				if (!(def instanceof Number)) {
					return "0";
				}

				if (current == 0) {
					current = (double) def;
				} else
					current = current * ((Double) def);
			}

		}
		if (math.contains("/")) {
			String[] ps = math.split("/");

			for (String p : ps) {
				Object def = null;
				if (Valid.isInteger(p) || Valid.isDecimal(p)) {

					def = Util.toDouble(p);
				} else
					def = readDef(p, "mathcalc");

				if (!(def instanceof Number)) {
					return "0";
				}

				if (current == 0) {
					current = (double) def;
				} else
					current = current / ((Double) def);

			}
		}
		if (math.contains("+")) {
			current = current - 1;
			String[] ps = math.split("\\+");

			for (String p : ps) {
				Object def = null;
				if (Valid.isInteger(p) || Valid.isDecimal(p)) {

					def = Util.toDouble(p);
				} else
					def = readDef(p, "mathcalc");
				if (!(def instanceof Number)) {
					return "0";
				}
				if (current == 0) {
					current = (double) def;
				} else
					current = current + ((Double) def);
			}
		}
		if (math.contains("-")) {
			current = current - 1;
			String[] ps = math.split("-");

			for (String p : ps) {
				Object def = null;
				if (Valid.isInteger(p) || Valid.isDecimal(p)) {

					def = Util.toDouble(p);
				} else
					def = readDef(p, "mathcalc");
				if (!(def instanceof Number)) {
					return "0";
				}
				if (current == 0) {
					current = (double) def;
				} else
					current = current - ((Double) def);
			}
		}

		return current + "";
	}

	public CodeVariable getVariable(String name) {
		for (CodeVariable var : variables) {
			if (var.getName().equalsIgnoreCase(name)) {
				return var;
			}
		}
		return null;
	}

	public void logError(String msg, String code) {
		Common.log(Common.consoleLine());
		Common.log("Error while performing " + cmdName + " code:");
		Common.log(msg);
		Common.log("Problematic code: " + code + ".");
		Common.log(Common.consoleLine());


		Logger.log(false, Common.consoleLine());
		Logger.log(false, "Error while performing " + cmdName + " code:");
		Logger.log(false, msg);
		Logger.log(false, "Problematic code: " + code + ".");
		Logger.log(false, Common.consoleLine());
	}


}
