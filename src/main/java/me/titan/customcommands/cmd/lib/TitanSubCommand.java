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

	Map<String, TitanCommand> subCommands = new HashMap<>();

	List<TitanCommand> actualSubCommands = new ArrayList<>();

	protected abstract void onCommand(CommandContext context);
	protected void doCommand(CommandContext con){
		onCommand(con);
		System.out.println("gg0" + subCommands);

		// label sublabel label2 <args>
		if(!subCommands.isEmpty()){
			if(con.args.length < 1){
				System.out.println("gg");
				con.tell(getHelpMessage());
				return;
			}
			TitanCommand cmd = subCommands.get(con.args[0].toLowerCase());
			if(cmd == null){
				System.out.println("gg2");

				con.tell(getHelpMessage());
				return;
			}
			System.out.println("gg3");

			con.args = Arrays.copyOfRange(con.args,1,con.args.length);
			cmd.doExecute(con);
			System.out.println("gg4");

		}
	}

	private List<String> aliases = new ArrayList<>();

	public TitanSubCommand(String sublabel) {
		this.sublabel = sublabel;
	}

	public void setParent(TitanCommand parent) {
		this.parent = parent;
	}

	public Map<String, TitanCommand> getSubCommands() {
		return subCommands;
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
	public String[] getHelpMessage(){
		return new String[0];
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
			req.append(req.length() == 0 ? "" : " ").append("<").append(a).append(">");
		}
		if (!requirements.optionalArgs.isEmpty()) {
			req.append(" ");
			for (String a : requirements.optionalArgs) {
				opt.append((opt.length() == 0 ? "" : " ") + "<" + a + ">");
			}
		}
		return "/" + parent.getLabel() + " " + sublabel + " " + req + opt;
	}

	public void onRegister(){
		registerSubCommands();
	}
	public void registerSubCommands(){

	}
	public void registerSubCommand(TitanCommand cmd){
		subCommands.put(cmd.getLabel().toLowerCase(), cmd);
		for (String ali : cmd.getAliases()) {
			subCommands.put(ali.toLowerCase(), cmd);
		}
		actualSubCommands.add(cmd);
		cmd.registerSubCommands();
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
