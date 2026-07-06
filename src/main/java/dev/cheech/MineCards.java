package dev.cheech;

import dev.cheech.commands.GivePack;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineCards extends JavaPlugin {

    static JavaPlugin instance;
    @Override
    public void onEnable() {
        // Plugin startup login
        new PackManager(getDataFolder());
        getServer().getPluginManager().registerEvents(new PackOpen(), this);
        getCommand("givepack").setExecutor(new GivePack());
        instance = this;
    }

    public static JavaPlugin getPluginObj(){
        return instance;
    }
}
