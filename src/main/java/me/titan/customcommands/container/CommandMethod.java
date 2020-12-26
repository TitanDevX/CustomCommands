package me.titan.customcommands.container;

import me.titan.customcommands.container.execution.CommandExecuteOperation;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandMethod {

	public static Map<String, CommandMethod> commandMethods = new HashMap<>();

	static {
		DefaultCommandMethods.load();
	}

	String name;

	public CommandMethod(String name) {
		this.name = name;
		commandMethods.put(name.toLowerCase(), this);
	}

	public static CommandMethod findAndExecute(String cmd, CommandExecuteOperation op) {
		if (!cmd.startsWith("!")) return null;
		cmd = cmd.substring(1);
		String[] c = cmd.split(" ");
		String name = c[0];
		String args = c[1];
		String[] a = args.split(",");
		CommandMethod m = commandMethods.get(name.toLowerCase());
		if (m == null) {
			return null;
		}
		m.perform(op, a);
		return m;
	}

	abstract void perform(CommandExecuteOperation op, String... args);

	public enum DefaultCommandMethods {
		WAIT() {
			@Override
			void perform(CommandExecuteOperation op, String... args) {
				long time = Long.parseLong(args[0]);
				op.setWaitTime(time);
			}
		};

		CommandMethod instance;

		DefaultCommandMethods() {
			instance = new CommandMethod(name()) {
				@Override
				void perform(CommandExecuteOperation op, String... args) {
					DefaultCommandMethods.this.perform(op, args);
				}
			};
		}

		public static void load() {

		}

		void perform(CommandExecuteOperation op, String... args) {

		}
	}
}
