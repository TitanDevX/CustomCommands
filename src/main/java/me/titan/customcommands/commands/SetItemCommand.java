package me.titan.customcommands.commands;

import me.titan.customcommands.common.ItemCommand;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SetItemCommand extends SimpleSubCommand {
	protected SetItemCommand() {
		super("setCommand");
		setMinArguments(1);
		setUsage("<Name>");
		setDescription("Make the item in hand clickable.");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		String name = args[0];


		if (ItemCommand.items.containsKey(name)) {

			returnTell("&cThis name does already exists.");
		}

		ItemStack item = getPlayer().getItemInHand();
		ItemCommand ic = new ItemCommand(name);
		ic.fromItem(item);
		ic.save();

		tell("&aSuccessfully made the item &C" + ItemUtil.bountify(item.getType().toString()) + " &awith the name &c" + name + "&a.");


	}
}
