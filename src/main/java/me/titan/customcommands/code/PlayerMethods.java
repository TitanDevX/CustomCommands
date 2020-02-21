package me.titan.customcommands.code;

import PacketWrapper.src.main.java.com.comphenix.packetwrapper.WrapperPlayServerGameStateChange;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

public enum PlayerMethods {


	TELEPORT("teleport", 4, (p, o, args) -> {
		p.teleport(Methods.getLocation(Methods.join(args)));
		return null;
	}, "teleport(x, y, z, world)", Location.class),
	SET_HEALTH("setHealth", 1, (p, o, args) -> {
		p.setHealth(Double.parseDouble(args[0]));
		return null;
	}, "setHealth(newHealth)", Double.class),
	GET_HEALTH("getHealth", 0, (p, o, args) -> {
		return p.getHealth();
	}, "getHealth"),
	RESET_HEALTH("resetHealth", 0, (p, o, args) -> {
		p.resetMaxHealth();
		return null;
	}, "resetHealth()"),
	SET_SPAWN_LOCATION("setSpawnLocation", 4, (p, o, args) -> {
		p.setBedSpawnLocation(Methods.getLocation(Methods.join(args)));
		return null;
	}, "setSpawnLocation(x, y, z, world)"),
	SET_GAMEMODE("setGamemode", 1, (p, o, args) -> {
		GameMode gm = ReflectionUtil.getEnum(args[0].toUpperCase(), args[0].toUpperCase(), GameMode.class);
		p.setGameMode(gm);
		return null;
	}, "setGamemode(gameMode)", GameMode.class),
	SET_ALLOW_FLY("setAllowFly", 1, (p, o, args) -> {
		p.setAllowFlight(Boolean.parseBoolean(args[0]));
		return null;
	}, "setAllowFly(true/false)", Boolean.class),
	SET_FLYING("setFlying", 1, (p, o, args) -> {
		p.setFlying(Boolean.parseBoolean(args[0]));
		return null;
	}, "setFlying(true/false)", Boolean.class),
	SET_FLY_SPEED("setFlySpeed", 1, (p, o, args) -> {
		p.setFlySpeed(Float.parseFloat(args[0]));
		return null;
	}, "setFlySpeed(FlySpeed)", Float.class),
	SET_WALK_SPEED("setWalkSpeed", 1, (p, o, args) -> {
		p.setWalkSpeed(Float.parseFloat(args[0]));
		return null;
	}, "setWalkSpeed(WalkSpeed)", Float.class),
	GET_WALK_SPEED("getWalkSpeed", 0, (p, o, args) -> {
		return p.getWalkSpeed();
	}, "getWalkSpeed"),
	GET_Fly_SPEED("getFlySpeed", 0, (p, o, args) -> {
		return p.getFlySpeed();
	}, "getFlySpeed"),
	SET_EXP("setExp", 1, (p, o, args) -> {
		p.setExp(Float.parseFloat(args[0]));
		return null;
	}, "setExp(Exp)", Float.class),
	SET_EXP_LEVEL("setExpLevel", 1, (p, o, args) -> {
		p.setLevel(Integer.parseInt(args[0]));
		return null;
	}, "setExpLevel(Exp)", Float.class),
	HIDE_PLAYER("hidePlayer", 1, (p, o, args) -> {
		Player t = Bukkit.getPlayer(args[0]);
		if (t == null) {

			Util.conoleError("Problem while invoking hidePlayer() -> Cannot find such a player!");
			return null;
		}
		p.hidePlayer(SimplePlugin.getInstance(), t);
		return null;
	}, "hidePlayer(Player) -> hides this player from another player, in tablist and physically. ", Float.class),
	SET_SPRINITING("setSprinting", 1, (p, o, args) -> {
		p.setSprinting(Boolean.parseBoolean(args[0]));

		return null;
	}, "setSprinting(true/false)", Boolean.class),
	SEND_TITLE("sendTitle", 2, (p, o, args) -> {

		Remain.sendTitle(p, args[0], args[1]);
		return null;
	}, "sendTitle(Title, subtitle)", Boolean.class),
	SEND_ACTION_BAR("sendActionBarMessage", 1, (p, o, args) -> {

		Remain.sendActionBar(p, args[0]);
		return null;
	}, "sendActionBarMessage(actionBar)", Boolean.class),
	SEND_TOAST("sendToast", 2, (p, o, args) -> {


		CompMaterial icon;
		if (args.length >= 2) {

			icon = CompMaterial.fromString(args[1].replace(" ", ""));
			if (icon != null) {

				Remain.sendToast(p, args[0], icon);
				return null;
			}
		}
		Remain.sendToast(p, args[0]);


		return null;
	}, "sendToast(toast, <optional> icon (Material))", Boolean.class),
	SET_ITEM_COOLDOWN("setItemCooldown", 2, (p, o, args) -> {


		CompMaterial mat = CompMaterial.fromString(args[0]);
		int sec = Integer.parseInt(args[1]) * 20;

		Remain.setCooldown(p, mat.getMaterial(), sec);


		return null;
	}, "setItemCooldown(Item, seconds)", Boolean.class),
	RESPAWN("respawn", 0, (p, o, args) -> {


		p.setHealth(0);
		Remain.respawn(p, 5);
		return null;
	}, "respawn()", Boolean.class),
	PLAY_SOUND("playSound", 1, (p, o, args) -> {
		ReflectionUtil.getEnum(args[0], args[0].toUpperCase(), CompSound.class).play(p);
		return null;
	}, "playSound(Sound)", String.class),
	CHANGE_GAME_STATE("setGameState", 2, (p, o, args) -> {

		WrapperPlayServerGameStateChange pack = new WrapperPlayServerGameStateChange();


		int ubyte = Integer.parseInt(args[0]);
		float f = Float.parseFloat(args[1]);

		pack.setReason(ubyte);
		pack.setValue(f);

		pack.sendPacket(p);


		return null;
	}, "setGameState(reason, value) -> (Deprecated) see resource page for more info.", String.class);

	final String str;
	final int argsAmount;

	final String usage;
	final Class<?>[] argsClasses;

	CodeMethod<Player> function;

	PlayerMethods(String str, int argsAmount, String usage, Class<?>... argsClasses) {

		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.argsClasses = argsClasses;

	}

	PlayerMethods(String str, int argsAmount, CodeMethod<Player> f, String usage, Class<?>... argsClasses) {
		this.str = str;
		this.usage = usage;
		this.argsAmount = argsAmount;
		this.argsClasses = argsClasses;
		this.function = f;
	}


	public static Object getInvoke(String m, Player p, String... args) {
		return get(m).invoke(p, args);
	}

	public static PlayerMethods get(String m) {
		for (PlayerMethods pm : values()) {
			if (m.equalsIgnoreCase(pm.str)) return pm;
		}
		return null;
	}

	public Object invoke(Player p, String... args) {


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
