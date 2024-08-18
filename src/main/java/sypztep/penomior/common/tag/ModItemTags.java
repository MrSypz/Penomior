package sypztep.penomior.common.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import sypztep.penomior.Penomior;

public class ModItemTags {
    public static final TagKey<Item> IGNORE_MODIFIER_ITEM = TagKey.of(RegistryKeys.ITEM, Penomior.id("ignore_modifier_item"));
}
