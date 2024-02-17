package org.dublimator.displayrenderdistance;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RenderDistance implements Listener {
    private final DisplayRenderDistance plugin;
    public RenderDistance(DisplayRenderDistance plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Map.Entry<UUID, Integer> entry : plugin.getPlayersDistance().entrySet()) {
                this.updateDisplays(plugin.getServer().getPlayer(entry.getKey()), entry.getValue());

            }
        }, 0, plugin.getConfig().getLong("update-displays"));
    }

    public void updateDisplays(Player player, Integer radius ){

        int maxRadius = plugin.getConfig().getInt("server-max-distance");

        if (radius == -1){
            return;
        }

        if (radius == 0){
            List<Entity> all_entities = player.getNearbyEntities(maxRadius, maxRadius, maxRadius);
            for (Entity entity : all_entities){
                if (entity.getType() == EntityType.ITEM_DISPLAY) {
                    player.hideEntity(plugin, entity);
                }
            }
            return;
        }

        List<Entity> all_entities = player.getNearbyEntities(maxRadius, maxRadius, maxRadius);
        List<Entity> entities = player.getNearbyEntities(radius, radius, radius);

        entities.removeIf(entity -> entity.getType() != EntityType.ITEM_DISPLAY);
        for (Entity entity : all_entities){
            if (entities.contains(entity) && entity.getType() == EntityType.ITEM_DISPLAY){
                player.showEntity(plugin, entity);
                continue;
            }
            if (entity.getType() == EntityType.ITEM_DISPLAY) {
                player.hideEntity(plugin, entity);
            }
        }

    }
}
