package dev.cheech;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class Pack {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private record Rarity(String name, double weight, String color, List<String> drops) {
    }

    private String name;
    private String displayName;
    private List<Rarity> rarities;

    public Pack(File file){
        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            this.name = jsonObject.get("Name").getAsString();
            this.displayName = jsonObject.get("DisplayName").getAsString();
            JsonObject curRarities = jsonObject.get("rarities").getAsJsonObject();
            Set<String> nameRarities = curRarities.keySet();
            Type listType = new TypeToken<List<String>>(){}.getType();
            for (String i : nameRarities){
                JsonObject rarity = curRarities.get(i).getAsJsonObject();
                List<String> drops = gson.fromJson(rarity.get("drops"), listType);;
                this.rarities.add(new Rarity(i, rarity.get("weight").getAsDouble(), rarity.get("color").getAsString(), drops));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getName(){
        return name;
    }
    public String getDisplayName(){
        return displayName;
    }
    public List<String> getRarities(){
        List<String> ret = null;
        for (Rarity i : rarities){
            ret.add(i.name());
        }
        return ret;
    }
}
