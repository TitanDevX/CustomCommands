package me.titan.customcommands.container;

import me.titan.customcommands.cmd.Messages;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.CommandTarget;
import me.titan.customcommands.cmd.lib.TitanCommand;
import me.titan.customcommands.cmd.lib.TitanSubCommand;
import me.titan.customcommands.container.execution.CommandExecuteOperation;
import me.titan.customcommands.data.player.PlayerCache;
import me.titan.customcommands.utils.Common;

import java.util.*;

public class SubCustomCommand extends TitanSubCommand implements AdvancedCustomCommand {
	final String name;


	Map<Integer, Map.Entry<String, String>> requiredArgsMap = new HashMap<>();

	Map<Integer, Map.Entry<String, String>> optionalArgsMap = new HashMap<>();

	List<String> rawRequiredArgs = new ArrayList<>();

	List<String> rawOptionalArgs = new ArrayList<>();
	List<String> executeCommands = new ArrayList<>();
	List<String> replyMessages = new ArrayList<>();

	CommandTarget target;

	ParentCustomCommand parent;
	int id;
	int uses = -1;
	Map<String, Integer> usesPerPerm;

	public SubCustomCommand(String name) {
		super(name);
		this.name = name;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public static String[] EMPTY_STRING_ARRAY = new String[0];


	@Override
	public void setUses(int uses) {
		this.uses = uses;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Map<String, Integer> getUsesPerPermission() {
		return usesPerPerm;
	}

	@Override
	public void setUsesPerPermission(Map<String, Integer> uses) {
		this.usesPerPerm = uses;
	}

	@Override
	public int getUses() {
		return uses;
	}

	@Override
	public TitanCommand getTitanCommand() {
		return parent;
	}

	@Override
	public CommandType getType() {
		return CommandType.SUB;
	}

	@Override
	public Set<String> getAliases() {
		return super.getAliases();
	}

	public void setParent(ParentCustomCommand parent) {
		this.parent = parent;
	}

	@Override
	public void setReplyMessages(List<String> replyMessages) {
		this.replyMessages = replyMessages;
	}

	@Override
	public void setSourceRequiredArgs(List<String> list) {

		setRequiredArgs(list);
	}

	@Override
	public void setSourceOptionalArgs(List<String> list) {

		setOptionalArgs(list);
	}

	@Override
	public void setRawOptionalArgs(List<String> rawOptionalArgs) {
		this.rawOptionalArgs = rawOptionalArgs;
	}

	@Override
	public void setRawRequiredArgs(List<String> rawRequiredArgs) {
		this.rawRequiredArgs = rawRequiredArgs;
	}


	public void error(CommandContext con) {
		con.tell("&cAn error occurred while trying to execute the command, please check console or contact an admin!");

	}

	@Override
	public CommandTarget getTarget() {
		return target;
	}

	@Override
	public List<String> getExecuteCommands() {
		return executeCommands;
	}

	@Override
	public List<String> getReplyMessages() {
		return replyMessages;
	}

	@Override
	public List<String> getRawOptionalArgs() {
		return rawOptionalArgs;
	}

	@Override
	public List<String> getRawRequiredArgs() {
		return rawRequiredArgs;
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object setDescription(String de) {
		setSubDescription(de);
		return null;
	}

	@Override
	public void setExecuteCommands(List<String> executeCommands) {
		this.executeCommands = executeCommands;
	}

	@Override
	public Object setAliases(List<String> aliases) {
		this.addAlias(aliases.toArray(EMPTY_STRING_ARRAY));
		return null;
	}

	@Override
	public Map<Integer, Map.Entry<String, String>> getRequiredArgsMap() {
		return requiredArgsMap;
	}

	@Override
	public void setRequiredArgsMap(Map<Integer, Map.Entry<String, String>> m) {

		this.requiredArgsMap = m;
	}

	@Override
	public Map<Integer, Map.Entry<String, String>> getOptionalArgsMap() {
		return optionalArgsMap;
	}

	@Override
	public void setOptionalArgsMap(Map<Integer, Map.Entry<String, String>> m) {

		this.optionalArgsMap = m;
	}


	@Override
	public boolean isParent() {
		return false;
	}

	@Override
	protected void onCommand(CommandContext con) {

		if (isParent()) return;


		Map<Integer, Object> parsedArgs = new HashMap<>();

		if (con.isPlayer()) {
			PlayerCache pc = PlayerCache.getPlayerCache(con.player);
			if (!pc.canExecuteCommand(this)) {
				Common.tell(con.sender,
						Messages.Cannot_Use_Command_Limited.
								getReplaced("{cmdUses}", pc.getUsesAd(this) + ""));
				return;
			}
			pc.IncreaseUses(this);
		}
		int i = 0;
		for (Map.Entry<Integer, Map.Entry<String, String>> en : getRequiredArgsMap().entrySet()) {

			if (con.foundError) return;

			String type = en.getValue().getValue();
			parsedArgs.put(i, resolveArg(type, i, con));


			i++;

		}
		if (con.foundError) {
			return;
		}
		i++;

		for (Map.Entry<Integer, Map.Entry<String, String>> en : getOptionalArgsMap().entrySet()) {

			if (con.foundError || con.args.length <= i) break;

			String type = en.getValue().getValue();
			parsedArgs.put(i, resolveArg(type, i, con));


			i++;

		}


		int optionalStarting = getRawRequiredArgs().size();

		// getItem <player> <item> [gg]
		// give {arg:0} {arg:1}

		new CommandExecuteOperation(getExecuteCommands()) {
			@Override
			public void doCmd(String cmd, CommandContext con, Map<Integer, Object> parsedArgs) {
				SubCustomCommand.this.doCmd(cmd, con, parsedArgs, this);
			}
		}.start(con, parsedArgs);
		for (String msg : getReplyMessages()) {

			sendMessage(msg, con, parsedArgs);

		}
	}

	public Object resolveArg(String type, int i, CommandContext con) {
		if (type.equalsIgnoreCase("OfflinePlayer")) {
			return con.readOfflinePlayer(con.args[i]);
			//continue;
		} else if (type.equalsIgnoreCase("OnlinePlayer")) {
			return con.reaPlayer(con.args[i]);


		} else if (type.equalsIgnoreCase("num")) {
			return con.readInt(con.args[i]);

		} else {
			return con.args[i];
		}

	}


}
