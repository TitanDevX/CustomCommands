package me.titan.customcommands.log;

import java.util.logging.Level;

public class Logger {

	private static Logger instance;

	public static boolean debug = false;
	java.util.logging.Logger source = java.util.logging.Logger.getLogger("Minecraft");

	public static Logger getInstance() {

		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	public void log(String msg) {


		log(Level.INFO, "[CustomCommands Logger] " + msg, false);

	}

	public void log(Level level, String msg, boolean force) {


		if (!force && !debug) return;
		source.log(level, msg);

	}

	public void forceLog(Level level, String msg) {

		log(level, "[CustomCommands] " + msg, true);

	}

	public void forceLog(String msg) {

		log(Level.INFO, "[CustomCommands] " + msg, true);

	}

	public void forceLogError(String msg) {

		log(Level.SEVERE, "[CustomCommands] " + msg, true);

	}
}
