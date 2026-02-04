package me.titan.customcommands.codeengines.tasks;

import com.google.common.collect.ArrayListMultimap;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectionalCodeEngine {

	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	Map<String, Set<Class<?>>> playersMethodCache = new HashMap<>();

	// player.isOnline. player.teleport
	public static Object performCode(String code, String[] args, Player p, Object current, String type) {

		String[] sections = code.split("\\.");

		boolean first = current == null;
		for (String sec : sections) {
			if (first) {

				if (sec.equals("player")) {
					current = p;
					type = "P";
				} else if (sec.startsWith("P:")) {

					current = Bukkit.getPlayer(sec.replace("P:", ""));
					type = "P";

				} else if (sec.startsWith("W:")) {
					current = Bukkit.getWorld(sec.replace("W:", ""));
					type = "W";
				}

			}


        }

		return null;
	}

	private static Object resolvePlayer(Player p, String method, String[] args) {

		List<Method> suspectedMethods = new ArrayList<>();
		for (Method m : Player.class.getMethods()) {
			if (m.getName().equalsIgnoreCase(method)) {
				suspectedMethods.add(m);
			}
		}
		if (suspectedMethods.isEmpty()) {

			//Common.log("Cannot find method '" + method + "' for player!");
			return null;
		}


		List<Class<?>> classesList = new ArrayList<>();
		List<Object> objectsList = new ArrayList<>();

		ArrayListMultimap<Class<?>, Integer> deprecatedTypesIndexes = ArrayListMultimap.create();
		StringBuilder currentLocation = new StringBuilder();
		int locArgs = 0;
		int maxLocArgs;
		int currentListsIndex = 0;
		for (String arg : args) {
			if (arg.startsWith("L:") && (currentLocation.length() == 0)) {
				arg = arg.replace("L:", "");
				currentLocation = new StringBuilder(arg);
				locArgs++;
				maxLocArgs = 4;
				continue;
			} else if (arg.startsWith("L2:") && (currentLocation.length() == 0)) {
				arg = arg.replace("L2:", "");
				currentLocation = new StringBuilder(arg);
				locArgs++;
				maxLocArgs = 6;
				if (currentLocation.length() > 0) {
					currentLocation.append(",").append(arg);
					++locArgs;
					if (locArgs >= maxLocArgs) {

						classesList.add(Location.class);
						objectsList.add(Util.getLocation(currentLocation.toString()));
						locArgs = 0;
						currentLocation = new StringBuilder();
						currentListsIndex++;

					}
					continue;
				}
				if (!arg.contains(".") && Util.isInteger(arg)) {

					classesList.add(int.class);
					objectsList.add(Integer.parseInt(arg));
					currentListsIndex++;
				} else {// (Valid.isDecimal(arg.replace("__", "."))) {

					classesList.add(Double.class);
					objectsList.add(Double.parseDouble(arg.replace("__", ".")));
					deprecatedTypesIndexes.put(Double.class, currentListsIndex);

					currentListsIndex++;
				}

			}
			Class<?>[] classes = classesList.toArray(EMPTY_CLASS_ARRAY);
			Object[] objects = objectsList.toArray(EMPTY_OBJECT_ARRAY);

			Method meth = null;
			for (Method m : suspectedMethods) {
				boolean changed = false;
				if (Arrays.equals(classes, m.getGenericParameterTypes())) {

					meth = m;
					break;

				} else if (classesList.contains(Double.class)) {

					for (int i : deprecatedTypesIndexes.get(Double.class)) {
						classesList.set(i, Float.class);
						objectsList.set(i, Float.parseFloat(objectsList.get(i) + ""));
					}
					classes = classesList.toArray(EMPTY_CLASS_ARRAY);
					objects = objectsList.toArray(EMPTY_OBJECT_ARRAY);

					changed = true;

				}
				if (!(changed && Arrays.equals(classes, m.getGenericParameterTypes())) && classesList.contains(String.class)) {


					for (int i : deprecatedTypesIndexes.get(String.class)) {
						classesList.set(i, Float.class);
						objectsList.set(i, Float.parseFloat(objectsList.get(i) + ""));
					}
					classes = classesList.toArray(EMPTY_CLASS_ARRAY);
					objects = objectsList.toArray(EMPTY_OBJECT_ARRAY);

					if (Arrays.equals(classes, m.getGenericParameterTypes())) {
						meth = m;
					}

				}


			}
			if (meth == null) {
				//Common.log("Invalid args given for player method " + method + ", given " + classesList + ", required: " +suspectedMethods);
				return null;
			}
			try {
				return meth.invoke(p, objects);
			} catch (IllegalAccessException | InvocationTargetException e) {


				//	Common.log("An error occurred while executing player method " + method + ".");

				e.printStackTrace();
			}

			return null;
		}
		return null;
	}
}
