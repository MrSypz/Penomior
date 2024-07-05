package sypztep.penomior.common.data;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import sypztep.penomior.Penomior;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PenomiorItemDataSerializer {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("penomior/penomior_item_data.json");
    public static PenomiorItemDataSerializer serializer = new PenomiorItemDataSerializer();
    public PenomiorItemDataSerializer(){}
    public Map<String, PenomiorItemData> loadConfig() {
        try {
            createDirectoriesIfNeeded();
            Path configFilePath = CONFIG_FILE_PATH;
            if (Files.exists(configFilePath)) {
                try (Reader reader = new FileReader(configFilePath.toFile())) {
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    PenomiorItemDataMap dataMap = gson.fromJson(jsonObject, PenomiorItemDataMap.class);
                    Penomior.LOGGER.info("Loaded configuration from file: {}", configFilePath);
                    return dataMap.getItemDataMap();
                }
            } else {
                Penomior.LOGGER.warn("Configuration file does not exist. Creating new file with default data.");
                saveConfig(new PenomiorItemDataMap(getDefaultData().getItemDataMap()));
                return new HashMap<>();
            }
        } catch (IOException e) {
            Penomior.LOGGER.error("Error loading configuration from file: {}", CONFIG_FILE_PATH, e);
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Save the configuration to JSON file
    public void saveConfig(PenomiorItemDataMap newData) {
        try {
            createDirectoriesIfNeeded();
            try (Writer writer = new FileWriter(CONFIG_FILE_PATH.toFile())) {
                gson.toJson(newData, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create directories if they don't exist
    private void createDirectoriesIfNeeded() throws IOException {
        if (!Files.exists(CONFIG_FILE_PATH.getParent())) {
            Files.createDirectories(CONFIG_FILE_PATH.getParent());
        }
    }

    // Define your default data here
    private PenomiorItemDataMap getDefaultData() {
        Map<String, PenomiorItemData> defaultData = new HashMap<>();
        defaultData.put("minecraft:iron_sword", new PenomiorItemData("minecraft:iron_sword", 20, 0, 75));
        defaultData.put("minecraft:diamond_sword", new PenomiorItemData("minecraft:diamond_sword", 20, 15, 180));
        return new PenomiorItemDataMap(defaultData);
    }

    public static class PenomiorItemDataMap {
        private Map<String, PenomiorItemData> itemDataMap;

        public PenomiorItemDataMap(Map<String, PenomiorItemData> itemDataMap) {
            this.itemDataMap = itemDataMap;
        }

        public Map<String, PenomiorItemData> getItemDataMap() {
            return itemDataMap;
        }
    }
}