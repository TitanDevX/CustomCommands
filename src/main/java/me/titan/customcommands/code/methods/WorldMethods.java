package me.titan.customcommands.code.methods;

import lombok.Getter;
import me.titan.customcommands.code.CodeMethod;
import me.titan.customcommands.code.Nothing;
import me.titan.customcommands.customcommands.ICustomCommand;
import me.titan.customcommands.utils.Util;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

@Getter
public enum WorldMethods {


	SPAWN_ENTITY("spawnEntity", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));
		EntityType et = Util.getEnum(args[4], EntityType.class);

		p.spawnEntity(loc, et);
		return null;
	}, "spawnEntity(x, y, z, world, entity)", Nothing.class),
	GENERATE_TREE("generateTree", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));

		TreeType et = Util.getEnum(args[4], TreeType.class);

		if (!p.generateTree(loc, et)) {
			Common.tell(w, "&cUnable to generate tree here.");
		}

		return null;
	}, "generateTree(x, y, z, world, treeType)", Nothing.class),
	SET_ANIMAL_SPAWN_LIMIT("setAnimalSpawnLimit", 1, (w, p, args) -> {
		int limit = Integer.parseInt(args[0]);
		p.setAnimalSpawnLimit(limit);
		return null;
	}, "setAnimalSpawnLimit(limit)", Nothing.class),
	SET_AMBIENT_LIMIT("setAmbientSpawnLimit", 1, (w, p, args) -> {
		int limit = Integer.parseInt(args[0]);
		p.setAmbientSpawnLimit(limit);

		return null;
	}, "setAmbientSpawnLimit(limit(num))", Nothing.class),
	SPAWN_PARTICLES("spawnParticle", 6, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));

		Particle par = Util.getEnum(args[4], Particle.class);
		int count = Integer.parseInt(args[5]);

		p.spawnParticle(par, loc, count);
		return null;
	}, "spawnParticle(x, y, z, world, particle, count)", Nothing.class),
	SPAWN_FALLING_BLOCK("spawnFallingBlock", 6, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));


		CompMaterial mat = CompMaterial.fromString(args[4]);

		byte data = Byte.parseByte(args[5]);


		p.spawnFallingBlock(loc, mat.getMaterial(), data);
		return null;
	}, "spawnFallingBlock(x, y, z, world, material, data)", Nothing.class),
	SET_BLOCK("setBlockType", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));


		CompMaterial mat = CompMaterial.fromString(args[4]);


		p.getBlockAt(loc).setType(mat.getMaterial());
		return null;
	}, "setBlockType(x, y, z, world, material)", Nothing.class),
	SET_BLOCK_DATA("setBlockData", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));


		int data = Integer.parseInt(args[4]);


		Remain.setData(p.getBlockAt(loc), data);
		return null;
	}, "setBlockData(x, y, z, world,data)", Nothing.class),
	CREATE_EXPLOSION("createExplosion", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));


		float f = Util.toFloat(args[4]);

		if (!p.createExplosion(loc, f)) {
			Common.tell(w, "&cThe explosion was canceled.");
		}
		return null;
	}, "createExplosion(x, y, z, world, power)", Nothing.class),
	WORLD_BORDER_SIZE("setWorldBorderSize", 1, (w, p, args) -> {

		if (args.length > 1) {
			p.getWorldBorder().setSize(Util.toDouble(args[0]), Long.parseLong(args[1]));
			return null;
		}
		p.getWorldBorder().setSize(Util.toDouble(args[0]));


		return null;
	}, "setWorldBorderSize(<size>, [seconds]) seconds is the time in seconds in which the border grows or shrinks from the previous size to that being set.", Boolean.class),
	WORLD_BORDER_CENTER("setWorldBorderCenter", 4, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args));

		p.getWorldBorder().setCenter(loc);


		return null;
	}, "setWorldBorderCeneter(center(location))", Nothing.class),
	WORLD_BORDER_DAMAGE("setWorldBorderDamage", 1, (w, p, args) -> {

		p.getWorldBorder().setDamageAmount(Util.toDouble(args[0]));


		return null;
	}, "setWorldBorderDamage(damage(number))", Nothing.class),
	WORLD_BORDER_BUFFER("setWorldBorderDamageBuffer", 1, (w, p, args) -> {

		p.getWorldBorder().setDamageBuffer(Util.toDouble(args[0]));


		return null;
	}, "setWorldBorderBuffer(blocks(number)) ->" +
			" Sets the amount of blocks a player may safely be outside the border before taking damage.", Nothing.class),
	WORLD_BORDER_WARNING_DISTANCE("setWorldBorderWarningDistance", 1, (w, p, args) -> {

		p.getWorldBorder().setWarningDistance(Integer.parseInt(args[0]));


		return null;
	}, "setWorldBorderWarningDistance(distance(number, default is 5 blocks)) ->" +
			" Sets the warning distance that causes the screen to be tinted red when the player is within the" +
			" specified number of blocks from the border.", Nothing.class),
	WORLD_BORDER_WARNING_TIME("setWorldBorderWarningTime", 1, (w, p, args) -> {

		p.getWorldBorder().setWarningTime(Integer.parseInt(args[0]));


		return null;
	}, "setWorldBorderWarningTime(time(number, default is 15 seconds)) ->" +
			"  Sets the warning time that causes the screen to be tinted red when a contract" +
			"ing border will reach the player within the specified time.", Nothing.class),
	RESET_WORLD_BOARDER("resetWorldBorder", 0, (w, p, args) -> {

		p.getWorldBorder().reset();


		return null;
	}, "Resets the border to default values.", Nothing.class),
	GET_RANDOM_LOC("getRandomLocation", 1, (w, p, args) -> {


		return RandomUtil.nextLocation(w.getLocation(), Util.toDouble(args[0]), false);

	}, "Gets random location in this world.", Nothing.class);


	final String str;
	final int argsAmount;

	final String usage;
	final Class<?> returnType;

	CodeMethod<World> function;

	WorldMethods(String str, int argsAmount, String usage, Class<?> argsClasses) {

		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.returnType = argsClasses;

	}

	WorldMethods(String str, int argsAmount, CodeMethod<World> f, String usage, Class<?> argsClasses) {
		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.returnType = argsClasses;
		this.function = f;
	}


	public static WorldMethods get(String m) {
		for (WorldMethods pm : values()) {
			if (m.equalsIgnoreCase(pm.str)) return pm;
		}
		return null;
	}

	public Object invoke(Player p, ICustomCommand command, World w, String code, String pr, String... args) {
		Methods.registerPremadeFunc(command, code, w, getFunction(), args, pr);

		if (args.length < argsAmount) {

			Common.throwError(new FoException("Invalid arguments amount for method: " + this.str + " usage: " + this.usage));
			return null;
		}

		if (function != null) {
			return function.apply(p, w, args);

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
