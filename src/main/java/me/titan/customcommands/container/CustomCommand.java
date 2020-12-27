package me.titan.customcommands.container;

import me.titan.customcommands.cmd.lib.TitanCommand;

public interface CustomCommand {
	int getId();
	void setId(int id);

	TitanCommand getTitanCommand();

	CommandType getType();

}
