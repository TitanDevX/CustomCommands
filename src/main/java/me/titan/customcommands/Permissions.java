package me.titan.customcommands;

public enum Permissions {

	CustomCommands_admin, CustomCommands_create, CustomCommands_reload;

	public String perm;

	Permissions() {
		perm = name().toLowerCase().replace("_", ".");
	}

}
