package me.titan.customcommands.code.cache;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.titan.customcommands.code.CodeMethod;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public class PremadeFunction<O> {

	final String[] args;
	final O object;
	final CodeMethod<O> method;
	final String propose;

	// player.setHealth(health) -> Methods.getPlayer("Name").setHealth(0.6)
	public Object apply(Player p) {
		System.out.print("OOF");
		System.out.print(Arrays.toString(args) + " " + object);
		return method.apply(p, object, args);
	}

}
