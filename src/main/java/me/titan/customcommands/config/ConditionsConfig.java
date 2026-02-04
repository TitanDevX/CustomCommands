package me.titan.customcommands.config;

import me.titan.customcommands.container.execution.CommandCondition;
import me.titan.customcommands.log.Logger;
import me.titan.customcommands.utils.Util;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ConditionsConfig extends TitanConfig {



	final Map<Integer, CommandCondition> conditions = new HashMap<>();
	public ConditionsConfig(JavaPlugin plugin) {
		super("conditions.yml", plugin);
		init();

	}

	@Override
	public void init() {

		setPathPrefix(null);
		for(String ids : singleLayerKeySet("Conditions")){
			setPathPrefix("Conditions." + ids);

			int id = Integer.parseInt(ids);
			String t = getString("Condition").trim();
			String msg = getString("Message");
			String[] args = t.split(":");

			String typeS = args[0];
			CommandCondition.ConditionType type = Util.getEnum(typeS.toUpperCase(), CommandCondition.ConditionType.class);

			if(type == null){

				Logger.getInstance().forceLogError("Condition Type for condition " + ids + " (" +typeS + ") is invalid! valid types are: " + Util.joinList(", ",CommandCondition.ConditionType.values()));
				continue;
			}
			CommandCondition con = new CommandCondition(type,t.replace(typeS + ":",""),msg);
			conditions.put(id,con);


		}

	}

	public Map<Integer, CommandCondition> getConditions() {
		return conditions;
	}
}
