package sypztep.penomior.common.init;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class ModLootableModify {
    public static void init() {
        LootTableEvents.MODIFY.register((id, tableBuilder, source) -> {
            if (source.isBuiltin()) {
                Block[] ores = {
                        Blocks.COAL_ORE,
                        Blocks.IRON_ORE,
                        Blocks.COPPER_ORE,
                        Blocks.REDSTONE_ORE,
                        Blocks.LAPIS_ORE,
                        Blocks.EMERALD_ORE,
                        Blocks.DIAMOND_ORE
                };
                for (Block ore : ores) {
                    if (ore.getLootTableKey().equals(id)) {
                        LootPool.Builder lootPool = LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1, 5)) // 1-5
                                .conditionally(RandomChanceLootCondition.builder(0.055f)); // 5.5%

                        // Add multiple items to the loot pool
                        lootPool.with(ItemEntry.builder(ModItems.REFINE_ARMOR_STONE));
                        lootPool.with(ItemEntry.builder(ModItems.REFINE_WEAPON_STONE));

                        tableBuilder.pool(lootPool);
                        break;
                    }
                }
            }
        });
    }
}
