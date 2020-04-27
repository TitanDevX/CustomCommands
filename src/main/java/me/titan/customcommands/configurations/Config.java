package me.titan.customcommands.configurations;

import org.mineacademy.fo.settings.SimpleSettings;

public class Config extends SimpleSettings {
	@Override
	protected int getConfigVersion() {
		return 0;
	}


	public static Boolean DEBUG_MODE;
	public static Boolean LAG;
		private final static void init(){

			DEBUG_MODE = getBoolean("Debug_Mode");
			LAG = getBoolean("Is_Server_Lagging");
		}

}
