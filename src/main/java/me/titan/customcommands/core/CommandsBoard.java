package me.titan.customcommands.core;

import me.titan.customcommands.container.CustomCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandsBoard extends HashMap<String, CustomCommand> {

	public Map<Integer, CustomCommand> byId = new HashMap<>();

}
