package me.titan.customcommands.common;

import me.titan.customcommands.core.CommandsManager;
import me.titan.customcommands.customcommands.CustomCommand;
import me.titan.customcommands.customcommands.CustomCommandsGroup;
import me.titan.customcommands.customcommands.CustomSubCommand;
import me.titan.customcommands.customcommands.ICustomCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.Remain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CustomCommandsReader {

	public static void readCommands(YamlConfiguration config, File f) {
		for (CustomCommandsGroup gg : CommandsManager.getInstance().commandsGroups.values()) {
			gg.getCommand().unregister();
		}
		CommandsManager.getInstance().commandsGroups.clear();
		CommandsManager.getInstance().commands.clear();

		ConfigurationSection section = config.getConfigurationSection("Commands");

		FirstLoop:
		for (String name : section.getKeys(false)) {


			AtomicReference<String> path = new AtomicReference<>(name + ".");
			if (section.contains(path + "Sub_Commands")) {

				if (!section.getConfigurationSection(path + "Sub_Commands").getKeys(false).isEmpty()) {
					readSubCommands(name, section, path, config, f);

					continue FirstLoop;
				}


			}
			CustomCommand cc;
			if (CommandsManager.getInstance().commands.containsKey(name)) {
				cc = CommandsManager.getInstance().commands.get(name);
			} else {
				cc = new CustomCommand(name);
			}
			setValues(cc, section, path.get(), config, f);

			if (!CommandsManager.getInstance().commands.containsKey(name)) {

				CommandsManager.register(cc);
			} else {
				Remain.unregisterCommand(cc.getName(), true);
				CommandsManager.register(cc);
			}
		}

	}

	public static void readSubCommands(String name, ConfigurationSection section, AtomicReference<String> path, YamlConfiguration config, File f) {
		Common.runLater(20, () -> {
			CustomCommandsGroup ccg = new CustomCommandsGroup(name);
			if (CommandsManager.getInstance().commandsGroups.containsKey(name)) {
				ccg = CommandsManager.getInstance().commandsGroups.get(name);
			}

			String apath = path.get();

			System.out.print(ccg);
			ccg.setAliases(section.getStringList(apath + "Aliases"));
			ccg.setHelpHeader(section.getStringList(apath + "Help_Header"));
			ccg.setNoParamsMsg(section.getStringList(apath + "No_Params_Message"));

			for (String subname : section.getConfigurationSection(apath + "Sub_Commands").getKeys(false)) {
				CustomSubCommand csc = new CustomSubCommand(subname);


				apath = path.get() + ".Sub_Commands." + subname + ".";

				csc.setAliases(section.getStringList(apath + "Aliases"));
				csc.setPerms(section.getString(apath + "Permission"));

				csc.setPerformCommands(section.getStringList(apath + "Commands"));

				csc.setReplyMessages(section.getStringList(apath + "Reply_Messages"));
				System.out.print(csc.getReplyMessages());

				csc.setUsage(section.getString(apath + "Usage"));
				csc.setMinArgs(section.getInt(apath + "MinArguments"));
				csc.setCodes(section.getStringList(apath + "Code"));
				csc.setCooldown(section.getString(apath + "Cooldown"));
				ccg.getSubCommands().add(csc);


			}

			CommandsManager.reload(ccg);
//						if (!CommandsManager.getInstance().commandsGroups.containsKey(name)) {
//
//							CommandsManager.register(ccg);
//						} else {
//							Remain.unregisterCommand(ccg.getName(), true);
//							CommandsManager.register(ccg);
//						}


		});
	}
	public static void setValues(ICustomCommand cc, ConfigurationSection section, String path, YamlConfiguration config, File f) {

		cc.setAliases(section.getStringList(path + "Aliases"));
		cc.setPerms(section.getString(path + "Permission"));

		cc.setPerformCommands(section.getStringList(path + "Commands"));
		if (section.contains(path + "Replay_Messages")) {
			List<String> list = section.getStringList(path + "Replay_Messages");
			cc.setReplyMessages(list);
			section.set(path + "Replay_Messages", null);
			section.set(path + "Reply_Messages", list);
			try {
				config.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else
			cc.setReplyMessages(section.getStringList(path + "Reply_Messages"));
		cc.setUsage(section.getString(path + "Usage"));
		cc.setMinArgs(section.getInt(path + "MinArguments"));
		cc.setCodes(section.getStringList(path + "Code"));
		cc.setCooldown(section.getString(path + "Cooldown"));
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
