package dev.cheech;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.*;

public class PackManager {
    static File dataFolder;
    private static final Map<String, Pack> packs = new HashMap<>();

    private PackManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void initialize(File dF) {
        dataFolder = dF;
        if (!dataFolder.exists()) {boolean created = dataFolder.mkdirs();if (!created){return;}}
        File[] files = dataFolder.listFiles();
        assert files != null;
        for (File curFile : files) {
            packs.put(curFile.getName().substring(0,curFile.getName().length()-5),new Pack(curFile));
        }
    }

    public static List<String> getPacks() {
        List<String> ret = new ArrayList<>();
        packs.forEach((_, value)-> ret.add(value.getName()));
        return ret;
    }

    public static ItemStack getDrop(String packName) {
        Pack pack = packs.get(packName);
        if (pack == null) return null;
        List<String> rarities = pack.getRarities();
        double totalWeight = 0.0;
        Pack.Rarity[] raritiesList = new Pack.Rarity[pack.getRarities().size()];
        int x = 0;
        for (String i : rarities) {
            Pack.Rarity rarity = pack.getRarity(i);
            totalWeight += rarity.weight();
            raritiesList[x] = rarity;
            x++;
        }
        int idx = 0;
        String rarityName = null;
        for (double r = Math.random() * totalWeight; idx < rarities.size() - 1; ++idx) {
            r -= raritiesList[idx].weight();
            rarityName = raritiesList[idx].name();
            if (r <= 0.0) break;
        }
        //get rarity object
        Pack.Rarity finIDrops = raritiesList[idx];
        //get drop list
        List<String> dropPool = finIDrops.drops();
        //get color
        String color = finIDrops.color();
        //roll for item
        Random rand = new Random();
        String itemName = String.valueOf(dropPool.get(rand.nextInt(dropPool.size())));
        //create item
        ItemStack ret = new ItemStack(Material.PAPER);
        ItemMeta meta = ret.getItemMeta();
        NamespacedKey key = new NamespacedKey(MineCards.getPluginObj(), "id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, pack.getDisplayName()+":"+itemName);
        //make name and roll for shiny
        Component message;
        if (Math.random() < .05){
            message = MiniMessage.miniMessage().deserialize("<rainbow>SHINY!</rainbow> "+"<" + color + ">" + itemName);
            meta.setEnchantmentGlintOverride(true);
        } else {
            message = MiniMessage.miniMessage().deserialize("<" + color + ">" + itemName);
        }
        Component lore = MiniMessage.miniMessage().deserialize("<white>A "+"<"+color+">"+rarityName +  " <white>trading card");
        meta.itemName(message);
        meta.lore(List.of(lore));
        ret.setItemMeta(meta);
        return ret;
    }
    public static Map<String, Pack.Rarity> getCards(String packName){
        return packs.get(packName).getRarityMap();
    }
    public static boolean packExists(String packName){
        return packs.containsKey(packName);
    }
}