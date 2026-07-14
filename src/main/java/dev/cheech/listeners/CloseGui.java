package dev.cheech.listeners;

import dev.cheech.binders.BinderInventoryHolder;
import dev.cheech.binders.BinderManager;
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
