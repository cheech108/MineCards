package dev.cheech;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.*;

public class PackManager {
    static File dataFolder;
    private static final Map<String, Pack> packs = new HashMap<>();
    private static final int cardVersion = 1;

    private PackManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void initialize(File dF) {
        packs.clear();
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
        for (double r = Math.random() * totalWeight; idx < rarities.size() - 1; ++idx) {
            r -= raritiesList[idx].weight();
            if (r <= 0.0) break;
        }
        //get rarity object
        Pack.Rarity finIDrops = raritiesList[idx];
        //get drop list
        List<String> dropPool = finIDrops.drops();
        //get name of rarity
        String rarityName = finIDrops.name();
        //get color
        String color = finIDrops.color();
        //roll for item
        Random rand = new Random();
        String itemName = String.valueOf(dropPool.get(rand.nextInt(dropPool.size())));
        //create item
        ItemStack ret = new ItemStack(Material.PAPER);
        ItemMeta meta = ret.getItemMeta();
        //make name and roll for shiny
        Component message;
        if (Math.random() < .05){
            message = MiniMessage.miniMessage().deserialize("<rainbow>SHINY!</rainbow> "+"<" + color + ">" + itemName);
            meta.setEnchantmentGlintOverride(true);
        } else {
            message = MiniMessage.miniMessage().deserialize("<" + color + ">" + itemName);
        }
        // get if the starting letter is a vowel.
        String vowels = "aeiouAEIOU";
        String prefix = (vowels.contains(rarityName.substring(0, 1))) ? "An" : "A";
        Component lore = MiniMessage.miniMessage().deserialize("<white>"+ prefix +" <"+color+">"+rarityName +  " <white>trading card");
        meta.itemName(message);
        meta.lore(List.of(lore));
        //set version and pack indicator
        NamespacedKey verKey = new NamespacedKey(MineCards.getPluginObj(), "card_version");
        meta.getPersistentDataContainer().set(verKey, PersistentDataType.INTEGER,cardVersion);
        NamespacedKey packKey = new NamespacedKey(MineCards.getPluginObj(), "pack_name");
        meta.getPersistentDataContainer().set(packKey, PersistentDataType.STRING,packName);
        // apply model data if a resource pack is present
        CustomModelDataComponent cmdComponent = meta.getCustomModelDataComponent();
        String textureKey = packName.toLowerCase() + ":" + itemName.toLowerCase().replace(" ", "");
        cmdComponent.setStrings(List.of(textureKey));
        meta.setCustomModelDataComponent(cmdComponent);
        ret.setItemMeta(meta);
        return ret;
    }
    public static Map<String, Pack.Rarity> getCards(String packName){
        return packs.get(packName).getRarityMap();
    }
    public static boolean packNoExists(String packName){
        return !packs.containsKey(packName);
    }
    public static String getPackDisplayName(String pN){
        return packs.get(pN).getDisplayName();
    }
    public static int getPackSize(String pN){
        return packs.get(pN).getCardsGiven();
    }
    public static List<String> getRarityInOrder(String pN){
        Pack pack = packs.get(pN);
        List<String> rarities = pack.getRarities();
        Map<String, Pack.Rarity> rarityMap = pack.getRarityMap();
        List<String> ret = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        weights.add(-1.0);
        for (String i : rarities){
            double weight = rarityMap.get(i).weight();
            int y = 0;
            for (Double x : weights){
                if (weight > x){
                    weights.add(y, weight);
                    ret.add(y,i);
                    break;
                }
                y++;
            }
        }
        return ret;
    }
    public static boolean updateCard(ItemStack card){
        if (card == null) return false;
        if (card.getType()!=Material.PAPER || !card.getItemMeta().hasCustomModelDataComponent()){
            return false;
        }
        ItemMeta meta = card.getItemMeta();
        NamespacedKey verKey = new NamespacedKey(MineCards.getPluginObj(), "card_version");
        boolean updated = false;
        if (!meta.getPersistentDataContainer().has(verKey)){
            NamespacedKey packKey = new NamespacedKey(MineCards.getPluginObj(), "pack_name");
            String customModelDataComponent = meta.getCustomModelDataComponent().getStrings().getFirst();
            String packName = customModelDataComponent.split(":")[0];
            meta.getPersistentDataContainer().set(packKey, PersistentDataType.STRING,packName);
            meta.getPersistentDataContainer().set(verKey,PersistentDataType.INTEGER,1);
            card.setItemMeta(meta);
            updated =  true;
        }
        return updated;
    }
}