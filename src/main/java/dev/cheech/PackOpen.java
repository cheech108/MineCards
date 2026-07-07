package dev.cheech;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PackOpen implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // 1. Prevent the event from firing twice (once for each hand)
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        // 2. Check if the action is a right-click
        if (!event.getAction().isRightClick() || !event.hasItem()) {
            return;
        }
        ItemStack item = event.getItem();
        NamespacedKey key = new NamespacedKey(MineCards.getPluginObj(), "pack_name");
        if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            // Get the data
            String value = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
            ItemStack drop = PackManager.getDrop(value);
            if (drop == null){
                event.getPlayer().sendMessage("Invalid Pack");
                return;
            }
            event.getItem().subtract(1);
            event.getPlayer().getInventory().addItem(PackManager.getDrop(value));
            event.getPlayer().getInventory().addItem(PackManager.getDrop(value));
            event.getPlayer().getInventory().addItem(PackManager.getDrop(value));
        }
    }
}