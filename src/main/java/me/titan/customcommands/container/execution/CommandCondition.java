package me.titan.customcommands.container.execution;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class CommandCondition {

	final ConditionType type;
	final String value;
	final String message;


	public CommandCondition(ConditionType type, String value, String message) {
		this.type = type;
		this.value = value;
		this.message = message;
	}

	public boolean isTrue(Player p , String[] args){

		if(type.name().contains("PAPI")){
			String sp = "=";
			String comp = "";
				// %gg%>=10
				int ind = value.indexOf("=");
				comp =value.charAt(ind-1) +"";
				if(comp.equals(">") || comp.equals("<") || comp.equals("!")) {
					sp = comp + value.charAt(ind);
				}else{
					comp = "";
				}



			String value = this.value.replace(" ","");
			String[] spp = value.trim().split(sp);
			String holder = spp[0];
			String v = spp[1];
			String av = holder;
			if(holder.contains("%")){
				av = PlaceholderAPI.setPlaceholders(p,holder);
			}else{
				for(int i =0;i<args.length;i++){
					String arg = args[i];
					av = av.replace("{arg:" + i+ "}",arg);
				}
			}
			if(av.contains("{arg:")){
				return false;
			}

			boolean b = false;
			if(type == ConditionType.PAPI_INT){
				if(comp.isEmpty()){
					return Double.parseDouble(av) == Double.parseDouble( v);
				}else if(comp.equals(">")){
					return Double.parseDouble(av) >= Double.parseDouble( v);

				}else if(comp.equals("!")){
					return Double.parseDouble(av) != Double.parseDouble( v);
				}else {
					return Double.parseDouble(av) <= Double.parseDouble( v);
				}

			}else if(type == ConditionType.PAPI_STRING){
				if(comp.isEmpty()){
					return av.equals(v);
				}else if(comp.equals("!")){
					return !av.equals(v);
				}

			}else {
				if(comp.isEmpty()){
					return Boolean.parseBoolean(av) == Boolean.parseBoolean(v);
				}else if(comp.equals("!")){
					return Boolean.parseBoolean(av) != Boolean.parseBoolean(v);
				}

			}


		}else if(type == ConditionType.HAS_PERMISSION){
			return p.hasPermission(value.trim());
		}
		return true;

	}

	public ConditionType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}


	public enum ConditionType {

		PAPI_INT, PAPI_STRING, PAPI_BOOLEAN, HAS_PERMISSION

	}

}
