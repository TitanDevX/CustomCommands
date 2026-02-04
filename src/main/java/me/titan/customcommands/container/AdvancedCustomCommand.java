package me.titan.customcommands.container;

import me.clip.placeholderapi.PlaceholderAPI;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.CommandTarget;
import me.titan.customcommands.config.Tags;
import me.titan.customcommands.container.execution.CommandCondition;
import me.titan.customcommands.container.execution.CommandMethod;
import me.titan.customcommands.container.execution.ExecuteOperation;
import me.titan.customcommands.container.execution.ReplyMessageMethod;
import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AdvancedCustomCommand extends CustomCommand {
	String getName();

	String getDescription();

	void setDescription(String de);

	Collection<String> getAliases();

	void setAliases(List<String> aliases);

	String getPermission();

	void setPermission(String perm);

	int getUses();

	void setUses(int uses);

	void setUsesResetTime(long time);
	long getUsesResetTime();

	Map<String, Integer> getUsesPerPermission();

	void setUsesPerPermission(Map<String, Integer> uses);

	Map<Integer, Map.Entry<String, String>> getRequiredArgsMap();

	void setRequiredArgsMap(Map<Integer, Map.Entry<String, String>> m);

	Map<Integer, Map.Entry<String, String>> getOptionalArgsMap();

	void setOptionalArgsMap(Map<Integer, Map.Entry<String, String>> m);


	long getCooldown();
	void setCooldown(long cooldown);

	List<String> getRawRequiredArgs();

	void setRawRequiredArgs(List<String> list);

	List<String> getRawOptionalArgs();

	void setRawOptionalArgs(List<String> list);

	void setSourceRequiredArgs(List<String> list);

	void setSourceOptionalArgs(List<String> list);

	List<String> getExecuteCommands();
	void setExecuteCommands(List<String> list);

	List<Integer> getConditions();
	void setConditions(List<Integer> cons);

	List<String> getReplyMessages();
	void setReplyMessages(List<String> list);
	CommandTarget getTarget();
	void setTarget(CommandTarget t);




	default String formatArgRequester(String cmd, CommandContext con, Map<String, Object> parsedArgs) {
		int optionalStarting = getRawRequiredArgs().size();

		for (String it : parsedArgs.keySet()) {
			String[] names = it.split(":");
			String str = "{arg:" + names[0] + "}";
			String str2 = "{" + names[1] + "}";

			int i = Integer.parseInt(names[0]);
			boolean contains1 = cmd.contains(str);
			if (contains1 || cmd.contains(str2) ) {

				String arg = con.args[i];
				if (getRequiredArgsMap().get(i).getValue().equalsIgnoreCase("message")) {


					arg = (String) parsedArgs.get(it);
				}
				if(contains1){
					cmd = cmd.replace(str, arg);
				}else {
					cmd = cmd.replace(str2, arg);
				}



			}
		}


		if (con.isPlayer()) {
			cmd = cmd.replace("{player}", con.player.getName());
			cmd = cmd.replace("{Player}", con.player.getName());
		}
		if (CustomCommandsPlugin.getPlugin().checkPlaceHolderApiHook()) {
			if(con.isPlayer()){
				cmd = PlaceholderAPI.setPlaceholders(con.player, cmd);
			}else
			  cmd = PlaceholderAPI.setPlaceholders(Bukkit.getOnlinePlayers().stream().findFirst().orElse(null),cmd) ;
		}

		return cmd;
	}

	default void doCmd(String cmd, CommandContext con, Map<String, Object> parsedArgs, ExecuteOperation op) {


		cmd = formatArgRequester(cmd, con, parsedArgs);
		if (cmd == null) return;
		if (cmd.isEmpty()) return;
		if (CommandMethod.findAndExecute(cmd, op) != null) return;

		Object[] tagData = Tags.Command.getCommandTagData(cmd);

		if(cmd.contains("[If")){
			int ind = cmd.indexOf("]");
			String str = cmd.substring(1,ind).replace("If:","");
			String[] ints = str.split(",");
			for(String ids : ints){


				int id = Integer.parseInt(ids);
				CommandCondition cond = CustomCommandsPlugin.getPlugin().getConditionsConfig().getConditions().get(id);


				if(con.isPlayer() && cond.isTrue(con.player, con.args)) return;
				cmd = cmd.replace(cmd.substring(0,ind+1),"");

			}
		}
		if (tagData == null) {
			if (cmd.startsWith("/") && con.isPlayer()) {
				con.player.performCommand(cmd.substring(1));
			} else {

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}

		} else {
			Tags.Command tag = (Tags.Command) tagData[0];
			cmd = (String) tagData[1];

			Object[] args = null;
			if (tagData.length > 2) {
				args = Arrays.copyOfRange(tagData, 2, tagData.length);

			}
			if (tag == Tags.Command.CONSOLE) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			} else if (tag == Tags.Command.PLAYER) {
				con.player.performCommand(cmd);
			} else if (tag == Tags.Command.PLAYER_CERTAIN) {
				if (args == null) return;
				String pname = (String) args[0];

				Player p = Bukkit.getPlayer(pname);
				if (p == null) {
					return;
				}
				p.performCommand(cmd);
			} else if (tag == Tags.Command.ALL_ONLINE) {

				for (Player p : Bukkit.getOnlinePlayers()) {
					String fcmd = cmd.replace("{oplayer}", p.getName());
					p.performCommand(fcmd);
				}
			} else if (tag == Tags.Command.ALL_ONLINE_CONSOLE) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					String fcmd = cmd.replace("{oplayer}", p.getName());
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), fcmd);
				}
			}

		}


	}

	default void sendMessage(String msg, CommandContext con, Map<String, Object> parsedArgs, ExecuteOperation op) {
		if (ReplyMessageMethod.findAndExecute(msg, op) != null) return;
		msg = formatArgRequester(msg, con, parsedArgs);
		if (msg == null) return;
		if (msg.isEmpty()) return;


		Object[] tagData = Tags.Message.getCommandTagData(msg);

		
		if(msg.contains("[If")){
			int ind = msg.indexOf("]",msg.indexOf("[If"));
			String str = msg.substring(1,ind).replace("If:","");
			String[] ints = str.split(",");
			for(String ids : ints){


				int id = Integer.parseInt(ids);
				CommandCondition cond = CustomCommandsPlugin.getPlugin().getConditionsConfig().getConditions().get(id);


				if(con.isPlayer() && cond.isTrue(con.player, con.args)) return;
				msg = msg.replace(msg.substring(0,ind+1),"");

			}
		}
		
		if (tagData == null) {
			con.tell(msg);
			return;
		}
		msg = (String) tagData[1];
		Tags.Message tag = (Tags.Message) tagData[0];
		Object[] args = null;
		if (tagData.length > 2) {
			args = Arrays.copyOfRange(tagData, 2, tagData.length);
		}
		if (tag == Tags.Message.BROADCAST) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				Common.tell(p, msg.replace("{oplayer}", p.getName()));
			}
		} else if (tag == Tags.Message.SEND_OTHER && args != null) {
			Player p = Bukkit.getPlayer(String.valueOf(args[0]));
			if (p == null) return;
			Common.tell(p, msg);
		}

	}


	boolean isParent();


}
