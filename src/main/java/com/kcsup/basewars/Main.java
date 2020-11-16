package com.kcsup.basewars;

import com.kcsup.basewars.commands.FallChestcommand;
import com.kcsup.basewars.commands.ForceOPCommand;
import com.kcsup.basewars.commands.PvpToggleCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin {

    public HashMap<Location, Player> beaconLocation = new HashMap<>();
    public HashMap<Double, Double> beaconXZ = new HashMap<>();
    public List<Double> blockY = new ArrayList<>();
    public boolean pvpEnabled;

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new ListenerClass(this), this);

        loadCommands();

        pvpEnabled = getConfig().getBoolean("pvpEnabledOnStart");
    }

    private void loadCommands() {
        getCommand("forceop").setExecutor(new ForceOPCommand());
        getCommand("fallchest").setExecutor(new FallChestcommand());
        getCommand("pvp-toggle").setExecutor(new PvpToggleCommand(this));
    }

    @Override
    public void onDisable() {
        beaconLocation.clear();
        beaconXZ.clear();
        blockY.clear();

        pvpEnabled = false;
    }
}
