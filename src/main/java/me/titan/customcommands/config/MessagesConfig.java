package me.titan.customcommands.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class MessagesConfig extends TitanConfig {

	public Method enum_values;
	public Method messages_put;

	Class<? extends MessagesEnum> enumer;

	public MessagesConfig(JavaPlugin plugin, Class<? extends MessagesEnum> en) {
		super("messages.yml", plugin);
		enumer = en;
		try {
			enum_values = en.getDeclaredMethod("values");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
			messages_put = en.getDeclaredMethod("putMessage", MessagesEnum.class, List.class);

			messages_put.setAccessible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		init();

	}

	@Override
	public void init() {

		MessagesEnum[] values = new MessagesEnum[0];
		try {
			values = (MessagesEnum[]) enum_values.invoke(null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}


		for (MessagesEnum e : values) {

			Object obj = get(e.getPath());
			if (obj instanceof List) {
				try {
					messages_put.invoke(null, e, obj);
				} catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
					illegalAccessException.printStackTrace();
				}
			} else {
				try {
					messages_put.invoke(null, e, Collections.singletonList(obj + ""));
				} catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
					illegalAccessException.printStackTrace();
				}
			}


		}
	}
}
