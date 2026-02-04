package me.titan.customcommands.container.execution;

import java.util.HashMap;
import java.util.Map;

public abstract class ReplyMessageMethod {

	public static final Map<String, ReplyMessageMethod> commandMethods = new HashMap<>();

	static {
		DefaultReplyMessageMethods.load();
	}

	String name;

	public ReplyMessageMethod(String name) {
		this.name = name;
		commandMethods.put(name.toLowerCase(), this);
	}

	public static ReplyMessageMethod findAndExecute(String cmd, ExecuteOperation op) {
		if (!cmd.startsWith("!")) return null;
		cmd = cmd.substring(1);
		String[] c = cmd.split(" ");
		String name = c[0];
		String args = c[1];
		String[] a = args.split(",");
		ReplyMessageMethod m = commandMethods.get(name.toLowerCase());
		if (m == null) {
			return null;
		}
		m.perform(op, a);
		return m;
	}

	abstract void perform(ExecuteOperation op, String... args);

	public enum DefaultReplyMessageMethods {
		WAIT() {
			@Override
			void perform(ExecuteOperation op, String... args) {
				long time = Long.parseLong(args[0]);
				op.setWaitTime(time);
			}
		};

		final ReplyMessageMethod instance;

		DefaultReplyMessageMethods() {
			instance = new ReplyMessageMethod(name()) {
				@Override
				void perform(ExecuteOperation op, String... args) {
					DefaultReplyMessageMethods.this.perform(op, args);
				}
			};
		}

		public static void load() {

		}

		void perform(ExecuteOperation op, String... args) {

		}
	}
}
