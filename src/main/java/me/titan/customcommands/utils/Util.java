package me.titan.customcommands.utils;

import javax.annotation.RegEx;
import java.util.ArrayList;
import java.util.List;

public class Util {

	public static List<String> toLowerCaseList(List<String> list){
		List<String> s = new ArrayList<>();
		for(String str : list){
			s.add(str.toLowerCase());
		}
		return s;

	}

	public static int getIntegerParsed(String fullStr, @RegEx String splitation, int intIndex ){
		String[] s = fullStr.split(splitation);

		return Integer.parseInt(s[intIndex]);
	}
	public static String getStringIndex(String fullStr, @RegEx String splitation, int intIndex ){
		String[] s = fullStr.split(splitation);

		return s[intIndex];
	}
	public static <E extends Enum<E>> E getEnum(String name, Class<E> clazz){
		name = name.replace(" ", "_");
		name = name.toUpperCase();

		return Enum.valueOf(clazz, name);
	}
}
