package me.titan.customcommands.code;

import me.titan.customcommands.utils.Util;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

public enum WorldMethods {


	SPAWN_ENTITY("spawnEntity", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));
		EntityType et = Util.getEnum(args[4], EntityType.class);

		p.spawnEntity(loc, et);
		return null;
	}, "spawnEntity(x, y, z, world, entity)", Boolean.class),
	GENERATE_TREE("generateTree", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));

		TreeType et = Util.getEnum(args[4], TreeType.class);

		if (!p.generateTree(loc, et)) {
			Common.tell(w, "&cUnable to generate tree here.");
		}

		return null;
	}, "generateTree(x, y, z, world, treeType)", Boolean.class),
	SET_ANIMAL_SPAWN_LIMIT("setAnimalSpawnLimit", 1, (w, p, args) -> {
		int limit = Integer.parseInt(args[0]);
		p.setAnimalSpawnLimit(limit);
		return null;
	}, "setAnimalSpawnLimit(limit)", Boolean.class),
	SET_AMBIENT_LIMIT("setAmbientSpawnLimit", 1, (w, p, args) -> {
		int limit = Integer.parseInt(args[0]);
		p.setAmbientSpawnLimit(limit);

		return null;
	}, "setAmbientSpawnLimit(limit(num))", Boolean.class),
	SPAWN_PARTICLES("spawnParticle", 6, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));

		Particle par = Util.getEnum(args[4], Particle.class);
		int count = Integer.parseInt(args[5]);

		p.spawnParticle(par, loc, count);
		return null;
	}, "spawnParticle(x, y, z, world, particle, count)", Boolean.class),
	SPAWN_FALLING_BLOCK("spawnFallingBlock", 6, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));


		CompMaterial mat = CompMaterial.fromString(args[4]);

		byte data = Byte.parseByte(args[5]);


		p.spawnFallingBlock(loc, mat.getMaterial(), data);
		return null;
	}, "spawnFallingBlock(x, y, z, world, material, data)", Boolean.class),
	SET_BLOCK("setBlockType", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));


		CompMaterial mat = CompMaterial.fromString(args[4]);


		p.getBlockAt(loc).setType(mat.getMaterial());
		return null;
	}, "setBlockType(x, y, z, world, material)", Boolean.class),
	SET_BLOCK_DATA("setBlockData", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));


		int data = Integer.parseInt(args[4]);


		Remain.setData(p.getBlockAt(loc), data);
		return null;
	}, "setBlockData(x, y, z, world,data)", Boolean.class),
	CREATE_EXPLOSION("createExplosion", 5, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args, 4));


		float f = Float.parseFloat(args[4]);

		if (!p.createExplosion(loc, f)) {
			Common.tell(w, "&cThe explosion was canceled.");
		}
		return null;
	}, "createExplosion(x, y, z, world, power)", Boolean.class),
	WORLD_BORDER_SIZE("setWorldBorderSize", 1, (w, p, args) -> {

		if (args.length > 1) {
			p.getWorldBorder().setSize(Double.parseDouble(args[0]), Long.parseLong(args[1]));
			return null;
		}
		p.getWorldBorder().setSize(Double.parseDouble(args[0]));


		return null;
	}, "setWorldBorderSize(<size>, [seconds]) seconds is the time in seconds in which the border grows or shrinks from the previous size to that being set.", Boolean.class),
	WORLD_BORDER_CENTER("setWorldBorderCenter", 4, (w, p, args) -> {
		Location loc = Methods.getLocation(Methods.join(args));

		p.getWorldBorder().setCenter(loc);


		return null;
	}, "setWorldBorderCeneter(center(location))", Boolean.class),
	WORLD_BORDER_DAMAGE("setWorldBorderDamage", 1, (w, p, args) -> {

		p.getWorldBorder().setDamageAmount(Double.parseDouble(args[0]));


		return null;
	}, "setWorldBorderDamage(damage(number))", Boolean.class),
	WORLD_BORDER_BUFFER("setWorldBorderDamageBuffer", 1, (w, p, args) -> {

		p.getWorldBorder().setDamageBuffer(Double.parseDouble(args[0]));


		return null;
	}, "setWorldBorderBuffer(blocks(number)) -> Sets the amount of blocks a player may safely be outside the border before taking damage.", Boolean.class),
	WORLD_BORDER_WARNING_DISTANCE("setWorldBorderWarningDistance", 1, (w, p, args) -> {

		p.getWorldBorder().setWarningDistance(Integer.parseInt(args[0]));


		return null;
	}, "setWorldBorderWarningDistance(distance(number, default is 5 blocks)) -> Sets the warning distance that causes the screen to be tinted red when the player is within the specified number of blocks from the border.", Boolean.class),
	WORLD_BORDER_WARNING_TIME("setWorldBorderWarningTime", 1, (w, p, args) -> {

		p.getWorldBorder().setWarningTime(Integer.parseInt(args[0]));


		return null;
	}, "setWorldBorderWarningTime(time(number, default is 15 seconds)) ->  Sets the warning time that causes the screen to be tinted red when a contracting border will reach the player within the specified time.", Boolean.class),
	RESET_WORLD_BOARDER("resetWorldBorder", 0, (w, p, args) -> {

		p.getWorldBorder().reset();


		return null;
	}, "Resets the border to default values.", Boolean.class);


	final String str;
	final int argsAmount;

	final String usage;
	final Class<?>[] argsClasses;

	CodeMethod<World> function;

	WorldMethods(String str, int argsAmount, String usage, Class<?>... argsClasses) {

		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.argsClasses = argsClasses;

	}

	WorldMethods(String str, int argsAmount, CodeMethod<World> f, String usage, Class<?>... argsClasses) {
		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.argsClasses = argsClasses;
		this.function = f;
	}


	public static Object getInvoke(String m, Player p, World w, String... args) {
		return get(m).invoke(p, w, args);
	}

	public static WorldMethods get(String m) {
		for (WorldMethods pm : values()) {
			if (m.equalsIgnoreCase(pm.str)) return pm;
		}
		return null;
	}

	public Object invoke(Player p, World w, String... args) {


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
