package dev.cheech.binders;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NonNull;

public class BinderInventoryHolder implements InventoryHolder {

    @Override
    public @NonNull Inventory getInventory() {
        return Bukkit.createInventory(null,9); // required by bukkit
    }
}
