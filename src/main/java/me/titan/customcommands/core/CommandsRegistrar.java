package me.titan.customcommands.core;

import me.titan.customcommands.log.Logger;
import me.titan.customcommands.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class CommandsRegistrar {


	final Class<?> CraftServerClass = Reflection.getOBCClass("CraftServer");
	Method getCommandMapMethod;
	Field getKnownCommandsField;
	Method MapRemoveMethod;


	public CommandsRegistrar() {


		try {
			getCommandMapMethod = Objects.requireNonNull(CraftServerClass).getDeclaredMethod("getCommandMap");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		Objects.requireNonNull(getCommandMapMethod).setAccessible(true);
		try {
			getKnownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		getKnownCommandsField.setAccessible(true);

		try {
			MapRemoveMethod = Map.class.getMethod("remove", Object.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}


	private SimpleCommandMap getCommandMap() {
		try {
			return (SimpleCommandMap) getCommandMapMethod.invoke(CraftServerClass.cast(Bukkit.getServer()));
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void registerCommand(Command cmd) {
		Objects.requireNonNull(getCommandMap()).register(cmd.getLabel(), cmd);
		Logger.getInstance().log("Register command with label " + cmd.getLabel());
	}

	public void unregisterCommand(Command cmd) {
		for (String ali : cmd.getAliases()) {

			removeKnownCommand(cmd.getLabel() + ":" + ali);
			removeKnownCommand(ali);
			Logger.getInstance().log("Unregistered alias " + ali + " for command " + cmd.getLabel());
		}

		removeKnownCommand(cmd.getLabel());


		cmd.unregister(Objects.requireNonNull(getCommandMap()));

		Logger.getInstance().log("Unregistered command " + cmd.getLabel());

	}

	private void removeKnownCommand(String str) {


		try {


			MapRemoveMethod.invoke(getKnownCommandsField.get(getCommandMap()), str);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}


	}


}
