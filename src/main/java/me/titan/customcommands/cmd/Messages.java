package me.titan.customcommands.cmd;

import me.titan.customcommands.config.MessagesEnum;
import me.titan.customcommands.utils.Replacer;

import java.util.*;

public enum Messages implements MessagesEnum {


	No_Perms("&cYou don't have permission to do this."),
	Usage("&cUsage: {usage}."),
	Must_Be_Player("&cOnly players can use this command."),
	Must_Be_Console("&cThis command can only be executed from the console!"),
	Help_Message__Header(
			"&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-"
			, "               &b&l{label} Sub Commands"
			, ""),
	Help_Message__Each("&8/{label} &7{sublabel} {usage}&8: &7{description}"),
	Help_Message__Footer(""
			, "&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-&4-&6-"),
	Player_Is_Not_Online("&cPlayer {arg} is not online!"),
	Cannot_Use_Command_Limited("&4You are not allowed to run this command more than {cmdUses} times!"),
	You_Can_Use_This_Command_In("&cYou can use this command again in {time}."),

	Cooldown("&4&lERROR &cplease wait {time}!");


	final String path;

	boolean isList;

	final List<String> defaults = new ArrayList<>();

	Messages(String path, String... defaults) {
		this.path = path;
		this.defaults.addAll(Arrays.asList(defaults));
	}

	public static final Map<Messages, List<String>> messages = new HashMap<>();

	Messages(String... defaults) {
		this.path = name().replace("__", ".");
		this.isList = true;
		this.defaults.addAll(Arrays.asList(defaults));
	}


	@Override
	public String getPath() {
		return path;
	}

	@Override
	public boolean isList() {
		return isList;
	}

	Messages(String defaults) {
		this.path = name().replace("__", ".");

		this.defaults.add(defaults);
	}

	private static void putMessage(MessagesEnum msg, List<String> gg) {

		messages.put((Messages) msg, gg);
	}

	public String get() {
        return getList().get(0);

	}

	public String getReplaced(String... replacer) {
		return Replacer.getReplaced(get(), replacer);
	}

	public String getReplaced(Replacer replacer) {
		return replacer.replace(get());
	}

	public List<String> getList() {
		List<String> msgs = messages.get(this);
		if (msgs == null) {
			msgs = defaults;
		}
        return msgs;
	}

	public List<String> getListReplaced(String... rep) {
		return Replacer.getReplaced(messages.get(this), rep);
	}

	public List<String> getListReplaced(Replacer rep) {
		return rep.replace(messages.get(this));
	}

}
