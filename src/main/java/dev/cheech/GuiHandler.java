package dev.cheech;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;

import java.util.Objects;

public class GuiHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Get the view of the opened inventory
        InventoryView view = event.getView();

        // Check if the title matches your custom chest display title
        if (Objects.requireNonNull(view.getTopInventory().getItem(0)).hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)){
            // Cancel the event so the item cannot be moved, taken, or swapped
            event.setCancelled(true);
        }
    }
}
