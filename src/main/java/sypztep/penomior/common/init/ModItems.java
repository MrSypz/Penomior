package sypztep.penomior.common.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import sypztep.penomior.Penomior;

public class ModItems {
    public static Item REFINE_WEAPON_STONE;
    public static Item REFINE_ARMOR_STONE;
    public static Item LOSS_FRAGMENT;
    public static Item LAHAV_FRAGMENT;
    public static Item REFINE_WEAPONENFORGE_STONE;
    public static Item REFINE_ARMORENFORGE_STONE;
    public static Item MAHILNANT;
    public static Item MOONLIGHT_CRESCENT;
    public static void init() {
        REFINE_WEAPON_STONE = registeritem("refine_weapon_stone", new Item(new Item.Settings().maxCount(99).rarity(Rarity.RARE)));
        REFINE_ARMOR_STONE = registeritem("refine_armor_stone", new Item(new Item.Settings().maxCount(99).rarity(Rarity.RARE)));
        LOSS_FRAGMENT = registeritem("loss_fragment", new Item(new Item.Settings().maxCount(99).rarity(Rarity.UNCOMMON)));
        LAHAV_FRAGMENT = registeritem("lahav_fragment", new Item(new Item.Settings().maxCount(99).rarity(Rarity.UNCOMMON).fireproof()));
        REFINE_WEAPONENFORGE_STONE = registeritem("refine_weapon_enforge_stone", new Item(new Item.Settings().maxCount(99).rarity(Rarity.EPIC)));
        REFINE_ARMORENFORGE_STONE = registeritem("refine_armor_enforge_stone", new Item(new Item.Settings().maxCount(99).rarity(Rarity.EPIC).fireproof()));
        MAHILNANT = registeritem("mahilnant", new Item(new Item.Settings().maxCount(99).rarity(Rarity.RARE)));
        MOONLIGHT_CRESCENT = registeritem("moonlight_crescent", new Item(new Item.Settings().maxCount(99).rarity(Rarity.RARE)));
    }
    public static <T extends Item> T registeritem(String name, T item) {
        Registry.register(Registries.ITEM, Penomior.id(name), item);
        return item;
    }
}
