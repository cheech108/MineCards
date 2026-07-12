package dev.cheech;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;


public class CloseGui implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){

        if (event.getInventory().getHolder() instanceof BinderInventoryHolder){
            BinderManager.SaveBinder(event.getPlayer().getInventory().getItemInMainHand(),event.getInventory());
        }
    }
}
