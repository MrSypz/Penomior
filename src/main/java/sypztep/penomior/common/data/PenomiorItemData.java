package sypztep.penomior.common.data;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public record PenomiorItemData(
        String itemID,
        int maxLvl,
        int startAccuracy,
        int endAccuracy,
        int startEvasion,
        int endEvasion
) {
    public static PenomiorItemData getPenomiroItemData(ItemStack stack) {
        String itemID = Registries.ITEM.getId(stack.getItem()).toString();
        return PenomiorItemDataSerializer.getConfigCache().get(itemID);
    }
}
