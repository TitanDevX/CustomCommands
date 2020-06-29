package me.titan.customcommands.code.methods;

import lombok.Getter;
import me.titan.customcommands.code.CodeMethod;
import me.titan.customcommands.code.Nothing;
import me.titan.customcommands.customcommands.ICustomCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.exception.FoException;

@Getter
public enum EntityMethods {


	TELEPORT("teleport", 4, (p, o, args) -> {
		o.teleport(Methods.getLocation(Methods.join(args)));
		return null;
	}, "teleport(x, y, z, world)", Nothing.class),
	SET_EQUIPMENT("setEquipment", 2, (p, o, args) -> {


		return null;
	}, "setEquipment(equipmentSlot, item)", Nothing.class);

	final String str;
	final int argsAmount;

	final String usage;
	final Class<?> returnType;

	CodeMethod<Entity> function;

	EntityMethods(String str, int argsAmount, String usage, Class<?> returnType) {

		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.returnType = returnType;

	}

	EntityMethods(String str, int argsAmount, CodeMethod<Entity> f, String usage, Class<?> returnType) {
		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;

		this.returnType = returnType;
		this.function = f;
	}


	public static EntityMethods get(String m) {
		for (EntityMethods pm : values()) {
			if (m.equalsIgnoreCase(pm.str)) return pm;
		}
		return null;
	}

	public Object invoke(Player p, ICustomCommand command, String code, String pr, String... args) {
		//command.getCodeMethods().put(code,getFunction());

		if (getReturnType().equals(Nothing.class))
			Methods.registerPremadeFunc(command, code, p, getFunction(), args, pr);
		if (args.length < argsAmount) {

			Common.throwError(new FoException("Invalid arguments amount for method: " + this.str + ". Correct usage: " + this.usage));
			return null;
		}

		if (function != null) {
			return function.apply(p, p, args);

		}

		return null;
	}

	public void doTeleport(Player p, String locs) {
		String[] parts = locs.split(", ");
		int x = Integer.parseInt(parts[0]);
		int y = Integer.parseInt(parts[1]);
		int z = Integer.parseInt(parts[2]);
		World w = Bukkit.getWorld(parts[3]);

		p.teleport(new Location(w, x, y, z));


	}


}
