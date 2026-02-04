package me.titan.customcommands.container.execution;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandMethod {

	public static final Map<String, CommandMethod> commandMethods = new HashMap<>();
	String name;

	static {
		DefaultCommandMethods.load();
	}

	public CommandMethod(String name) {
		this.name = name;
		commandMethods.put(name.toLowerCase(), this);
	}

	public static CommandMethod findAndExecute(String cmd, ExecuteOperation op) {
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

	abstract void perform(ExecuteOperation op, String... args);

	public enum DefaultCommandMethods {
		WAIT() {
			@Override
			void perform(ExecuteOperation op, String... args) {
				long time = Long.parseLong(args[0]);
				op.setWaitTime(time);
			}
		};

		final CommandMethod instance;

		DefaultCommandMethods() {
			instance = new CommandMethod(name()) {
				@Override
				void perform(ExecuteOperation op, String... args) {
					DefaultCommandMethods.this.perform(op, args);
				}
			};
		}

		public static void load() {

		}

		void perform(ExecuteOperation op, String... args) {

		}
	}
}
