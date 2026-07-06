package dev.cheech.commands;

import dev.cheech.MineCards;
import dev.cheech.PackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GivePack implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        // 1. Get Meta and Define Key
        ItemMeta meta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(MineCards.getPluginObj(), "pack_name");

        // 2. Set Data (e.g., String, Integer, Byte)
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, args[1]);

        // 3. Apply and Give
        itemStack.setItemMeta(meta);
        Bukkit.getPlayer(args[0]).getInventory().addItem(itemStack);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        final List<String> validArgs = new ArrayList<>();
        if (args.length == 1){
            List<String> playerNames = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                //check if player is a player, if they are not we don't need to restrict the list.
                if (sender instanceof Player) {
                    Player sendingPlayer = (Player) sender;
                    if (sendingPlayer.canSee(player)) {
                        playerNames.add(player.getName());
                    }
                } else {
                    playerNames.add(player.getName());
                }
            }

            List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], playerNames, completions);
            return completions;
        }
        if (args.length == 2){
            StringUtil.copyPartialMatches(args[1], PackManager.getPacks(), validArgs);
            return validArgs;
        }
        return List.of();
    }
}
