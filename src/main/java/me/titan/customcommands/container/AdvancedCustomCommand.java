package me.titan.customcommands.container;

import me.clip.placeholderapi.PlaceholderAPI;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.CommandTarget;
import me.titan.customcommands.config.Tags;
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

	Object setDescription(String de);

	Collection<String> getAliases();

	Object setAliases(List<String> aliases);

	String getPermission();

	void setPermission(String perm);

	int getUses();

	void setUses(int uses);

	Map<String, Integer> getUsesPerPermission();

	void setUsesPerPermission(Map<String, Integer> uses);

	Map<Integer, Map.Entry<String, String>> getRequiredArgsMap();

	void setRequiredArgsMap(Map<Integer, Map.Entry<String, String>> m);

	Map<Integer, Map.Entry<String, String>> getOptionalArgsMap();

	void setOptionalArgsMap(Map<Integer, Map.Entry<String, String>> m);


	List<String> getRawRequiredArgs();

	void setRawRequiredArgs(List<String> list);

	List<String> getRawOptionalArgs();

	void setRawOptionalArgs(List<String> list);

	void setSourceRequiredArgs(List<String> list);

	void setSourceOptionalArgs(List<String> list);

	List<String> getExecuteCommands();
	void setExecuteCommands(List<String> list);

	List<String> getReplyMessages();
	void setReplyMessages(List<String> list);
	CommandTarget getTarget();
	void setTarget(CommandTarget t);



	default String formatArgRequester(String cmd, CommandContext con, Map<Integer, Object> parsedArgs) {
		int optionalStarting = getRawRequiredArgs().size();

		for (int i : parsedArgs.keySet()) {
			String str = "{arg:" + i + "}";

			if (cmd.contains(str)) {

				String arg = con.args[i];
				if (getRequiredArgsMap().get(i).getValue().equalsIgnoreCase("message")) {


					arg = (String) parsedArgs.get(i);
				}
				cmd = cmd.replace(str, arg);


			}
		}


		if (con.isPlayer()) {
			cmd = cmd.replace("{player}", con.player.getName());
			cmd = cmd.replace("{Player}", con.player.getName());
		}
		if (CustomCommandsPlugin.getPlugin().checkPlaceHolderApiHook()) {
			cmd = PlaceholderAPI.setPlaceholders(con.player, cmd);
		}

		return cmd;
	}

	default void doCmd(String cmd, CommandContext con, Map<Integer, Object> parsedArgs, ExecuteOperation op) {


		cmd = formatArgRequester(cmd, con, parsedArgs);
		if (cmd == null) return;
		if (cmd.isEmpty()) return;
		if (CommandMethod.findAndExecute(cmd, op) != null) return;

		Object[] tagData = Tags.Command.getCommandTagData(cmd);

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

	default void sendMessage(String msg, CommandContext con, Map<Integer, Object> parsedArgs, ExecuteOperation op) {
		if (ReplyMessageMethod.findAndExecute(msg, op) != null) return;
		msg = formatArgRequester(msg, con, parsedArgs);
		if (msg == null) return;
		if (msg.isEmpty()) return;


		Object[] tagData = Tags.Message.getCommandTagData(msg);
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
