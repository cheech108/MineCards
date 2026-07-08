package dev.cheech;

import dev.cheech.commands.GivePack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MineCards extends JavaPlugin {

    static JavaPlugin instance;
    @Override
    public void onEnable() {
        // Plugin startup login
        PackManager.initialize(getDataFolder());
        getServer().getPluginManager().registerEvents(new PackOpen(), this);
        Objects.requireNonNull(getCommand("givepack")).setExecutor(new GivePack());
        instance = this;
    }

    public static JavaPlugin getPluginObj(){
        return instance;
    }
}
