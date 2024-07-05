package sypztep.penomior.common.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.penomior.Penomior;

public class ModItem {
    public static Item REFINE_TABLE;
//    public static Item COPPERAL_WEAPON;
//    public static Item COPPERAL_ARMOR;
    public static void init() {
        REFINE_TABLE = registeritem("refine_table",new BlockItem(ModBlockItem.REFINE_TABLE, new Item.Settings()));
//        COPPERAL_WEAPON = registeritem("copperal_weapon", new Item(new Item.Settings().maxCount(99)));
//        COPPERAL_ARMOR = registeritem("copperal_armor", new Item(new Item.Settings().maxCount(99)));
    }
    public static <T extends Item> T registeritem(String name, T item) {
        Registry.register(Registries.ITEM, Penomior.id(name), item);
        return item;
    }
}
