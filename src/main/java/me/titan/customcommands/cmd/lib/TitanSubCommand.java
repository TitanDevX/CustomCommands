package me.titan.customcommands.cmd.lib;

import java.util.*;

public abstract class TitanSubCommand {

	final String sublabel;
	String permission;
	protected CommandRequirements requirements = new CommandRequirements();
	String cachedUsage;
	TitanCommand parent;
	String description;

	boolean changed;

	protected abstract void onCommand(CommandContext context);

	private List<String> aliases;

	public TitanSubCommand(String sublabel) {
		this.sublabel = sublabel;
	}

	public void setParent(TitanCommand parent) {
		this.parent = parent;
	}

	public void setTarget(CommandTarget t) {
		this.requirements.commandTarget = t;
	}

	public String getSublabel() {
		return sublabel;
	}

	public String getDescription() {
		return description;
	}

	public void addAlias(String... ali) {

		if (aliases == null) aliases = new ArrayList<>();
		for (String al : ali) {
			aliases.add(al);
			if (parent != null) {
				parent.subCommands.put(al, this);
			}
		}


	}

	public void notifyChange() {
		changed = true;
	}

	public void addOptionalArgs(String... args) {
		requirements.optionalArgs.addAll(Arrays.asList(args));
	}

	public void addRequiredArgs(String... args) {
		requirements.requiredArgs.addAll(Arrays.asList(args));
	}

	public void setRequiredArgs(List<String> requiredArgs) {
		requirements.requiredArgs = requiredArgs;
	}

	public void setOptionalArgs(List<String> opt) {
		requirements.optionalArgs = opt;
	}

	public String getUsage() {


		StringBuilder req = new StringBuilder();
		StringBuilder opt = new StringBuilder();

		for (String a : requirements.requiredArgs) {
			req.append((req.length() == 0 ? "" : " ") + "<" + a + ">");
		}
		if (!requirements.optionalArgs.isEmpty()) {
			req.append(" ");
			for (String a : requirements.optionalArgs) {
				opt.append((opt.length() == 0 ? "" : " ") + "<" + a + ">");
			}
		}
		return "/" + parent.getLabel() + " " + sublabel + " " + req + opt;
	}

	public String getHelpMessageUsage() {

		if (!changed && cachedUsage != null) {
			return cachedUsage;
		}

		StringBuilder req = new StringBuilder();
		StringBuilder opt = new StringBuilder();

		for (String a : requirements.requiredArgs) {
			req.append((req.length() == 0 ? "" : " ") + "<" + a + ">");
		}
		if (!requirements.optionalArgs.isEmpty()) {
			req.append(" ");
			for (String a : requirements.optionalArgs) {
				opt.append((opt.length() == 0 ? "" : " ") + "<" + a + ">");
			}
		}
		cachedUsage = req.toString() + opt.toString();
		return cachedUsage;
	}

	public Set<String> getAliases() {
		return new HashSet<>(aliases);
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setSubDescription(String description) {
		this.description = description;
	}
}
