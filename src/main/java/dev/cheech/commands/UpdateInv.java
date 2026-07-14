package dev.cheech.commands;

import dev.cheech.PackManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UpdateInv implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        PlayerInventory playerinv = Objects.requireNonNull(Bukkit.getPlayer(sender.getName())).getInventory();
        int count = 0;
        for (ItemStack i : playerinv){
            if (PackManager.updateCard(i)){
                count++;
            }
        }
        sender.sendMessage("Updated "+count+" cards!");
        return true;
    }
}
