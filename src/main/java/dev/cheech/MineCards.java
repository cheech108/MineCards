package dev.cheech;

import org.bukkit.plugin.java.JavaPlugin;

public final class MineCards extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic. This is when frank goes sleepy go night night.
    }
}
