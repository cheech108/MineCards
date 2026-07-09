package dev.cheech.commands;

import dev.cheech.PackManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static dev.cheech.MineCards.getPluginObj;

public class ReloadPack implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        PackManager.initialize(getPluginObj().getDataFolder());
        return true;
    }
}
