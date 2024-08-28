package sypztep.penomior.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class ModItemGroups {
    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addAfter(Items.DISC_FRAGMENT_5, ModItems.REFINE_ARMOR_STONE);
            content.addAfter(ModItems.REFINE_ARMOR_STONE, ModItems.REFINE_WEAPON_STONE);
            content.addAfter(ModItems.REFINE_WEAPON_STONE, ModItems.LOSS_FRAGMENT);
            content.addAfter(ModItems.LOSS_FRAGMENT, ModItems.LAHAV_FRAGMENT);
            content.addAfter(ModItems.LAHAV_FRAGMENT, ModItems.REFINE_ARMORENFORGE_STONE);
            content.addAfter(ModItems.REFINE_ARMORENFORGE_STONE, ModItems.REFINE_WEAPONENFORGE_STONE);
            content.addAfter(ModItems.REFINE_WEAPONENFORGE_STONE, ModItems.MAHILNANT);
            content.addAfter(ModItems.MAHILNANT, ModItems.MOONLIGHT_CRESCENT);
        });
    }
}
