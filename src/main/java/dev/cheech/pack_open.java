package com.example.myplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class pack_open.java implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // 1. Prevent the event from firing twice (once for each hand)
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        // 2. Check if the action is a right-click
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            // Your logic goes here
            player.sendMessage("You right-clicked!");
        }
    }
}