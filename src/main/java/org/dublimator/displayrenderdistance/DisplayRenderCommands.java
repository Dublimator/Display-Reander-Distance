package org.dublimator.displayrenderdistance;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.Key;
import java.util.*;

public class DisplayRenderCommands implements CommandExecutor, TabExecutor {

    private final DisplayRenderDistance plugin;

    public DisplayRenderCommands(DisplayRenderDistance plugin) {
        this.plugin = plugin;

        plugin.getCommand("drdistance").setExecutor(this);

    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only player can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!(command.getName().equalsIgnoreCase(label))) {
            createMenu(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            player.sendMessage("§7[§6DRDistance§7]§e Config reloaded");
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("menu")) {
            createMenu(player);
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && command.getName().equalsIgnoreCase(label))
            return Arrays.asList("menu", "reload");

        return new ArrayList<>();
    }

    public void createMenu(Player player) {

        ConfigurationSection buttonsKeys = plugin.getConfig().getConfigurationSection("buttons");

        Inventory inventory = Bukkit.createInventory(player, 9, plugin.getConfig().getString("menu-name", "DRD Menu"));

        for (String buttonKey : buttonsKeys.getKeys(false)) {
            ConfigurationSection buttonMap = buttonsKeys.getConfigurationSection(buttonKey);

            ItemStack buttonItem = new ItemStack(Material.valueOf((String) buttonMap.get("item")));
            ItemMeta buttonMeta = buttonItem.getItemMeta();

            buttonMeta.setDisplayName((String) buttonMap.get("button-name"));
            buttonMeta.setLore((List<String>) buttonMap.get("button-lore"));
            if ((Integer) buttonMap.get("custom-model-data") >= 1) {
                buttonMeta.setCustomModelData((Integer) buttonMap.get("custom-model-data"));
            }
            buttonItem.setItemMeta(buttonMeta);
            inventory.setItem((Integer) buttonMap.get("slot"), buttonItem);
        }
        player.openInventory(inventory);
        player.setMetadata("OpenedMenu", new FixedMetadataValue(DisplayRenderDistance.getInstance(), "DistanceSettings"));
    }

}
