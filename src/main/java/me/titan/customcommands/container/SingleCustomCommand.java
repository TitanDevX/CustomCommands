package me.titan.customcommands.container;

import me.titan.customcommands.cmd.Messages;
import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.cmd.lib.CommandTarget;
import me.titan.customcommands.cmd.lib.TitanCommand;
import me.titan.customcommands.container.execution.CommandCondition;
import me.titan.customcommands.container.execution.ExecuteOperation;
import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.data.player.PlayerCache;
import me.titan.customcommands.utils.Common;
import me.titan.customcommands.utils.Util;

import java.util.*;

public class SingleCustomCommand extends TitanCommand implements AdvancedCustomCommand, Waitable {

	final String name;



	Map<Integer, Map.Entry<String, String>> requiredArgsMap = new HashMap<>();

	Map<Integer, Map.Entry<String, String>> optionalArgsMap = new HashMap<>();

	List<String> rawRequiredArgs = new ArrayList<>();

	List<String> rawOptionalArgs = new ArrayList<>();
	List<String> executeCommands = new ArrayList<>();
	List<String> replyMessages = new ArrayList<>();

	CommandTarget target;
	long waitTime;

	List<Integer> conditions = new ArrayList<>();
	long cooldown;
	int uses = -1;
	Map<String, Integer> usesPerPerm;
	int id;
	public SingleCustomCommand(String name) {
		super(name);
		this.name = name;

		setRequiredArgs(getRawRequiredArgs());
		setOptionalArgs(getRawOptionalArgs());
		try {
			setTarget(getTarget());
		} catch (LinkageError l) {

		}


	}

	@Override
	public List<Integer> getConditions() {
		return conditions;
	}

	@Override
	public void setConditions(List<Integer> conditions) {
		this.conditions = conditions;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getUses() {
		return uses;
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
	public long getWaitTime() {
		return waitTime;
	}

	@Override
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	@Override
	public void setUses(int uses) {
		this.uses = uses;
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
	public CommandType getType() {
		return CommandType.SINGLE;
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

	@Override
	public TitanCommand getTitanCommand() {
		return this;
	}

	@Override
	public void setExecuteCommands(List<String> executeCommands) {
		if(executeCommands == null) return;
		this.executeCommands = executeCommands;
	}

	@Override
	protected boolean onCommand(CommandContext con) {

		if (isParent()) return false;


		if (con.isPlayer()) {
			PlayerCache pc = PlayerCache.getPlayerCache(con.player);
			if (!pc.checkUses(this)) {
				Common.tell(con.sender,
						Messages.Cannot_Use_Command_Limited.
								getReplaced("{cmdUses}", pc.getUsesAd(this) + ""));
				return false;
			}
			pc.IncreaseUses(this);
			long cooldown = pc.checkCooldown(this);
			if(cooldown != -1){
				con.tell(Messages.Cooldown.getReplaced("{time}", Util.formatTime(cooldown)));
				return false;
			}
			for(int conId : getConditions()){

				CommandCondition cond = CustomCommandsPlugin.getPlugin().getConditionsConfig().getConditions().get(conId);

				if(!cond.isTrue(con.player,con.args)) {
					if(cond.getMessage() != null)
					  con.tell(cond.getMessage());
					return false;

				}

			}
		}
		Map<Integer, Object> parsedArgs = new HashMap<>();

		int i = 0;
		for (Map.Entry<Integer, Map.Entry<String, String>> en : getRequiredArgsMap().entrySet()) {

			if (con.foundError) return false;

			String type = en.getValue().getValue();
			parsedArgs.put(i, resolveArg(type, i, con));


			i++;

		}
		if (con.foundError) {
			return false;
		}
		i++;

		for (Map.Entry<Integer, Map.Entry<String, String>> en : getOptionalArgsMap().entrySet()) {

			if (con.foundError || con.args.length <= i) break;

			String type = en.getValue().getValue();
			parsedArgs.put(i, resolveArg(type, i, con));


			i++;

		}
		PlayerCache.getPlayerCache(con.player).getCommandCooldowns().put(getId(),System.currentTimeMillis()/1000);


		int optionalStarting = getRawRequiredArgs().size();

		// getItem <player> <item> [gg]
		// give {arg:0} {arg:1}

		new ExecuteOperation(getExecuteCommands()) {
			@Override
			public void doAction(String item, CommandContext con, Map<Integer, Object> parsedArgs) {
				SingleCustomCommand.this.doCmd(item, con, parsedArgs, this);
			}
		}.start(con, parsedArgs);
//		for (String cmd : getExecuteCommands()) {
//			doCmd(cmd,con,parsedArgs);
//		}
		new ExecuteOperation(getReplyMessages()) {
			@Override
			public void doAction(String item, CommandContext con, Map<Integer, Object> parsedArgs) {
				SingleCustomCommand.this.sendMessage(item, con, parsedArgs, this);
			}
		}.start(con, parsedArgs);
		return true;
	}


	public Object resolveArg(String type, int i, CommandContext con) {
		if (type.equalsIgnoreCase("OfflinePlayer")) {
			return con.readOfflinePlayer(con.args[i]);
			//continue;
		} else if (type.equalsIgnoreCase("OnlinePlayer")) {
			return con.reaPlayer(con.args[i]);


		} else if (type.equalsIgnoreCase("num")) {
			return con.readInt(con.args[i]);

		} else if (type.equalsIgnoreCase("message")) {

			String msg = String.join(" ",
					Arrays.copyOfRange(con.args, i, con.args.length));
			return msg;

		} else {
			return con.args[i];
		}

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

	public void makeCommand() {


	}

	@Override
	public String getName() {
		return name;
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
	public boolean isParent() {
		return false;
	}


	@Override
	public void setOptionalArgsMap(Map<Integer, Map.Entry<String, String>> m) {

		this.optionalArgsMap = m;
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public void setCooldown(long cooldown) {

		this.cooldown= cooldown;
	}


}
