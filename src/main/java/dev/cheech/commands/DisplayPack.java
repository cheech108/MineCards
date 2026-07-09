package dev.cheech.commands;

import dev.cheech.Pack;
import dev.cheech.PackManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayPack implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (PackManager.packNoExists(args[0])){
            sender.sendMessage("Invalid pack!");
            return false;
        }
        Map<String, Pack.Rarity> cards = PackManager.getCards(args[0]);
        List<String> rarities = PackManager.getRarityInOrder(args[0]);
        double totalWeight = 0;
        for (var entry : cards.entrySet()){
            totalWeight += entry.getValue().weight();
        }
        String message = "<underlined>Cards in " + PackManager.getPackDisplayName(args[0]) + ":</underlined>";
        for (String i : rarities) {
            double odds = (cards.get(i).weight() / totalWeight)*100;
            String rounded = String.format("%.2f", odds);
            message += "\n<"+cards.get(i).color()+">" + "<underlined>" + i + "("+ rounded +"%):</underlined>";
            for (String y : cards.get(i).drops()){
                message += "\n" + y;
            }
        }
        sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
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
