package me.titan.customcommands.customcommands;

import me.titan.customcommands.code.cache.PremadeFunction;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;

public interface ICustomCommand {

	String getName();

	List<String> getAliases();

	void setAliases(List<String> ali);

	String getPerms();

	void setPerms(String perms);

	int getMinArgs();

	void setMinArgs(int min);

	String getUsage();

	void setUsage(String usg);

	String getCooldown();

	void setCooldown(String cool);

	Map<String, PremadeFunction> getCodeMethods();

	List<String> getPerformCommands();

	void setPerformCommands(List<String> list);

	List<String> getReplyMessages();

	void setReplyMessages(List<String> list);

	List<String> getCodes();

	void setCodes(List<String> codes);

	void saveData(ConfigurationSection section);

	void setup();
}
