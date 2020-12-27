package me.titan.customcommands.container.execution;

import me.titan.customcommands.cmd.lib.CommandContext;
import me.titan.customcommands.container.Waitable;
import me.titan.customcommands.core.CustomCommandsPlugin;

import java.util.List;
import java.util.Map;

public class ExecuteOperation implements Waitable {

	final List<String> list;

	boolean started;

	long waitTime;

	protected ExecuteOperation(List<String> commands) {
		this.list = commands;
	}

	public void doAction(String item, CommandContext con, Map<Integer, Object> parsedArgs) {
	}

	public void start(CommandContext con, Map<Integer, Object> parsedArgs) {
		if (started) return;
		started = true;
		for (int i = 0; i < list.size(); i++) {
			String cmd = list.get(i);
			if (waitTime == 0) {
				doAction(cmd, con, parsedArgs);
			} else {

				iterate(i, con, parsedArgs);
				break;
			}


		}

	}

	public void iterate(int startIndex, CommandContext con, Map<Integer, Object> parsedArgs) {
		CustomCommandsPlugin.getPlugin().runLater(waitTime, () -> {


			waitTime = 0;
			for (int i = startIndex; i < list.size(); i++) {
				String cmd = list.get(i);


				if (waitTime == 0) {
					doAction(cmd, con, parsedArgs);
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
