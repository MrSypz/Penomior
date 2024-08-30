package sypztep.penomior.common.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.item.RefinementStoneItem;

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
        REFINE_WEAPON_STONE = registerItem("refine_weapon_stone", new RefinementStoneItem(new Item.Settings().maxCount(99).rarity(Rarity.RARE), "Used to refine weapons"));
        REFINE_ARMOR_STONE = registerItem("refine_armor_stone", new RefinementStoneItem(new Item.Settings().maxCount(99).rarity(Rarity.RARE), "Used to refine armor"));
        LOSS_FRAGMENT = registerItem("loss_fragment", new RefinementStoneItem(new Item.Settings().maxCount(99).rarity(Rarity.UNCOMMON), "Combine to obtain Refined Armor Enforge"));
        LAHAV_FRAGMENT = registerItem("lahav_fragment", new RefinementStoneItem(new Item.Settings().maxCount(99).rarity(Rarity.UNCOMMON).fireproof(), "Combine to obtain Refined Weapon Enforge"));
        REFINE_WEAPONENFORGE_STONE = registerItem("refine_weapon_enforge_stone", new RefinementStoneItem(new Item.Settings().maxCount(99).rarity(Rarity.EPIC), "Refines weapons with level > 15"));
        REFINE_ARMORENFORGE_STONE = registerItem("refine_armor_enforge_stone", new RefinementStoneItem(new Item.Settings().maxCount(99).rarity(Rarity.EPIC).fireproof(), "Refines armor with level > 15"));
        MAHILNANT = registerItem("mahilnant", new RefinementStoneItem(new Item.Settings().maxCount(99).rarity(Rarity.RARE), ""));
        MOONLIGHT_CRESCENT = registerItem("moonlight_crescent", new RefinementStoneItem(new Item.Settings().maxCount(99).rarity(Rarity.RARE), "Used to repair items"));
    }

    public static <T extends Item> T registerItem(String name, T item) {
        Registry.register(Registries.ITEM, Penomior.id(name), item);
        return item;
    }
}
