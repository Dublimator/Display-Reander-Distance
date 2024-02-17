package org.dublimator.displayrenderdistance;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

public final class DisplayRenderDistance extends JavaPlugin implements Listener {

    @Getter
    private Database db;

    @Getter
    private HashMap<UUID, Integer> playersDistance;

    private final HashMap<UUID, PlayerDistanceData> tempPlayersDistance = new HashMap<>();

    @Getter
    private final List<UUID> tempPlayersUuid = new ArrayList<>();


    @Override
    public void onEnable() {
        this.db = new Database(this);
        this.db.setup();
        this.playersDistance = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        new DisplayRenderCommands(this);
        new GuiListener(this);
        new RenderDistance(this);
        getServer().getScheduler().runTaskTimer(this, this::cleanUp, 1200, 1200);

        getServer().getScheduler().runTaskTimer(this, () -> {
            if (!tempPlayersUuid.isEmpty()) {
                tempPlayersUuid.forEach(uuid -> {
                    int distance = playersDistance.get(uuid);
                    getServer().getScheduler().runTaskAsynchronously(this, () -> {
                        try {
                            db.updatePlayerDistance(uuid, distance);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                });
                tempPlayersUuid.clear();

            }

        }, 1200, 1200);


        try {
            db.initializeDatabase();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onDisable() {
        try {
            if (db != null) {
                db.shutdown();
                db = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (this.playersDistance != null) {
            this.playersDistance.clear();
            this.playersDistance = null;
        }
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) throws SQLException {
        UUID uuid = event.getUniqueId();
        synchronized (tempPlayersDistance) {
            tempPlayersDistance.put(uuid, new PlayerDistanceData(db.getDistance(uuid), System.currentTimeMillis()));
        }


    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int distance;
        synchronized (tempPlayersDistance) {
            distance = tempPlayersDistance.get(event.getPlayer().getUniqueId()).distance;
        }
        playersDistance.put(event.getPlayer().getUniqueId(), distance);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws SQLException {
        UUID uuid = event.getPlayer().getUniqueId();

        int distance = playersDistance.getOrDefault(uuid, -1);

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                db.updatePlayerDistance(uuid, distance);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        tempPlayersUuid.remove(uuid);
        playersDistance.remove(uuid);

    }

    public static DisplayRenderDistance getInstance() {
        return getPlugin(DisplayRenderDistance.class);
    }

    public void reload() {
        Bukkit.getPluginManager().registerEvents(this, this);
        reloadConfig();
        new DisplayRenderCommands(this);
        new GuiListener(this);
        new RenderDistance(this);

    }

    private void cleanUp() {
        List<UUID> remove = new ArrayList<>();
        long data = System.currentTimeMillis();
        synchronized (tempPlayersDistance) {
            tempPlayersDistance.forEach((uuid, playerDistanceData) -> {
                if (data - playerDistanceData.date < 30000) return;
                remove.add(uuid);

            });
            remove.forEach(tempPlayersDistance::remove);
        }
    }

    private record PlayerDistanceData(int distance, long date) {
    }
}
