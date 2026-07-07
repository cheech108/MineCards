package dev.cheech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.*;

import static org.bukkit.Registry.MATERIAL;

public class PackManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static File dataFolder;

    public PackManager(File dF) {
        dataFolder = dF;
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public static List<String> getPacks() {
        File[] files = dataFolder.listFiles();
        if (files == null) return List.of();
        List<String> retList = new ArrayList<>();
        for (File curFile : files) {
            try (FileReader reader = new FileReader(curFile)) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                retList.add(jsonObject.get("Name").getAsString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return retList;
    }

    public static ItemStack getDrop(String packName) {
        File pack = new File(dataFolder, packName + ".json");
        if (!pack.exists()) return null;
        JsonObject jPack;
        try (FileReader reader = new FileReader(pack)) {
            jPack = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonElement drops = jPack.get("rarities");
        JsonObject dropsObj = drops.getAsJsonObject();
        Set<String> rarities = dropsObj.keySet();
        String[] raritiesStrings = new String[rarities.size()];
        JsonObject[] iDropsObjs = new JsonObject[rarities.size()];
        double totalWeight = 0.0;
        int x = 0;
        for (String i : rarities) {
            JsonElement idrops = dropsObj.get(i);
            JsonObject idropsObj = idrops.getAsJsonObject();
            totalWeight += idropsObj.get("weight").getAsDouble();
            raritiesStrings[x] = i;
            iDropsObjs[x] = idropsObj;
            x++;
        }
        int idx = 0;
        String rarityName = null;
        for (double r = Math.random() * totalWeight; idx < rarities.size() - 1; ++idx) {
            r -= iDropsObjs[idx].get("weight").getAsDouble();
            rarityName = raritiesStrings[idx];
            if (r <= 0.0) break;
        }
        //get rarity object
        JsonObject finIDrops = iDropsObjs[idx];
        //get drop list
        List<JsonElement> dropPool = finIDrops.get("drops").getAsJsonArray().asList();
        //get color
        String color = finIDrops.get("color").getAsString();
        //roll for item
        Random rand = new Random();
        String itemName = String.valueOf(dropPool.get(rand.nextInt(dropPool.size())));
        //cut off quotes
        itemName = itemName.substring(1);
        itemName = itemName.substring(0,itemName.length()-1);
        //create item
        ItemStack ret = new ItemStack(Material.PAPER);
        ItemMeta meta = ret.getItemMeta();
        NamespacedKey key = new NamespacedKey(MineCards.getPluginObj(), "id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, packName+":"+itemName);
        //make name and roll for shiny
        Component message = null;
        if (Math.random() < .05){
            message = MiniMessage.miniMessage().deserialize("<rainbow>SHINY!</rainbow> "+"<" + color + ">" + itemName);
            meta.setEnchantmentGlintOverride(true);
        } else {
            message = MiniMessage.miniMessage().deserialize("<" + color + ">" + itemName);
        }
        Component lore = MiniMessage.miniMessage().deserialize("<"+color+">"+rarityName);
        meta.itemName(message);
        meta.lore(Arrays.asList(lore));
        ret.setItemMeta(meta);
        return ret;
    }
}