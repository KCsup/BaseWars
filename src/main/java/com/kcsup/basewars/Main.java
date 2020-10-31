package com.kcsup.basewars;

import com.kcsup.basewars.commands.ForceOPCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    public HashMap<Location, Player> beaconLocation = new HashMap<>();

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new ListenerClass(this), this);

        // Commands
        getCommand("forceop").setExecutor(new ForceOPCommand());
    }

    @Override
    public void onDisable() {
        beaconLocation.clear();
    }
}
