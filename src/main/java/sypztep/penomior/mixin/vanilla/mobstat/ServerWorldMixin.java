package sypztep.penomior.mixin.vanilla.mobstat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.data.BaseMobStatsEntry;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.data.MobStatsEntry;
import sypztep.penomior.common.stats.LivingStats;
import sypztep.penomior.common.stats.Stat;
import sypztep.penomior.common.stats.StatTypes;
import sypztep.penomior.common.util.PlayerEntityUtils;

import java.util.*;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(method = "spawnEntity", at = @At("HEAD"))
    private void applyMobStats(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof LivingEntity living && ModConfig.mobEvasion) {
            EntityType<?> mobEntityType = living.getType();
            applyStatsForMobType(living, mobEntityType, MobStatsEntry.MOBSTATS_MAP, true);
            applyStatsForMobType(living, mobEntityType, BaseMobStatsEntry.BASEMOBSTATS_MAP, false);
        }
    }

    @Unique
    private <T> void applyStatsForMobType(LivingEntity living, EntityType<?> mobEntityType, Map<EntityType<?>, T> statsMap, boolean isMobStats) {
        T entry = statsMap.get(mobEntityType);
        if (entry == null) return;

        if (isMobStats) {
            if (entry instanceof MobStatsEntry mobStatsEntry) {
                StatsComponent statsComponent = ModEntityComponents.STATS.get(living);
                statsComponent.setEvasion(mobStatsEntry.evasion());
                statsComponent.setAccuracy(mobStatsEntry.accuracy());
            }
        } else {
            if (entry instanceof BaseMobStatsEntry baseMobStatsEntry) {
                UniqueStatsComponent uniqueStatsComponent = ModEntityComponents.UNIQUESTATS.get(living);
                LivingStats livingStats = uniqueStatsComponent.getLivingStats();

                if (!(living.getWorld() instanceof ServerWorld serverWorld)) {
                    return; // Or throw an exception if that's more appropriate
                }

                List<PlayerEntity> players = PlayerEntityUtils.getPlayersInChunk(serverWorld, living.getChunkPos().x, living.getChunkPos().z,1);
                int totalLevel = 0;

                int playerCount = 0;
                for (PlayerEntity player : players) {
                    if (player instanceof ServerPlayerEntity serverPlayer) {
                        UniqueStatsComponent playerStats = ModEntityComponents.UNIQUESTATS.get(serverPlayer);
                        totalLevel += playerStats.getLevel();
                        playerCount++;
                    }
                }

                int meanLevel = playerCount > 0 ? totalLevel / playerCount : 0;

                Map<StatTypes, Integer> localStatsMap = Map.of(
                        StatTypes.STRENGTH, baseMobStatsEntry.str(),
                        StatTypes.AGILITY, baseMobStatsEntry.agi(),
                        StatTypes.DEXTERITY, baseMobStatsEntry.dex(),
                        StatTypes.VITALITY, baseMobStatsEntry.vit(),
                        StatTypes.INTELLIGENCE, baseMobStatsEntry.anint(),
                        StatTypes.LUCK, baseMobStatsEntry.luk()
                );

                StatsComponent statsComponent = ModEntityComponents.STATS.get(living);
                statsComponent.setExtraEvasion(localStatsMap.get(StatTypes.AGILITY));
                statsComponent.setExtraAccuracy(localStatsMap.get(StatTypes.DEXTERITY));
                statsComponent.addExtraAccuracy(localStatsMap.get(StatTypes.LUCK) / 5);
                statsComponent.addExtraEvasion(localStatsMap.get(StatTypes.LUCK) / 5);

                List<StatTypes> statTypes = new ArrayList<>(localStatsMap.keySet());

                for (StatTypes statType : statTypes) {
                    Stat stat = livingStats.getStat(statType);
                    stat.setPoints(localStatsMap.get(statType) + meanLevel);
                    stat.applyPrimaryEffect(living);
                    stat.applySecondaryEffect(living);
                }

                living.setHealth(living.getMaxHealth());
                uniqueStatsComponent.getLivingStats().getLevelSystem().setLevel(baseMobStatsEntry.lvl() + meanLevel);
            }
        }
    }
}
