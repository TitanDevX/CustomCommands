package me.titan.customcommands.code;

import lombok.Getter;
import lombok.Setter;
import me.titan.customcommands.DataShortcut;
import org.bukkit.event.Event;

import java.util.List;


@Getter
@Setter
public class VariablesGroup<T> extends DataShortcut {


	final GroupsType type;

	T instance;

	List<DataShortcut> variables;

	public VariablesGroup(String name, GroupsType type) {
		super(name);
		this.type = type;
	}

	@Override
	public String getValueString() {

		if (type == GroupsType.EVENT) return ((Event) instance).getEventName();
		return instance + "";
	}

	public DataShortcut getVariable(String name) {
		for (DataShortcut ds : variables) {
			if (ds.getName().equalsIgnoreCase(name)) {
				return ds;
			}
		}
		return null;
	}


	enum GroupsType {
		EVENT
	}
}
