package me.titan.customcommands.config;

import java.util.ArrayList;
import java.util.List;

public class Tags {
	public enum Command {
		CONSOLE(), PLAYER, PLAYER_CERTAIN(1), ALL_ONLINE(),
		ALL_ONLINE_CONSOLE;
		public static Object[] EMPTY_OBJECTS_ARRAY = new Object[0];
		int argsAmount;

		Command(int argsAmount) {
			this.argsAmount = argsAmount;
		}

		Command() {
			this.argsAmount = 0;
		}

		public static Object[] getCommandTagData(String cmd) {
			Command tag = null;
			for (Command t : values()) {
				String str = "[" + t.name() + "]";
				if (cmd.startsWith(str)) {
					tag = t;
					cmd = cmd.replace(str, "");
				}
			}
			if (tag == null) return null;

			List<Object> obs = new ArrayList<>();
			obs.add(tag);
			obs.add(cmd);
			if (tag.argsAmount > 0) {
				if (cmd.startsWith("[")) {
					String a = cmd.substring(1, cmd.indexOf("]"));
					a = a.replace("[", "").replace("]", "");
					cmd = cmd.replace(cmd.substring(0, cmd.indexOf("]")), "");
					obs.add(a);
				}
			}
			obs.set(1, cmd);
			return obs.toArray(EMPTY_OBJECTS_ARRAY);

		}
	}

	public enum Message {
		BROADCAST, SEND_OTHER(1);
		public static Object[] EMPTY_OBJECTS_ARRAY = new Object[0];
		int argsAmount;

		Message(int argsAmount) {
			this.argsAmount = argsAmount;
		}

		Message() {
			this.argsAmount = 0;
		}

		public static Object[] getCommandTagData(String cmd) {
			Message tag = null;
			for (Message t : values()) {
				String str = "[" + t.name() + "]";
				if (cmd.startsWith(str)) {
					tag = t;
					cmd = cmd.replace(str, "");
				}
			}
			if (tag == null) return null;

			List<Object> obs = new ArrayList<>();
			obs.add(tag);
			obs.add(cmd);
			if (tag.argsAmount > 0) {
				if (cmd.startsWith("[")) {
					String a = cmd.substring(1, cmd.indexOf("]"));
					a = a.replace("[", "").replace("]", "");
					cmd = cmd.replace(cmd.substring(0, cmd.indexOf("]")), "");
					obs.add(a);
				}
			}
			obs.set(1, cmd);
			return obs.toArray(EMPTY_OBJECTS_ARRAY);

		}
	}
}
