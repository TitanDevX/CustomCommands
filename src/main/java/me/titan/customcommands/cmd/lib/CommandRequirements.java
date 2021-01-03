package me.titan.customcommands.cmd.lib;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandRequirements {
	public List<String> requiredArgsCopy = new ArrayList<String>();
	public List<String> requiredArgs = new ArrayList<String>() {
		@Override
		public String set(int index, String element) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

			return super.set(index, element);
		}

		@Override
		public void clear() {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

			super.clear();
		}

		@Override
		public boolean add(String o) {
			super.add(o);
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends String> c) {
			super.addAll(c);
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

			return true;
		}
	};
	List<String> optionalArgs = new ArrayList<>();

	CommandTarget commandTarget;

	public CmdCheckResult check(CommandSender sender, String[] args) {

		CmdCheckResult cmdc = checkSender(sender);

		if (!cmdc.positive()) return cmdc;
		cmdc = checkArgs(args);

		return cmdc;
	}

	public CmdCheckResult checkSender(CommandSender sender) {
		if (commandTarget == null || commandTarget == CommandTarget.ALL) return CmdCheckResult.EMPTY;
		if (commandTarget == CommandTarget.PLAYERS && !(sender instanceof Player)) {
			return CmdCheckResult.NotPlayer;
		} else if (commandTarget == CommandTarget.CONSOLE && sender instanceof Player) {
			return CmdCheckResult.NotConsole;
		}
		return CmdCheckResult.EMPTY;
	}

	/**
	 * @return the option args provided.
	 */
	public CmdCheckResult checkArgs(String[] args) {

		int min = requiredArgsCopy.size();
		if (args.length < min) {
			return CmdCheckResult.argsLength();
		}
		String[] extraArgs = Arrays.copyOfRange(args, min, args.length);
		// 1
		// give <item> [player]
		//
		if (extraArgs.length == 0) {
			return CmdCheckResult.EMPTY;
		}
		return CmdCheckResult.ofOptionalArgs(extraArgs);


	}

	public static class CmdCheckResult {

		public static CmdCheckResult NotPlayer = new CmdCheckResult(null, false, true, false);
		public static CmdCheckResult NotConsole = new CmdCheckResult(null, false, false, true);
		final public boolean notPlayer;
		final List<String> optionalArgs;
		final boolean argsLength;
		final boolean notConsole;

		public static CmdCheckResult ArgsLengthNotMet;
		public static CmdCheckResult EMPTY = new CmdCheckResult(null, false, false, false);

		public CmdCheckResult(List<String> optionlArgs, boolean argsLength, boolean notPlayer, boolean notConsole) {
			this.optionalArgs = optionlArgs;
			this.argsLength = argsLength;
			this.notPlayer = notPlayer;
			this.notConsole = notConsole;
		}

		public static CmdCheckResult argsLength() {
			if (ArgsLengthNotMet == null) {
				ArgsLengthNotMet = new CmdCheckResult(null, true, false, false);
			}
			return ArgsLengthNotMet;
		}

		public static CmdCheckResult ofOptionalArgs(String[] optional) {

			return new CmdCheckResult(Arrays.asList(optional), false, false, false);
		}

		public boolean positive() {
			return !argsLength && !notPlayer && !notConsole;
		}

	}
}
