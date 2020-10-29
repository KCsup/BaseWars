package com.kcsup.basewars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {
    public HashMap<Location, Player> beaconLocation = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new ListenerClass(this), this);
    }

    @Override
    public void onDisable() {
        beaconLocation.clear();
    }
}
