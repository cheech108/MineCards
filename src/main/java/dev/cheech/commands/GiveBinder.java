package dev.cheech.commands;

import dev.cheech.MineCards;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
import java.util.Objects;

public class GiveBinder implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        // 1. Get Meta and Define Key
        ItemMeta meta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(MineCards.getPluginObj(), "binder_inv");
        // 2. Set Data
        meta.itemName(MiniMessage.miniMessage().deserialize("Card Binder"));
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING,"empty");
        itemStack.setItemMeta(meta);
        Objects.requireNonNull(Bukkit.getPlayer(args[0])).getInventory().addItem(itemStack);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1){
            List<String> playerNames = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                //check if player is a player, if they are not we don't need to restrict the list.
                if (sender instanceof Player sendingPlayer) {
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
        return List.of();
    }
}
