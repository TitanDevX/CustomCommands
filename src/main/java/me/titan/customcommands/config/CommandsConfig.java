package me.titan.customcommands.config;

import de.leonhard.storage.Yaml;
import me.titan.customcommands.cmd.lib.CommandTarget;
import me.titan.customcommands.container.AdvancedCustomCommand;
import me.titan.customcommands.container.ParentCustomCommand;
import me.titan.customcommands.container.SingleCustomCommand;
import me.titan.customcommands.container.SubCustomCommand;
import me.titan.customcommands.core.CustomCommandsPlugin;
import me.titan.customcommands.log.Logger;
import me.titan.customcommands.utils.Util;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

public class CommandsConfig extends TitanConfig {
	CustomCommandsPlugin plugin;

	Yaml defaultsFile;

	int size;
	public static String id_field = "dont_edit";

	public CommandsConfig(CustomCommandsPlugin plugin) {
		super("commands.yml", plugin);
		this.plugin = plugin;
		InputStream i = plugin.getResource("default.yml");
		File f = new File("default.yml");

		init();
	}

	@Override
	public void init() {

		Logger.getInstance().log("Unregistering all custom commands.");
		plugin.unregisterAllCommands();

		Logger.getInstance().log("Loading custom commands from config...");
		size = singleLayerKeySet().size();
		for (String cmd : singleLayerKeySet()) {

			if (contains(cmd + ".SubCommands")) {

				setPathPrefix(cmd);
				ParentCustomCommand parent = new ParentCustomCommand(cmd);

				parent.setAliases(getStringList("Aliases"));
				parent.setPermission(getString("Permission"));
				parent.setTarget(Util.getEnum(getOrDefault("Who_Can_Use_Command", "ALL"), CommandTarget.class));
				parent.setHelpMessageHeader(getStringList("HelpMessage.Header"));
				parent.setHelpMessageEach(getString("HelpMessage.Each"));
				parent.setHelpMessageFooter(getStringList("HelpMessage.Footer"));
				for (String sub : singleLayerKeySet(cmd + ".SubCommands")) {
					parent.getSubCustomCommands().add((SubCustomCommand) loadCommand(sub, true, cmd + ".SubCommands." + sub));
				}
				setPathPrefix(null);
				plugin.getCommandsRegistrar().registerCommand(parent);
				plugin.getCommandsBoard().put(cmd, parent);
				continue;
			}
			loadCommand(cmd, false, "");


		}


	}


	public AdvancedCustomCommand loadCommand(String cmd, boolean sub, String path) {
		Logger.getInstance().log("Loading command " + cmd + ".");
		setPathPrefix(!sub ? cmd : path);
		List<String> aliases = getStringList("Aliases");
		String permission = getString("Permission");
		List<String> reqArgs = getStringList("RequiredArgs");
		List<String> optArgs = getStringList("OptionalArgs");
		CommandTarget cmdTarget = Util.getEnum(getOrDefault("Who_Can_Use_Command", "ALL"), CommandTarget.class);

		List<String> executeCommands = getStringList("ExecuteCommands");
		List<String> replyMessages = getStringList("ReplyMessages");

		AdvancedCustomCommand scmd = null;
		if (sub) {

			scmd = new SubCustomCommand(cmd);
		} else
			scmd = new SingleCustomCommand(cmd);

		if (contains("id")) {
			int i = getInt("id");
			remove("id");
			set(id_field, i);
		}
		if (!contains(id_field)) {
			scmd.setId(++size);
			set(id_field, size);
		} else {
			scmd.setId(getInt(id_field));
		}
		if (contains("Uses")) {
			scmd.setUses(getInt("Uses"));
		}
		if (contains("UsesPerPermission")) {

			Map<String, Integer> map = new HashMap<>();

			for (String perm : getStringList("UsesPerPermission")) {


				perm = perm.trim();
				String[] args = perm.split(":");
				String pe = args[0];
				String v = args[1];
				int u = Integer.parseInt(v);

				map.put(pe, u);
			}
			scmd.setUsesPerPermission(map);
		}
		Iterator<String> it = aliases.iterator();

		while (it.hasNext()) {
			String a = it.next();
			if (a.equals(scmd.getName())) {
				Logger.getInstance().forceLog(Level.WARNING, String.format("Found an alias in %s aliases that is the same as the command name/label (%s)" +
						", please remove it to prevent this warning from showing again.", scmd.getName(), scmd.getName()));
				it.remove();
			}
		}
		scmd.setAliases(aliases);
		scmd.setPermission(permission);
		scmd.setTarget(cmdTarget);
		scmd.setDescription(getOrDefault("Description", ""));
		scmd.setExecuteCommands(executeCommands);
		scmd.setReplyMessages(replyMessages);
		scmd.setSourceRequiredArgs(reqArgs);
		scmd.setSourceOptionalArgs(optArgs);


		List<String> rReqArgs = new ArrayList<>();
		int i = 0;
		for (String r : reqArgs) {
			if (r.contains(":")) {
				String[] gg = r.split(":");
				scmd.getRequiredArgsMap().put(i, new AbstractMap.SimpleEntry<>(gg[0], gg[1]));
				r = gg[0];
			} else {
				scmd.getRequiredArgsMap().put(i, new AbstractMap.SimpleEntry<>("str", r));
			}
			rReqArgs.add(r);
			i++;
		}
		scmd.setRawRequiredArgs(rReqArgs);
		scmd.setSourceRequiredArgs(rReqArgs);

		List<String> rOptArgs = new ArrayList<>();
		for (String r : optArgs) {
			if (r.contains(":")) {
				String[] gg = r.split(":");
				scmd.getOptionalArgsMap().put(i, new AbstractMap.SimpleEntry<>(gg[0], gg[1]));
				r = gg[0];
			} else {
				scmd.getOptionalArgsMap().put(i, new AbstractMap.SimpleEntry<>("str", r));

			}
			rOptArgs.add(r);
		}
		scmd.setSourceOptionalArgs(rOptArgs);
		scmd.setRawOptionalArgs(rOptArgs);

		Logger.getInstance().forceLog(Level.INFO, "Loaded command " + cmd + ", now registering it on spigot command map...");

		if (!sub) {
			plugin.getCommandsRegistrar().registerCommand(scmd.getTitanCommand());
			plugin.getCommandsBoard().put(cmd, scmd);
			plugin.getCommandsBoard().byId.put(scmd.getId(), scmd);
		}
		Logger.getInstance().log("Registered command " + cmd + " in spigot command map.");

		setPathPrefix(null);
		return scmd;
	}


}
