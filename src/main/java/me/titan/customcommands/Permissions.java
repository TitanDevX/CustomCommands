package me.titan.customcommands;

public enum Permissions {

	CustomCommands_admin, CustomCommands_create, CustomCommands_reload, CustomCommands_edit;

	public final String perm;

	Permissions() {
		perm = name().toLowerCase().replace("_", ".");
	}

}
