package dev.cheech.commands;

import dev.cheech.Pack;
import dev.cheech.PackManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class DisplayPack implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!PackManager.packExists(args[0])){
            sender.sendMessage("Invalid pack!");
            return false;
        }
        Inventory chestDisplay = Bukkit.createInventory(null, 27);
        Map<String, Pack.Rarity> cards = PackManager.getCards(args[0]);
        AtomicInteger idx = new AtomicInteger();
        cards.forEach((key,value)->{
            for (String i : value.drops()) {
                ItemStack card = new ItemStack(Material.PAPER, 1);
                ItemMeta meta = card.getItemMeta();
                if (meta != null) {
                    meta.itemName(MiniMessage.miniMessage().deserialize("<"+value.color()+">"+i));
                    Component lore = MiniMessage.miniMessage().deserialize("<white>A "+"<"+value.color()+">"+key +  " <white>trading card");
                    meta.lore(List.of(lore));
                    card.setItemMeta(meta);
                    card.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                }
                chestDisplay.setItem(idx.get(), card);
                idx.getAndIncrement();
            }
        });

        Objects.requireNonNull(Bukkit.getPlayer(sender.getName())).openInventory(chestDisplay);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        final List<String> validArgs = new ArrayList<>();
        if (args.length == 1){
            StringUtil.copyPartialMatches(args[0], PackManager.getPacks(), validArgs);
            return validArgs;
        }
        return List.of();
    }
}
