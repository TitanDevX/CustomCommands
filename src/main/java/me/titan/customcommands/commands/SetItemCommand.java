package me.titan.customcommands.commands;

import me.titan.customcommands.common.ItemCommand;
import me.titan.customcommands.utils.Util;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SetItemCommand extends SimpleSubCommand {
	protected SetItemCommand() {
		super("makeClickable|addCommand");
		setMinArguments(1);
		setUsage("<Name> [command(put this inside ' and ' and split with ',')]");
		setDescription("Make the item in hand clickable. optionally adds commands will be performed when the item clicked.");
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

		if (args.length > 1) {
			String all = "";
			for (int i = 1; i < args.length; i++) {
				String del = all.equalsIgnoreCase("") ? "" : all + " ";
				all = del + args[i];
			}
			if (all.contains(",")) {

				String[] parts = all.split(",");
				for (String part : parts) {
					//int index1 = all.indexOf("'");
					//int index2 = all.lastIndexOf("'");
					String command = Util.getSubStringBetween(part, "'", "'");//all.substring(index1, index2);

					ic.getCommands().add(command);
				}

			} else {

				//int index1 = all.indexOf("'");
				//int index2 = all.lastIndexOf("'");
				String command = Util.getSubStringBetween(all, "'", "'");//all.substring(index1, index2);

				ic.getCommands().add(command);
			}
		}

		ic.save();

		tell("&aSuccessfully made the item &C" + ItemUtil.bountify(item.getType().toString()) + " &awith the name &c" + name + "&a.");


	}
}
