package sypztep.penomior.common.reloadlistener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.data.DamageReductionEntry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class DamageReductionReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final Identifier ID = Penomior.id("damagereduction_map");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        DamageReductionEntry.DAMAGEREDUCTION_ENTRY_MAP.clear();
        manager.findAllResources("damagereduction/damage_reduction.json", path -> path.getNamespace().equals(Penomior.MODID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
            for (Resource resource : resources) {
                try (InputStream stream = resource.getInputStream()) {
                    JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();

                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        int point = Integer.parseInt(entry.getKey());
                        float percentage = entry.getValue().getAsFloat();
                        DamageReductionEntry.DAMAGEREDUCTION_ENTRY_MAP.put(point, percentage);
                    }
                } catch (Exception e) {
                    Penomior.LOGGER.error("Failed to load damage reduction map from '{}': {}", identifier, e.getMessage());
                    Penomior.LOGGER.error("Exception details: ", e);
                }
            }
        });
    }
}
