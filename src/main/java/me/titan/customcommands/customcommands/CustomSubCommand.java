package me.titan.customcommands.customcommands;

import lombok.Getter;
import lombok.Setter;
import me.titan.customcommands.titancommands.TitanSubCommand;
import me.titan.customcommands.utils.ObjectsSet;
import me.titan.customcommands.utils.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class CustomSubCommand extends YamlSectionConfig implements ICustomCommand {

	final String name;

	TitanSubCommand command;

	List<String> aliases = new ArrayList<>();
	String perms;

	int minArgs = 0;
	String usage;

	String cooldown;

	List<String> performCommands = new ArrayList<>();
	List<String> replyMessages = new ArrayList<>();
	List<String> codes = new ArrayList<>();

	CustomCommandsGroup mainCommand;


	/**
	 * Create a new section config with a section prefix,
	 * for example Players for storing player data.
	 *
	 * @param name
	 */
	public CustomSubCommand(String name) {
		super(name);
		this.name = name;

		setHeader("This file is updated.");
		this.command = new TitanSubCommand(this);
		loadConfiguration(null, "data.db");
	}

	@Override
	public void setup() {
		List<String> ali = this.getAliases();
		TitanSubCommand tsc = new TitanSubCommand(this);
		this.command = tsc;
	}

	@Override
	protected void onLoadFinish() {

	}

	@Override
	public void saveData(ConfigurationSection sec) {


		String path = getMainCommand().getName() + ".Sub_Commands." + getName() + ".";

		sec.set(path + "Aliases", getAliases());
		sec.set(path + "Permission", perms);
		sec.set(path + "Commands", getPerformCommands());
		sec.set(path + "Reply_Messages", getReplyMessages());
		sec.set(path + "Usage", usage);
		sec.set(path + "MinArguments", minArgs);
		sec.set(path + "Code", codes);
		sec.set(path + "Cooldown", cooldown);

	}


	public ObjectsSet<Integer, TimeUnit> getDelayObjects() {
		if (cooldown == null || cooldown.isEmpty()) return null;
		int time = Util.getIntegerParsed(cooldown, " ", 0);
		TimeUnit tu = ReflectionUtil.getEnum(Util.getStringIndex(cooldown, " ", 1).toUpperCase(), Util.getStringIndex(cooldown, " ", 1).toUpperCase(), TimeUnit.class);

		return new ObjectsSet<>(time, tu);
	}
}