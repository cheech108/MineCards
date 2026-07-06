package dev.cheech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
        JsonObject[] iDropsObjs = new JsonObject[rarities.size()];
        double totalWeight = 0.0;
        int x = 0;
        for (String i : rarities) {
            JsonElement idrops = dropsObj.get(i);
            JsonObject idropsObj = idrops.getAsJsonObject();
            totalWeight += idropsObj.get("weight").getAsDouble();
            iDropsObjs[x] = idropsObj;
            x++;
        }
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < rarities.size() - 1; ++idx) {
            r -= iDropsObjs[idx].get("weight").getAsDouble();
            if (r <= 0.0) break;
        }
        JsonObject finIDrops = iDropsObjs[idx];
        List<JsonElement> dropPool = finIDrops.get("drops").getAsJsonArray().asList();
        Random rand = new Random();
        String randomElement = String.valueOf(dropPool.get(rand.nextInt(dropPool.size())));
        Bukkit.getServer().getConsoleSender().sendMessage(randomElement);

        return new ItemStack(Material.PAPER);
    }
}