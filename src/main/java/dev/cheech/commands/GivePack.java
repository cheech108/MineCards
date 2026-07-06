package dev.cheech.commands;

import dev.cheech.PackManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GivePack implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return false;
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
