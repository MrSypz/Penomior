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
    private static Map<String, PenomiorItemData> configCache;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("penomior/penomior_item_data.json");
    public static PenomiorItemDataSerializer serializer = new PenomiorItemDataSerializer();

    public PenomiorItemDataSerializer() {
    }
    public void loadConfig() {
        if (configCache == null) {
            try {
                createDirectoriesIfNeeded();
                Path configFilePath = CONFIG_FILE_PATH;
                if (Files.exists(configFilePath)) {
                    try (Reader reader = new FileReader(configFilePath.toFile())) {
                        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                        PenomiorItemDataMap dataMap = gson.fromJson(jsonObject, PenomiorItemDataMap.class);
                        int itemCount = dataMap.itemDataMap().size(); // Count items for informative message
                        if (itemCount > 0) {
                            Penomior.LOGGER.info(String.format("Found %d RefineItem Data items!", itemCount));
                        } else {
                            Penomior.LOGGER.warn("No RefineItem Data found. (Recommend delete this file {} )", CONFIG_FILE_PATH);
                        }
                        configCache = dataMap.itemDataMap();
                    }
                } else {
                    Penomior.LOGGER.warn("Configuration file does not exist. Creating new file with default data.");
                    saveConfig(new PenomiorItemDataMap(getDefaultData().itemDataMap()));
                    configCache = new HashMap<>(getDefaultData().itemDataMap());
                }
            } catch (IOException e) {
                Penomior.LOGGER.error("Error loading configuration from file: {}", CONFIG_FILE_PATH, e);
                configCache = new HashMap<>();
            }
        }
    }

    public static Map<String, PenomiorItemData> getConfigCache() {
        return configCache;
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
        // Swords
        defaultData.put("minecraft:wooden_sword", new PenomiorItemData("minecraft:wooden_sword", 20, 0, 182, 0, 0,100));
        defaultData.put("minecraft:stone_sword", new PenomiorItemData("minecraft:stone_sword", 20, 0, 182, 0, 0,100));
        defaultData.put("minecraft:golden_sword", new PenomiorItemData("minecraft:golden_sword", 20, 0, 182, 0, 0,100));
        defaultData.put("minecraft:iron_sword", new PenomiorItemData("minecraft:iron_sword", 20, 0, 182, 0, 0,100));
        defaultData.put("minecraft:diamond_sword", new PenomiorItemData("minecraft:diamond_sword", 20, 0, 182, 0, 0,100));
        defaultData.put("minecraft:netherite_sword", new PenomiorItemData("minecraft:netherite_sword", 20, 0, 182, 0, 0,100));

        // Helmets
        defaultData.put("minecraft:leather_helmet", new PenomiorItemData("minecraft:leather_helmet", 20, 0, 0, 0, 82,100));
        defaultData.put("minecraft:chainmail_helmet", new PenomiorItemData("minecraft:chainmail_helmet", 20, 0, 0, 0, 82,100));
        defaultData.put("minecraft:iron_helmet", new PenomiorItemData("minecraft:iron_helmet", 20, 0, 0, 0, 82,100));
        defaultData.put("minecraft:golden_helmet", new PenomiorItemData("minecraft:golden_helmet", 20, 0, 0, 0, 82,100));
        defaultData.put("minecraft:diamond_helmet", new PenomiorItemData("minecraft:diamond_helmet", 20, 0, 0, 0, 82,100));
        defaultData.put("minecraft:netherite_helmet", new PenomiorItemData("minecraft:netherite_helmet", 20, 0, 0, 0, 82,100));

        // Chestplates
        defaultData.put("minecraft:leather_chestplate", new PenomiorItemData("minecraft:leather_chestplate", 20, 0, 0, 0, 152,100));
        defaultData.put("minecraft:chainmail_chestplate", new PenomiorItemData("minecraft:chainmail_chestplate", 20, 0, 0, 0, 152,100));
        defaultData.put("minecraft:iron_chestplate", new PenomiorItemData("minecraft:iron_chestplate", 20, 0, 0, 0, 152,100));
        defaultData.put("minecraft:golden_chestplate", new PenomiorItemData("minecraft:golden_chestplate", 20, 0, 0, 0, 152,100));
        defaultData.put("minecraft:diamond_chestplate", new PenomiorItemData("minecraft:diamond_chestplate", 20, 0, 0, 0, 152,100));
        defaultData.put("minecraft:netherite_chestplate", new PenomiorItemData("minecraft:netherite_chestplate", 20, 0, 0, 0, 152,100));

        // Boots
        defaultData.put("minecraft:leather_boots", new PenomiorItemData("minecraft:leather_boots", 20, 0, 0, 0, 129,100));
        defaultData.put("minecraft:chainmail_boots", new PenomiorItemData("minecraft:chainmail_boots", 20, 0, 0, 0, 129,100));
        defaultData.put("minecraft:iron_boots", new PenomiorItemData("minecraft:iron_boots", 20, 0, 0, 0, 129,100));
        defaultData.put("minecraft:golden_boots", new PenomiorItemData("minecraft:golden_boots", 20, 0, 0, 0, 129,100));
        defaultData.put("minecraft:diamond_boots", new PenomiorItemData("minecraft:diamond_boots", 20, 0, 0, 0, 129,100));
        defaultData.put("minecraft:netherite_boots", new PenomiorItemData("minecraft:netherite_boots", 20, 0, 0, 0, 129,100));

        // Leggings
        defaultData.put("minecraft:leather_leggings", new PenomiorItemData("minecraft:leather_leggings", 20, 0, 48, 0, 96,100));
        defaultData.put("minecraft:chainmail_leggings", new PenomiorItemData("minecraft:chainmail_leggings", 20, 0, 48, 0, 96,100));
        defaultData.put("minecraft:iron_leggings", new PenomiorItemData("minecraft:iron_leggings", 20, 0, 48, 0, 96,100));
        defaultData.put("minecraft:golden_leggings", new PenomiorItemData("minecraft:golden_leggings", 20, 0, 48, 0, 96,100));
        defaultData.put("minecraft:diamond_leggings", new PenomiorItemData("minecraft:diamond_leggings", 20, 0, 48, 0, 96,100));
        defaultData.put("minecraft:netherite_leggings", new PenomiorItemData("minecraft:netherite_leggings", 20, 0, 48, 0, 96,100));

        return new PenomiorItemDataMap(defaultData);
    }


    public record PenomiorItemDataMap(Map<String, PenomiorItemData> itemDataMap) {
    }
}