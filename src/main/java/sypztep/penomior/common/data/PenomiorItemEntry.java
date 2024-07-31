package sypztep.penomior.common.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record PenomiorItemEntry(
        int maxLvl,
        int startAccuracy,
        int endAccuracy,
        int startEvasion,
        int endEvasion,
        int maxDurability,
        int starDamage,
        int endDamage,
        int startProtection,
        int endProtection,
        int repairpoint
)
{
    public static final Map<RegistryEntry<Item>, PenomiorItemEntry> PENOMIOR_ITEM_ENTRY_MAP = new HashMap<>();

    public static Optional<PenomiorItemEntry> getPenomiorItemData(ItemStack stack) {
        RegistryEntry<Item> itemEntry = Registries.ITEM.getEntry(stack.getItem());
        return Optional.ofNullable(PENOMIOR_ITEM_ENTRY_MAP.get(itemEntry));
    }
    public static Optional<PenomiorItemEntry> getPenomiorItemData(String itemID) {
        Identifier itemIdentifier = Identifier.of(itemID);
        RegistryEntry<Item> itemEntry = Registries.ITEM.getEntry(itemIdentifier).orElse(null);
        return Optional.ofNullable(PENOMIOR_ITEM_ENTRY_MAP.get(itemEntry));
    }
    public static String getItemId(ItemStack stack) {
        return Registries.ITEM.getId(stack.getItem()).toString();
    }
}
