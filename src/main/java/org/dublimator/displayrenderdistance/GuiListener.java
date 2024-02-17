package org.dublimator.displayrenderdistance;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.sql.SQLException;
import java.util.*;

public class GuiListener implements Listener {
    private final DisplayRenderDistance plugin;

    public GuiListener(DisplayRenderDistance plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) throws SQLException {
        Player player = (Player) event.getWhoClicked();

        if (player.hasMetadata("OpenedMenu")) {
            event.setCancelled(true);
            Map<UUID, Integer> playersDistance = plugin.getPlayersDistance();
            ConfigurationSection buttonsKeys = plugin.getConfig().getConfigurationSection("buttons");

            for (String buttonKey : buttonsKeys.getKeys(false)) {
                ConfigurationSection buttonMap = buttonsKeys.getConfigurationSection(buttonKey);

                if (buttonMap.getInt("slot") == event.getSlot()) {

                    playersDistance.put(player.getUniqueId(), buttonMap.getInt("distance"));
                    if (!plugin.getTempPlayersUuid().contains(player.getUniqueId()))
                        plugin.getTempPlayersUuid().add(player.getUniqueId());

                    updateEntity(player, plugin.getConfig().getInt("server-max-distance"));

                    player.closeInventory();
                    playButtonSound(player);

                }
            }
        }
    }

    private void updateEntity(Player player, Integer maxRadius) {
        List<Entity> all_entities = player.getNearbyEntities(maxRadius, maxRadius, maxRadius);
        for (Entity entity : all_entities) {
            if (entity.getType() == EntityType.ITEM_DISPLAY) {
                player.showEntity(plugin, entity);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasMetadata("OpenedMenu"))
            player.removeMetadata("OpenedMenu", DisplayRenderDistance.getInstance());
    }


    private void playButtonSound(Player player) {
        if (!Objects.equals(plugin.getConfig().getString("button-sound"), "Null")) {
            player.playSound(player, Sound.valueOf(plugin.getConfig().getString("button-sound")), 0.5F, 1);
        }
    }

}
