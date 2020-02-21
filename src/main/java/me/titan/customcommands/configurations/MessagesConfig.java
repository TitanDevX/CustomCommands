package me.titan.customcommands.configurations;

import org.mineacademy.fo.settings.SimpleLocalization;
import org.mineacademy.fo.settings.YamlStaticConfig;

public class MessagesConfig extends YamlStaticConfig {
	public static String usageMsg;

	private final static void init() {

		SimpleLocalization.NO_PERMISSION = getString("No_Permission_Message");

		usageMsg = getString("Invaild_Usage_Message");

	}

	@Override
	protected void load() throws Exception {
		this.createFileAndLoad("messages.yml");
	}
}
