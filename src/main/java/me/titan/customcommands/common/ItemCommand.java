package me.titan.customcommands.common;

import lombok.Getter;
import lombok.Setter;
import me.titan.customcommands.utils.EnchantmentUtils;
import me.titan.customcommands.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.SimpleEnchant;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ItemCommand {

	public static Map<String, ItemCommand> items = new HashMap<>();
	final String name;

	//-------------
	// ITEM
	ItemStack itemStack;
	List<String> commands = new ArrayList<>();
	List<String> replyMessages = new ArrayList<>();


	public ItemCommand(String name) {
		this.name = name;

		items.put(name, this);
	}

	//	public ItemStack getItemStack(){
//		if(type == null) {
//			System.out.print("TYPE NULL");
//			return null;
//		}
//		ItemCreator.ItemCreatorBuilder ic = ItemCreator.of(type);
//		if(!displayName.isEmpty()){
//			ic.name(displayName);
//		}
//		if(!lore.isEmpty()){
//			ic.lores(lore);
//		}
//		if(!enchants.isEmpty()){
//			ic.enchants(enchants);
//		}
//		if(data > 0){
//			ic.damage(data);
//		}
//		return ic.build().make();
//	}
	public static ItemCommand getByItem(ItemStack clicked) {
		for (ItemCommand item : items.values()) {
			ItemStack items = item.getItemStack();
			if (ItemUtil.isSimilar(items, clicked)) return item;
		}
		return null;
	}

	public static ItemCommand fromSection(String name, ConfigurationSection mainSection) {

		ItemCommand cmd = new ItemCommand(name);

		ConfigurationSection s = mainSection.getConfigurationSection("Item");

		String displayName = s.getString("Display_Name");
		CompMaterial type = CompMaterial.fromString(s.getString("Type"));
		int data = s.getInt("Data");
		List<String> strs = s.getStringList("Enchantments");
		List<SimpleEnchant> enchantments = new ArrayList<>();

		// PROTECTION-1
		for (String enchs : strs) {
			enchs = enchs.replace(" ", "");
			String[] parts = enchs.split("-");
			Enchantment en = EnchantmentUtils.getByName(parts[0]);

			int level = Integer.parseInt(parts[1]);


			enchantments.add(new SimpleEnchant(en, level));
		}

		boolean glow = s.getBoolean("glow");
		List<String> lore = s.getStringList("Lore");

		if (type == null) {
			return null;
		}
		ItemCreator.ItemCreatorBuilder ic = ItemCreator.of(type);

		if (displayName != null && !displayName.isEmpty()) {
			ic.name(displayName);
		}
		if (!lore.isEmpty()) {
			ic.lores(lore);
		}
		if (!enchantments.isEmpty()) {
			ic.enchants(enchantments);
		}
		if (data > 0) {
			ic.damage(data);
		}
		cmd.itemStack = ic.build().make();
		cmd.commands = mainSection.getStringList("On_Click_Commands");
		cmd.replyMessages = mainSection.getStringList("Reply_Messages");

		return cmd;
	}

	public void fromItem(ItemStack item) {
		this.itemStack = item;

	}

	public void onClick(Player p) {


		for (String cmd : this.commands) {
			if (cmd.startsWith("/")) {
				p.performCommand(cmd.replace("/", ""));

			} else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
		}
		for (String msg : replyMessages) {
			Common.tell(p, msg);
		}

	}

	public void save() {
		File f = FileUtil.getFile("items.yml");
		YamlConfiguration yc = FileUtil.loadConfigurationStrict(f);

		if (getItemStack() == null) return;
		yc.set("Items." + name + ".Item", Util.serializeItemStack(getItemStack()).serialize());
		yc.set("Items." + name + ".On_Click_Commands", this.getCommands());
		yc.set("Items." + name + ".Reply_Messages", this.getReplyMessages());


		try {
			yc.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
