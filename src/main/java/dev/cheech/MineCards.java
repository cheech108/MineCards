package dev.cheech;

import org.bukkit.plugin.java.JavaPlugin;

public final class MineCards extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PackOpen(), this);
    }
}
