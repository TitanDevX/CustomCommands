package me.titan.customcommands.cmd.lib;

import com.google.common.collect.Lists;
import me.titan.customcommands.cmd.Messages;
import me.titan.customcommands.utils.Common;
import me.titan.customcommands.utils.Replacer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class TitanCommand extends Command {

	public static String[] EMPTY_STRING_ARRAY = new String[0];
	public final CommandRequirements requirements = new CommandRequirements();
	List<TitanSubCommand> actualSubCommands = new ArrayList<>();
	Map<String, TitanSubCommand> subCommands = new HashMap<>();

	boolean changed;
	String cachedUsage;

	protected TitanCommand(String name) {
		super(name);

		setAliases(Lists.newArrayList());
	}

	@Override
	public final boolean register(CommandMap commandMap) {
		boolean b = super.register(commandMap);
		if (!b) return false;
		registerSubCommands();
		return true;
	}

	public final void setTarget(CommandTarget t) {
		this.requirements.commandTarget = t;
	}

	public final void registerSubCommand(TitanSubCommand cmd) {
		subCommands.put(cmd.getSublabel().toLowerCase(), cmd);
		for (String ali : cmd.getAliases()) {
			subCommands.put(ali.toLowerCase(), cmd);
		}
		cmd.setParent(this);
		actualSubCommands.add(cmd);
	}

	public void registerSubCommands() {
	}

	@Override
	public final boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (!checkPerms(sender, getPermission())) return false;
		CommandRequirements.CmdCheckResult checkResult = requirements.check(sender, args);
		if (checkResult.notPlayer) {
			Common.tell(sender, Messages.Must_Be_Player.get());
			return false;
		} else if (checkResult.notConsole) {
			Common.tell(sender, Messages.Must_Be_Console.get());
			return false;
		} else if (checkResult.argsLength) {

			Common.tell(sender, getUsageMessage());
			return false;
		}
		CommandContext con = CommandContext.of(sender, checkResult, args);


		if (!onCommand(con)) return false;
		if (!subCommands.isEmpty()) {

			if (args.length < 1) {
				Common.tell(sender, getHelpMessage(sender));
				return false;
			}
			String sub = args[0].toLowerCase();
			if (!subCommands.containsKey(sub)) {
				Common.tell(sender, getHelpMessage(sender));
				return false;
			}
			TitanSubCommand cmd = subCommands.get(sub);
			if (!checkPerms(sender, cmd.getPermission())) return false;
			con.args = Arrays.copyOfRange(args, 1, args.length);
			CommandRequirements.CmdCheckResult subCheckResult = cmd.requirements.check(sender, con.args);
			if (subCheckResult.notPlayer) {
				Common.tell(sender, Messages.Must_Be_Player.get());
				return false;
			} else if (subCheckResult.notConsole) {
				Common.tell(sender, Messages.Must_Be_Console.get());
				return false;
			} else if (subCheckResult.argsLength) {
				Common.tell(sender, cmd.getUsage());
				return false;
			}
			cmd.onCommand(con);
		}
		changed = false;


		return false;
	}

	public boolean checkPerms(CommandSender sender, String perms) {
		if (perms != null && !perms.isEmpty() && !sender.hasPermission(perms)) {

			if (getPermissionMessage() == null || getPermissionMessage().isEmpty()) {

				Common.tell(sender, Messages.No_Perms.get());

			} else {
				Common.tell(sender, getPermissionMessage());
			}
			return false;
		}
		return true;
	}

	public void notifyChange() {
		changed = true;
	}

	public final void aliases(String... ali) {
		getAliases().addAll(Arrays.asList(ali));
	}

	public final void addOptionalArgs(String... arg) {
		requirements.optionalArgs.addAll(Arrays.asList(arg));
		notifyChange();
	}

	public final void addRequiredArgs(String... arg) {
		requirements.requiredArgs.addAll(Arrays.asList(arg));
		notifyChange();
	}

	public final void setOptionalArgs(List<String> arg) {
		requirements.optionalArgs = arg;
		notifyChange();
	}

	public final void setRequiredArgs(List<String> arg) {
		requirements.requiredArgsCopy = arg;
		notifyChange();

	}

	@Override
	public void setPermission(String permission) {
		super.setPermission(permission);

		notifyChange();
	}

	public Object[] getHelpMessageCustom() {
		return null;

	}


	public final List<String> getHelpMessage(CommandSender sender) {


		List<String> header = Messages.Help_Message__Header.getList();//

		Object[] customHelpMessage = getHelpMessageCustom();
		if (customHelpMessage != null && customHelpMessage.length > 0 && customHelpMessage[0] != null) {
			header = (List<String>) customHelpMessage[0];
		}


		header = Replacer.getReplaced(header, "{label}", getLabel());
		String each = Messages.Help_Message__Each.get();
		if (customHelpMessage != null && customHelpMessage.length > 1 && customHelpMessage[1] != null) {
			each = (String) customHelpMessage[1];
		}
		List<String> footer = Messages.Help_Message__Footer.getList();
		if (customHelpMessage != null && customHelpMessage.length > 2 && customHelpMessage[2] != null) {
			footer = (List<String>) customHelpMessage[2];
		}
		List<String> msg = new ArrayList<>(header);

		for (TitanSubCommand cmd : actualSubCommands) {

			if (cmd.getPermission() != null && !sender.hasPermission(cmd.getPermission())) continue;
			msg.add(Replacer.getReplaced(each, "{label}", getLabel(), "{sublabel}", cmd.getSublabel(), "{usage}", cmd.getHelpMessageUsage(), "{description}", cmd.getDescription() == null ? "" : cmd.getDescription()));

		}
		msg.addAll(footer);
		return msg;


	}

	@Override
	public String getUsage() {


		StringBuilder req = new StringBuilder();
		StringBuilder opt = new StringBuilder();

		for (String a : requirements.requiredArgsCopy) {
			req.append((req.length() == 0 ? "" : " ") + "<" + a + ">");
		}
		if (!requirements.optionalArgs.isEmpty()) {
			req.append(" ");
			for (String a : requirements.optionalArgs) {
				opt.append((opt.length() == 0 ? "" : " ") + "<" + a + ">");
			}
		}
		usageMessage = "/" + getLabel() + " " + req + opt;
		return usageMessage;
	}

	public String getUsageMessage() {
		return Messages.Usage.getReplaced("{usage}", getUsage());
	}

	protected abstract boolean onCommand(CommandContext context);

	@Override
	public @NotNull List<String> tabComplete
			(@NotNull CommandSender sender, @NotNull String alias,
			 @NotNull String[] args) throws IllegalArgumentException {

		if (args.length == 1) {
			if (!subCommands.isEmpty()) {
				List<String> strs = new ArrayList<>();
				for (String str : subCommands.keySet()) {
					if (str.startsWith(args[0])) {
						strs.add(str);
					}
					//strs.add(str);

				}
				return strs;
			}
		}


		return super.tabComplete(sender, alias, args);

	}
}
