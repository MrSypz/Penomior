package sypztep.penomior.common.reloadlistener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import sypztep.penomior.Penomior;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MobStatsReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final Identifier ID = Penomior.id("mobstats");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        MobStatsEntry.MOBSTATS_MAP.clear();
        manager.findAllResources("mobstats", path -> path.getNamespace().equals(Penomior.MODID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
            for (Resource resource : resources) {
                try (InputStream stream = resource.getInputStream()) {
                    JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();

                    String filePath = identifier.getPath();
                    String entityIdStr = filePath.substring(filePath.indexOf("/") + 1, filePath.length() - 5).replace("/", ":");
                    Identifier entityId = Identifier.of(entityIdStr);
                    EntityType<?> entityType = Registries.ENTITY_TYPE.get(entityId);

                    if (entityType == Registries.ENTITY_TYPE.get(Registries.ENTITY_TYPE.getDefaultId()) && !entityId.equals(Registries.ENTITY_TYPE.getDefaultId())) {
                        continue;
                    }

                    int evasion = JsonHelper.getInt(object, "evasion");
                    int accuracy = JsonHelper.getInt(object, "accuracy");

                    MobStatsEntry.MOBSTATS_MAP.put(entityType, new MobStatsEntry(evasion, accuracy));
                } catch (Exception e) {
                    Penomior.LOGGER.error("Failed to load mob stats from '{}': {}", identifier, e.getMessage());
                    Penomior.LOGGER.error("Exception details: ", e);
                }
            }
        });

    }
}
