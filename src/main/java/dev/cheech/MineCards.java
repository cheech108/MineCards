package dev.cheech;

import dev.cheech.commands.DisplayPack;
import dev.cheech.commands.GivePack;
import dev.cheech.commands.ReloadPack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MineCards extends JavaPlugin {

    static JavaPlugin instance;
    @Override
    public void onEnable() {
        // Plugin startup login
        PackManager.initialize(getDataFolder());
        getServer().getPluginManager().registerEvents(new PackOpen(), this);
        getServer().getPluginManager().registerEvents(new GuiHandler(), this);
        Objects.requireNonNull(getCommand("givepack")).setExecutor(new GivePack());
        Objects.requireNonNull(getCommand("displaypack")).setExecutor(new DisplayPack());
        Objects.requireNonNull(getCommand("reloadpacks")).setExecutor(new ReloadPack());
        instance = this;
    }

    public static JavaPlugin getPluginObj(){
        return instance;
    }
}
