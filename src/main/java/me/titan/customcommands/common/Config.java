package me.titan.customcommands.common;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.SimpleSettings;

public class Config extends SimpleSettings {
	@Override
	protected int getConfigVersion() {
		return 0;
	}
	public static class Commands{


		private final static void init(){

			Common.log("Registering Custom Commands...");

			pathPrefix("Commands");

			CustomCommandsReader.readCommands(getConfig().getConfigurationSection("Commands"));

		}
	}
}
