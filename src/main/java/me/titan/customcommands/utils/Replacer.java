package me.titan.customcommands.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version RedVsBlue (1.0)
 */
public class Replacer {

	final Map<String, String> result = new HashMap<>();

	public Replacer(String... strs) {

		for (int i = 0; i < strs.length; i = i + 2) {

			if (strs.length <= i + 1) return;
			String find = strs[i];
			String replace = strs[i + 1];

			result.put(find, replace);
		}

	}

	public static String getReplaced(String msg, String... strs) {
		Map<String, String> result = new HashMap<>();
		for (int i = 0; i < strs.length; i = i + 2) {

			if (strs.length <= i + 1) break;
			String find = strs[i];
			String replace = strs[i + 1];

			result.put(find, replace);
		}
		for (Map.Entry<String, String> en : result.entrySet()) {
			if (msg.contains(en.getKey())) {
				msg = msg.replace(en.getKey(), en.getValue());

			}
		}
		return msg;

	}

	public static List<String> getReplaced(List<String> msgs, String... strs) {
		Map<String, String> result = new HashMap<>();
		for (int i = 0; i < strs.length; i = i + 2) {

			if (strs.length <= i + 1) break;
			String find = strs[i];
			String replace = strs[i + 1];

			result.put(find, replace);
		}
		List<String> nl = new ArrayList<>();

		for (int i = 0; i < msgs.size(); i++) {
			String msg = msgs.get(i);
			for (Map.Entry<String, String> en : result.entrySet()) {
				if (msg.contains(en.getKey())) {
					msg = msg.replace(en.getKey(), en.getValue());
				}
			}
			nl.add(msg);
		}
		return nl;

	}

	public void add(Entry en) {
		result.put(en.getName(), en.getValue());
	}

	public Replacer add(Replacer replacer) {
		result.putAll(replacer.result);
		return this;
	}


	public Replacer add(String... strs) {
		if (strs.length == 2) {

			result.put(strs[0], strs[1]);
			return this;
		}
		for (int i = 0; i < strs.length; i = i + 2) {

			if (strs.length <= i + 1) return this;
			String find = strs[i];
			String replace = strs[i + 1];

			result.put(find, replace);
		}
		return this;
	}

	public String replace(String msg) {
		for (Map.Entry<String, String> en : result.entrySet()) {
			if (msg.contains(en.getKey())) {
				msg = msg.replace(en.getKey(), en.getValue());

			}
		}
		return msg;
	}

	public void updateValues(String... values) {
		int i = 0;
		for (Map.Entry<String, String> en : result.entrySet()) {
			result.put(en.getKey(), values[i]);
			i++;
		}

	}

	public List<String> replace(List<String> msgs) {
		List<String> list = new ArrayList<>();

		for (String msg : msgs) {
			msg = replace(msg);
			list.add(msg);
		}
		return list;
	}


	public static class Entry {
		final String name;
		final String value;

		public Entry(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
	}
}
