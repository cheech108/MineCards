package dev.cheech;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Base64;
import java.util.UUID;

public class BinderManager {

    public static boolean initializeBinder(ItemStack item){
        Inventory inventory = Bukkit.createInventory(null,54);

        String base64Inventory = Base64.getEncoder().encodeToString(ItemStack.serializeItemsAsBytes(inventory.getContents()));

        // Save to PersistentDataContainer
        ItemMeta meta = item.getItemMeta();
        if (item.getAmount() > 1){
            return false;
        }
        if (meta != null) {
            NamespacedKey uuid = new NamespacedKey(MineCards.getPluginObj(), "unique_id");
            meta.getPersistentDataContainer().set(uuid, PersistentDataType.STRING, UUID.randomUUID().toString());
            NamespacedKey key = new NamespacedKey(MineCards.getPluginObj(), "binder_inv");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, base64Inventory);
            item.setItemMeta(meta);
        }
        return true;
    }
    public static void SaveBinder(ItemStack item, Inventory inventory) {
        String base64Inventory = Base64.getEncoder().encodeToString(ItemStack.serializeItemsAsBytes(inventory.getContents()));

        // Save to PersistentDataContainer
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(MineCards.getPluginObj(), "binder_inv");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, base64Inventory);
        item.setItemMeta(meta);
    }
    public static Inventory getBinder(String encodedInventory) {

        byte[] byteInv = Base64.getDecoder().decode(encodedInventory);
        ItemStack[] itemstacks = ItemStack.deserializeItemsFromBytes(byteInv);
        Inventory inventory = Bukkit.createInventory(new BinderInventoryHolder(), 54, MiniMessage.miniMessage().deserialize("Card Binder"));
        inventory.setContents(itemstacks);
        return inventory;
    }
}