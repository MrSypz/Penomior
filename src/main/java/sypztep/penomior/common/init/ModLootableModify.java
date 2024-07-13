package sypztep.penomior.common.init;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Set;

public class ModLootableModify {
    private static final Set<EntityType<?>> HOSTILE_MOBS = Set.of(
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.SPIDER,
            EntityType.ENDERMAN,
            EntityType.BLAZE,
            EntityType.WITHER_SKELETON,
            EntityType.WITCH,
            EntityType.HUSK,
            EntityType.STRAY,
            EntityType.PHANTOM,
            EntityType.DROWNED,
            EntityType.PILLAGER,
            EntityType.VINDICATOR,
            EntityType.EVOKER,
            EntityType.VEX,
            EntityType.RAVAGER,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.GHAST,
            EntityType.HOGLIN,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.MAGMA_CUBE,
            EntityType.SLIME,
            EntityType.GUARDIAN,
            EntityType.ELDER_GUARDIAN,
            EntityType.SILVERFISH,
            EntityType.SHULKER,
            EntityType.ENDERMITE,
            EntityType.WITHER,
            EntityType.ENDER_DRAGON
    );

    public static void registerLootTableModifiers() {
        LootTableEvents.MODIFY.register((id, tableBuilder, source) -> {
            if (source.isBuiltin() && isHostileMobLootTable(id)) {
                LootPool.Builder lootPoolkillByPlayer = LootPool.builder()
                        .rolls(BinomialLootNumberProvider.create(1, 0.1f))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(ItemEntry.builder(ModItems.REFINE_WEAPON_STONE));

                tableBuilder.pool(lootPoolkillByPlayer);
            }
        });
    }

    private static boolean isHostileMobLootTable(RegistryKey<LootTable> id) {
        for (EntityType<?> entityType : HOSTILE_MOBS) {
            if (entityType.getLootTableId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}