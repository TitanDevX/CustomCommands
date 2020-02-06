package me.titan.customcommands.common;

import me.titan.customcommands.core.CommandsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.List;

public class CustomCommandsReader {

	public static void readCommands(ConfigurationSection section){

		for(String name : section.getKeys(false)){

			CustomCommand cc;
			if(CommandsManager.getInstance().commands.containsKey(name)){
				cc = CommandsManager.getInstance().commands.get(name);
			}else{
				cc = new CustomCommand(name);
			}
			String path =  name+ ".";
			cc.setAliases(section.getStringList(path + "Aliases"));
			cc.setPerms(section.getString(path + "Permission"));

			cc.setPerformCommands(section.getStringList(path + "Commands"));
			cc.setReplyMessages(section.getStringList(path + "Reply_Messages"));
			cc.setUsage(section.getString(path + "Usage"));
			cc.setMinArgs(section.getInt(path + "MinArguments"));
			cc.setCodes(section.getStringList(path + "Code"));
			cc.setCooldown(section.getString(path + "Cooldown"));

			if(!CommandsManager.getInstance().commands.containsKey(name)) {

				CommandsManager.register(cc);
			}else{
				Remain.unregisterCommand(cc.getName(),true);
				CommandsManager.register(cc);
			}
		}
	}




	public static String replacePlaceholders(String str,String[] args, Player p, String umsg){
	List<String> newArgs = new ArrayList<>();
		if(str.contains("{player}")){
			str = str.replace("{player}",p.getName() );
		}
		String[] strs = str.split(" ");

		for(String nstr : strs) {


			if (nstr.contains("{arg") || nstr.contains("}")) {

				nstr = nstr.replace("{","").replace("}","");

				String[] parts = nstr.split(":");
				int number = Integer.parseInt(parts[1]);
				if(nstr.contains("*")) {
					if (args.length < number + 1) {
						Common.tell(p, "&cInvalid usage: " + umsg + ".");
						return null;
					}
				}else {
					String rep = "";
					if (parts.length == 3) {
						ArgTypes at = ReflectionUtil.getEnum(parts[2].toUpperCase(), parts[2].toUpperCase(), ArgTypes.class);
						if(at == null){
							 rep = parts[2];
						}
					}else if(parts.length == 4){
						rep = parts[3];

					}
					str = str.replace(nstr, rep);

					continue;
				}
				String arg = args[number];

				if(parts.length > 2){
					ArgTypes at = ReflectionUtil.getEnum(parts[2].toUpperCase(), parts[2].toUpperCase(), ArgTypes.class);
					String gg = at.replace(arg);
					if(gg == null){
						Common.tell(p,at.getErrorMessage(number));
						return null;
					}
				}

				str = str.replace(nstr, arg);


			}
		}


		return str.replace("{","").replace("}","");

	}
	public enum ArgTypes {
		PLAYER, NUMBER, BOOLEAN, ENUM;

		public String replace(String str){
			switch (this){
				case PLAYER:
					Player p = PlayerUtil.getNickedNonVanishedPlayer(str);
					if(p == null) return null;
					return p.getName();
				case NUMBER:
					if(!Valid.isInteger(str))  return null;
					return Integer.parseInt(str) + "";
				case BOOLEAN:
					if(str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")) return Boolean.parseBoolean(str) + "";
					return null;
			}
			return null;
		}
		public String getErrorMessage(int args){
			switch (this){
				case PLAYER:
					return "&cInvalid player at argument " + args + ".";
				case NUMBER:
					return "&cInvalid number at argument " + args + ".";
				case BOOLEAN:
					return "&cInvalid boolean at argument " + args + ".";
			}
			return null;
		}
	}
}
