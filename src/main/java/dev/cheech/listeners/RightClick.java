package dev.cheech.listeners;

import dev.cheech.binders.BinderManager;
import dev.cheech.MineCards;
import dev.cheech.cards.PackManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Objects;

public class RightClick implements Listener {

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
        NamespacedKey packKey = new NamespacedKey(MineCards.getPluginObj(), "pack_name");
        NamespacedKey binderKey = new NamespacedKey(MineCards.getPluginObj(), "binder_inv");
        assert item != null;
        if (!item.hasItemMeta()) return;
        if (item.getItemMeta().getPersistentDataContainer().has(packKey, PersistentDataType.STRING)) {
            // Get the data
            String value = item.getItemMeta().getPersistentDataContainer().get(packKey, PersistentDataType.STRING);
            if (PackManager.packNoExists(value)){
                event.getPlayer().sendMessage("Invalid Pack");
                return;
            }
            assert event.getItem() != null;
            int size = PackManager.getPackSize(value);
            event.getItem().subtract(1);
            event.getPlayer().playSound(Sound.sound(Key.key("item.hoe.till"), Sound.Source.PLAYER, 1f, 1f));
            for (int i = 0; i < size; i++) {
                HashMap<Integer, ItemStack> leftover =  event.getPlayer().getInventory().addItem(Objects.requireNonNull(PackManager.getDrop(value)));
                for (var entry : leftover.entrySet()) {
                    event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(),entry.getValue());
                }
            }
        }
        if (item.getItemMeta().getPersistentDataContainer().has(binderKey,PersistentDataType.STRING)){
            String value = item.getItemMeta().getPersistentDataContainer().get(binderKey, PersistentDataType.STRING);
            assert value != null;
            if (value.equals("empty")){
                assert event.getItem() != null;
                boolean init = BinderManager.initializeBinder(event.getItem());
                if (!init){
                    event.getPlayer().sendMessage("Error, initialize binders one at a time");
                }
                return;
            }
            event.getPlayer().openInventory(BinderManager.getBinder(value));

        }
    }
}