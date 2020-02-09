package me.titan.customcommands.commands;

import me.titan.customcommands.common.ItemCommand;
import org.mineacademy.fo.command.SimpleSubCommand;

public class GiveItemCommand extends SimpleSubCommand {
	protected GiveItemCommand() {
		super("giveCommandItem");
		setMinArguments(1);
		setUsage("<Name>");
		setDescription("Gives you this clickable item.");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		String name = args[0];


		if (!ItemCommand.items.containsKey(name)) {

			returnTell("&cThis name does not exists.");
		}


		ItemCommand ic = ItemCommand.items.get(name);
		getPlayer().getInventory().addItem(ic.getItemStack());

		//tell("&aSuccessfully made the item &C" + ItemUtil.bountify(item.getType().toString()) + " &awith the name &c" + name + "&a.");


	}
}
