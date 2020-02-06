package me.titan.customcommands.code;

import me.titan.customcommands.utils.Util;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import java.util.function.Function;

public enum Methods {

	GET_WORLD("getWorld", 1,(args) -> {
		return Bukkit.getWorld(args[0]);

	}, World.class);
	final String str;
	final int argsAmount;

	final Class<?> returnType;

	Function<String[],Object> function;

	private Methods(String str, int argsAmount, Class<?> returnType) {

		this.str = str;
		this.argsAmount = argsAmount;
		this.returnType = returnType;

	}
	private Methods(String str, int argsAmount, Function<String[],Object> f, Class<?> returnType) {
		this.str = str;
		this.argsAmount = argsAmount;
		this.returnType = returnType;
		this.function = f;
	}
	public static Object getInvoke(String m, String... args){
		return get(m).invoke(args);
	}

	public static Methods get(String m){
		for(Methods pm : values()){
			if(m.equalsIgnoreCase(pm.str)) return pm;
		}
		return null;
	}
	public static boolean isMethod(String m){
		return get(m) != null;
	}
	public Object invoke( String... args){



		if(args.length < argsAmount) return null;

		if(function != null){
			return function.apply(args);

		}
		return null;
	}
	public static Location getLocation(String locs) {
		locs = locs.replace(" ", "");
		String[] parts = locs.split(",");
		int x = Integer.parseInt(parts[0]);
		int y = Integer.parseInt(parts[1]);
		int z = Integer.parseInt(parts[2]);
		World w = Bukkit.getWorld(parts[3]);

		return new Location(w, x, y, z);
	}
	public enum PlayerMethods{

		TELEPORT("teleport", 4, (p,o, args) -> {
			p.teleport(getLocation(join(args)));
			return null;
		},"teleport(x, y, z, world)", Location.class),
		SET_HEALTH("setHealth", 1,(p,o, args) -> {
			p.setHealth(Double.parseDouble(args[0]));
			return null;
		},"setHealth(newHealth)",Double.class ),
		GET_HEALTH("getHealth",0,(p,o, args) -> {
			return p.getHealth();
		},"getHealth"),
		RESET_HEALTH("resetHealth",0, (p,o, args) -> {
			p.resetMaxHealth();
			return null;
		},"resetHealth()"),
		SET_SPAWN_LOCATION("setSpawnLocation",4, (p,o, args) -> {
			p.setBedSpawnLocation(getLocation(join(args)));
			return null;
		},"setSpawnLocation(x, y, z, world)"),
		SET_GAMEMODE("setGamemode",1, (p,o, args) -> {
			GameMode gm = ReflectionUtil.getEnum(args[0].toUpperCase(), args[0].toUpperCase(), GameMode.class);
			p.setGameMode(gm);
			return null;
		},"setGamemode(gameMode)", GameMode.class),
		SET_ALLOW_FLY("setAllowFly", 1, (p,o, args) -> {
			p.setAllowFlight(Boolean.parseBoolean(args[0]));
			return null;
		},"setAllowFly(true/false)", Boolean.class),
		SET_FLYING("setFlying",1,(p,o, args) -> {
			p.setFlying(Boolean.parseBoolean(args[0]));
			return null;
		},"setFlying(true/false)", Boolean.class),
		SET_FLY_SPEED("setFlySpeed",1,(p,o, args) -> {
			p.setFlySpeed(Float.parseFloat(args[0]));
			return null;
		},"setFlySpeed(FlySpeed)",Float.class),
		SET_WALK_SPEED("setWalkSpeed",1,(p,o, args) -> {
			p.setWalkSpeed(Float.parseFloat(args[0]));
			return null;
		},"setWalkSpeed(WalkSpeed)",Float.class),
		GET_WALK_SPEED("getWalkSpeed",0,(p,o, args) -> {
			return p.getWalkSpeed();
		},"getWalkSpeed"),
		GET_Fly_SPEED("getFlySpeed",0,(p,o, args) -> {
			return p.getFlySpeed();
		},"getFlySpeed"),
		SET_EXP("setExp",1,(p,o, args) -> {
			p.setExp(Float.parseFloat(args[0]));
			return null;
		},"setExp(Exp)", Float.class),
		SET_SPRINITING("setSprinting",1,(p,o, args) -> {
			p.setSprinting(Boolean.parseBoolean(args[0]));

			return null;
		},"setSprinting(true/false)", Boolean.class),
		SEND_TITLE("sendTitle",2,(p,o, args) -> {

			Remain.sendTitle(p,args[0], args[1]);
			return null;
		},"sendTitle(Title, subtitle)", Boolean.class),
		SEND_ACTION_BAR("sendActionBarMessage",1,(p,o, args) -> {

			Remain.sendActionBar(p,args[0]);
			return null;
		},"sendActionBarMessage(actionBar)", Boolean.class),
		SEND_TOAST("sendToast",2,(p,o, args) -> {

			CompMaterial icon;
			System.out.print(Common.joinToString(args));
			if(args.length >= 2){

				icon = CompMaterial.fromString(args[1].replace(" ", ""));
				if(icon != null) {

					Remain.sendToast(p, args[0], icon);
					return null;
				}
			}
			Remain.sendToast(p,args[0]);


			return null;
		},"sendToast(toast, <optional> icon (Material))", Boolean.class),
		SET_ITEM_COOLDOWN("setItemCooldown",2,(p,o, args) -> {


				CompMaterial mat= CompMaterial.fromString(args[0]);
				int sec = Integer.parseInt(args[1]) * 20;

				Remain.setCooldown(p,mat.getMaterial(),sec);





			return null;
		},"setItemCooldown(Item, seconds)", Boolean.class),
		RESPAWN("respawn",0,(p,o, args) -> {


			System.out.print("respawn called!");
			p.setHealth(0);
			Remain.respawn(p, 5);
			return null;
		},"respawn()", Boolean.class),
		PLAY_SOUND("playSound",1,(p,o, args) -> {
			ReflectionUtil.getEnum(args[0], args[0].toUpperCase(), CompSound.class).play(p);
			return null;
		},"playSound(Sound)", String.class);

		final String str;
		final int argsAmount;

		final String usage;
		final Class<?>[] argsClasses;

		CodeMethod<Player> function;

		private PlayerMethods(String str, int argsAmount, String usage, Class<?>... argsClasses) {

			this.str = str;
			this.usage = usage;
			this.argsAmount = argsAmount;
			this.argsClasses = argsClasses;

		}
		private PlayerMethods(String str, int argsAmount, CodeMethod<Player> f, String usage, Class<?>... argsClasses) {
			this.str = str;
			this.usage = usage;
			this.argsAmount = argsAmount;
			this.argsClasses = argsClasses;
			this.function = f;
		}


		public static Object getInvoke(String m,Player p, String... args){
			return get(m).invoke(p,args);
		}

		public static PlayerMethods get(String m){
			for(PlayerMethods pm : values()){
				if(m.equalsIgnoreCase(pm.str)) return pm;
			}
			return null;
		}
		public Object invoke(Player p, String... args){


			if(args.length < argsAmount) {

				Common.throwError(new FoException("Invalid arguments amount for method: " + this.str + ". Correct usage: "  + this.usage));
				return null;
			}

			if(function != null){
				return function.apply(p,p,args);

			}

			return null;
		}
		public void doTeleport(Player p, String locs){
			String[] parts = locs.split(", ");
			int x = Integer.parseInt(parts[0]);
			int y =Integer.parseInt(parts[1]);
			int z =Integer.parseInt(parts[2]);
			World w = Bukkit.getWorld(parts[3]);

			p.teleport(new Location(w,x,y,z));


		}


	}
	public enum WorldMethods{

		SPAWN_ENTITY("spawnEntity",5,(w, p, args) -> {
			Location loc = getLocation(join(args, 4));
			EntityType et = Util.getEnum(args[4],EntityType.class);

		p.spawnEntity(loc,et);
			return null;
		},"spawnEntity(x, y, z, world, entity)", Boolean.class),
		GENERATE_TREE("generateTree",5,(w, p, args) -> {
			Location loc = getLocation(join(args, 4));

			System.out.print(Common.joinToString(args));
			TreeType et = Util.getEnum(args[4], TreeType.class);

			if(!p.generateTree(loc,et)){
				Common.tell(w,"&cUnable to generate tree here.");
			}

			return null;
		},"generateTree(x, y, z, world, treeType)", Boolean.class),
		SET_ANIMAL_SPAWN_LIMIT("setAnimalSpawnLimit",1,(w, p, args) -> {
			int limit = Integer.parseInt(args[0]);
			p.setAnimalSpawnLimit(limit);
			return null;
		},"setAnimalSpawnLimit(limit)", Boolean.class),
		SET_AMBIENT_LIMIT("setAmbientSpawnLimit",1,(w, p, args) -> {
			int limit = Integer.parseInt(args[0]);
			p.setAmbientSpawnLimit(limit);

			return null;
		},"setAmbientSpawnLimit(limit(num))", Boolean.class),
		SPAWN_PARTICLES("spawnParticle",6,(w, p, args) -> {
			Location loc = getLocation(join(args, 4));

			Particle par =  Util.getEnum(args[4],Particle.class);
			int count = Integer.parseInt(args[5]);

			p.spawnParticle(par,loc,count);
			return null;
		}, "spawnParticle(x, y, z, world, particle, count)",Boolean.class),
		SPAWN_FALLING_BLOCK("spawnFallingBlock",6,(w, p, args) -> {
			Location loc = getLocation(join(args, 4));


			CompMaterial mat = CompMaterial.fromString(args[4]);

			byte data = Byte.parseByte(args[5]);


			p.spawnFallingBlock(loc,mat.getMaterial(),data);
			return null;
		}, "spawnFallingBlock(x, y, z, world, material, data)",Boolean.class),
		SET_BLOCK("setBlockType",5,(w, p, args) -> {
			Location loc = getLocation(join(args, 4));


			CompMaterial mat = CompMaterial.fromString(args[4]);




			p.getBlockAt(loc).setType(mat.getMaterial());
			return null;
		}, "setBlockType(x, y, z, world, material)",Boolean.class),
		SET_BLOCK_DATA("setBlockData",5,(w, p, args) -> {
			Location loc = getLocation(join(args, 4));


			int data = Integer.parseInt(args[4]);




			Remain.setData(p.getBlockAt(loc),data);
			return null;
		}, "setBlockData(x, y, z, world,data)",Boolean.class),
		CREATE_EXPLOSION("createExplosion",5,(w, p, args) -> {
			Location loc = getLocation(join(args, 4));

			float f = Float.parseFloat(args[4]);

			if(!p.createExplosion(loc,f )){
				Common.tell(w,"&cThe explosion was canceled.");
			}
			return null;
		},"createExplosion(x, y, z, world, power)", Boolean.class);

		final String str;
		final int argsAmount;

		final String usage;
		final Class<?>[] argsClasses;

		CodeMethod<World> function;

		private WorldMethods(String str, int argsAmount,String usage, Class<?>... argsClasses) {

			this.str = str;
			this.usage = usage;
			this.argsAmount = argsAmount;
			this.argsClasses = argsClasses;

		}
		private WorldMethods(String str, int argsAmount, CodeMethod<World> f,String usage, Class<?>... argsClasses) {
			this.str = str;
			this.usage = usage;
			this.argsAmount = argsAmount;
			this.argsClasses = argsClasses;
			this.function = f;
		}


		public static Object getInvoke(String m,Player p,World w, String... args){
			return get(m).invoke(p,w,args);
		}

		public static WorldMethods get(String m){
			for(WorldMethods pm : values()){
				if(m.equalsIgnoreCase(pm.str)) return pm;
			}
			return null;
		}
		public Object invoke(Player p, World w, String... args){


			if(args.length < argsAmount) {

				Common.throwError(new FoException("Invalid arguments amount for method: " + this.str + " usage: "  + this.usage));
				return null;
			}

			if(function != null){
				return function.apply(p,w,args);

			}

			return null;
		}
		public void doTeleport(Player p, String locs){
			String[] parts = locs.split(", ");
			int x = Integer.parseInt(parts[0]);
			int y =Integer.parseInt(parts[1]);
			int z =Integer.parseInt(parts[2]);
			World w = Bukkit.getWorld(parts[3]);

			p.teleport(new Location(w,x,y,z));


		}

	}
	public static String join(String[] args){
		return join(args,args.length);
	}
	public static String join(String[] args, int amount){
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < amount; i++){

			String arg = args[i];
			if(!result.toString().isEmpty()){
				result.append(", ");
			}
			result.append(arg);
		}
		return result.toString();
	}


}
