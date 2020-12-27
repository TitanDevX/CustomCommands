package me.titan.customcommands.container.execution;

import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.container.Waitable;
import me.titan.customcommands.core.CustomCommandsPlugin;

import java.util.List;
import java.util.Map;

public class CommandExecuteOperation implements Waitable {

	final List<String> commands;

	boolean started;

	long waitTime;

	protected CommandExecuteOperation(List<String> commands) {
		this.commands = commands;
	}

	public void doCmd(String cmd, CommandContext con, Map<Integer, Object> parsedArgs) {
	}

	public void start(CommandContext con, Map<Integer, Object> parsedArgs) {
		if (started) return;
		started = true;
		for (int i = 0; i < commands.size(); i++) {
			String cmd = commands.get(i);
			if (waitTime == 0) {
				doCmd(cmd, con, parsedArgs);
			} else {

				iterate(i, con, parsedArgs);
				break;
			}


		}

	}

	public void iterate(int startIndex, CommandContext con, Map<Integer, Object> parsedArgs) {
		CustomCommandsPlugin.getPlugin().runLater(waitTime, () -> {


			waitTime = 0;
			for (int i = startIndex; i < commands.size(); i++) {
				String cmd = commands.get(i);


				if (waitTime == 0) {
					doCmd(cmd, con, parsedArgs);
				} else {


					iterate(i, con, parsedArgs);

					break;
				}


			}
		});
	}
	@Override
	public long getWaitTime() {
		return waitTime;
	}

	@Override
	public void setWaitTime(long time) {

		this.waitTime = time;
	}
}
