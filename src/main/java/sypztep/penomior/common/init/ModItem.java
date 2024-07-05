package sypztep.penomior.common.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.penomior.Penomior;

public class ModItem {
    public static Item REFINE_TABLE;
    public static Item REFINE_WEAPON_STONE;
    public static Item REFINE_ARMOR_STONE;
    public static void init() {
        REFINE_TABLE = registeritem("refine_table",new BlockItem(ModBlockItem.REFINE_TABLE, new Item.Settings()));
        REFINE_WEAPON_STONE = registeritem("refine_weapon_stone", new Item(new Item.Settings().maxCount(99)));
        REFINE_ARMOR_STONE = registeritem("refine_armor_stone", new Item(new Item.Settings().maxCount(99)));
    }
    public static <T extends Item> T registeritem(String name, T item) {
        Registry.register(Registries.ITEM, Penomior.id(name), item);
        return item;
    }
}
