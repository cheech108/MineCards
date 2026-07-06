package dev.cheech;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class pack_open implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // 1. Prevent the event from firing twice (once for each hand)
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("test");
        // 2. Check if the action is a right-click
        if (event.getAction().isRightClick()) {
            Bukkit.getServer().getConsoleSender().sendMessage("rclick!");
        }
    }
}