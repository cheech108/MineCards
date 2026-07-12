package dev.cheech;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

// Class Containing all information pretaining to a pack
public class Pack {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public record Rarity(String name, double weight, String color, List<String> drops) {
    }

    // Variable Library
    private final String name;
    private final String displayName;
    private final Map<String, Rarity> rarities = new HashMap<>();
    private final int cardsGiven;

    // Pack Object Constuctor (uses file data as params)
    public Pack(File file){
        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            this.name = file.getName().substring(0,file.getName().length()-5);
            this.displayName = jsonObject.get("DisplayName").getAsString();
            this.cardsGiven = jsonObject.get("CardsGiven") != null ? jsonObject.get("CardsGiven").getAsInt() : 3;
            JsonObject curRarities = jsonObject.get("Rarities").getAsJsonObject();
            Set<String> nameRarities = curRarities.keySet();
            Type listType = new TypeToken<List<String>>(){}.getType();
            for (String i : nameRarities){
                JsonObject rarity = curRarities.get(i).getAsJsonObject();
                List<String> drops = gson.fromJson(rarity.get("drops"), listType);
                this.rarities.put(i, new Rarity(i, rarity.get("weight").getAsDouble(), rarity.get("color").getAsString(), drops));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Getter methods for the names and abs.names of the cards
    public String getName(){
        return name;
    }
    public String getDisplayName(){
        return displayName;
    }
    public List<String> getRarities(){
        List<String> ret = new ArrayList<>();
        rarities.forEach((key,_)->ret.add(key));
        return ret;
    }
    public Rarity getRarity(String name){
        return rarities.get(name);
    }
    public Map<String, Rarity> getRarityMap(){return rarities;}
    public int getCardsGiven(){
        return cardsGiven;
    }
}
