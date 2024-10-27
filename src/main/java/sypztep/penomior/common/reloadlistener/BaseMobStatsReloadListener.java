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
import sypztep.penomior.Penomior;
import sypztep.penomior.common.data.BaseMobStatsEntry;

import java.io.InputStream;
import java.io.InputStreamReader;

public class BaseMobStatsReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final Identifier ID = Penomior.id("basemobstats");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        BaseMobStatsEntry.BASEMOBSTATS_MAP.clear();
        manager.findAllResources("basemobstats", path -> path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
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
                    JsonObject basestat = object.getAsJsonObject("basestat");

                    int str = basestat.get("str").getAsInt();
                    int agi = basestat.get("agi").getAsInt();
                    int dex = basestat.get("dex").getAsInt();
                    int vit = basestat.get("vit").getAsInt();
                    int anInt = basestat.get("int").getAsInt();
                    int luk = basestat.get("luk").getAsInt();
                    int lvl = basestat.get("lvl").getAsInt();
                    int exp = basestat.get("exp").getAsInt();

                    BaseMobStatsEntry.BASEMOBSTATS_MAP.put(entityType, new BaseMobStatsEntry(str, agi, dex, vit, anInt, luk, lvl, exp));
                } catch (Exception e) {
                    Penomior.LOGGER.error("Failed to load base mob stats from '{}': {}", identifier, e.getMessage());
                    Penomior.LOGGER.error("Exception details: ", e);
                }
            }
        });

    }
}
