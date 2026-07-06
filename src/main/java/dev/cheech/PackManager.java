package dev.cheech;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PackManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static File dataFolder;
    public PackManager(File dF){
        dataFolder = dF;
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public static List<String> getPacks(){
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
}
