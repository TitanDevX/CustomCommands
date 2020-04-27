package me.titan.customcommands.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.titan.customcommands.DataShortcut;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class CodeVariable extends DataShortcut {


	final VariableType type;
	Object value;

	public CodeVariable(String name, VariableType type) {
		super(name);
		this.type = type;
	}


	@Override
	public String getValueString() {

		System.out.print("YES AM I GETTING CALLED " + value + " " + type);
//		if (value == null) {
//			Common.throwError(new Throwable("Error while trying to get the variable " + getName() + " value's actual value: Null value."), "");
//		}
//		if(value instanceof Location){
//			Location loc = (Location) value;
//			return loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getWorld().getName();
//
//		}
		String re = "";
		//	try {
		if (value instanceof Location) {
			System.out.print("LOOOOOOOOOOOOOOOOOOOOOOOOOOOC");
			Location loc = (Location) value;
			re = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getWorld().getName();
		} else if (value instanceof Boolean) {
			re = value.toString();
		} else if (value instanceof String && Valid.isInteger((String) value)) {
			re = Integer.parseInt(value.toString()) + "";

		} else if (value instanceof World) {
			System.out.print("WORLD");
			re = ((World) value).getName();
		} else if (value instanceof Player) {
			re = ((Player) value).getName();

		} else if (value instanceof File) {
			re = "methods.getFile(" + ((File) value).getPath() + ")";
		} else if (value instanceof String[]) {
			re = "[" + Common.joinToString((String[]) value) + "]";


		}

//		} catch (ClassCastException ex) {
//
//			Common.throwError(new Throwable("Error while trying to get the variable " + getName() + " value's actual value: the given value doesn't meet the type class type: " + type + ", value: " + value + "."));
//		}
		return re;

	}


	@RequiredArgsConstructor
	@Getter
	public enum VariableType {
		TEXT(String.class),
		STRING(String.class),
		NUMBER(Number.class),
		BOOLEAN(Boolean.class),
		PLAYER(Player.class),
		WORLD(World.class),
		FILE(File.class),
		LOCATION(Location.class),
		LIST(List.class),
		STRING_LIST(String[].class),
		CONFIG(YamlConfiguration.class),
		VAR(Object.class);

		final Class<?> clazz;

	}
}
