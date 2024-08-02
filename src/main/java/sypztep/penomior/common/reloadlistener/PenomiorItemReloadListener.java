package sypztep.penomior.common.reloadlistener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.data.PenomiorItemEntry;

import java.io.InputStream;
import java.io.InputStreamReader;

public class PenomiorItemReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final Identifier ID = Penomior.id("penomioritemdata");
    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        PenomiorItemEntry.PENOMIOR_ITEM_ENTRY_MAP.clear();
        manager.findAllResources("refine", path -> path.getNamespace().equals(Penomior.MODID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
            for (Resource resource : resources) {
                try (InputStream stream = resource.getInputStream()) {
                    JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
                    Identifier itemId = Identifier.of(identifier.getPath().substring(identifier.getPath().indexOf("/") + 1, identifier.getPath().length() - 5).replace("/", ":"));
                    Item item = Registries.ITEM.get(itemId);

                    if (item == Registries.ITEM.get(Registries.ITEM.getDefaultId()) && !itemId.equals(Registries.ITEM.getDefaultId())) {
                        continue;
                    }
                    int maxLvl = object.get("maxLvl").getAsInt();
                    int startAccuracy = object.get("startAccuracy").getAsInt();
                    int endAccuracy = object.get("endAccuracy").getAsInt();
                    int startEvasion = object.get("startEvasion").getAsInt();
                    int endEvasion = object.get("endEvasion").getAsInt();
                    int maxDurability = object.get("maxDurability").getAsInt();
                    int starDamage = object.get("starDamage").getAsInt();
                    int endDamage = object.get("endDamage").getAsInt();
                    int startProtection = object.get("startProtection").getAsInt();
                    int endProtection = object.get("endProtection").getAsInt();
                    int repairpoint = object.get("repairpoint").getAsInt();

                    PenomiorItemEntry entry = new PenomiorItemEntry(
                            maxLvl,
                            startAccuracy,
                            endAccuracy,
                            startEvasion,
                            endEvasion,
                            maxDurability,
                            starDamage,
                            endDamage,
                            startProtection,
                            endProtection,
                            repairpoint
                    );
                    PenomiorItemEntry.PENOMIOR_ITEM_ENTRY_MAP.put(Registries.ITEM.getEntry(item), entry);

                } catch (Exception ignored) {

                }
            }
        });
    }
}
