package sypztep.penomior.mixin.vanilla.mobstat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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

import java.util.Map;

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

                Map<StatTypes, Integer> localStatsMap = Map.of(
                        StatTypes.STRENGTH, baseMobStatsEntry.str(),
                        StatTypes.AGILITY, baseMobStatsEntry.agi(),
                        StatTypes.DEXTERITY, baseMobStatsEntry.dex(),
                        StatTypes.VITALITY, baseMobStatsEntry.vit(),
                        StatTypes.INTELLIGENCE, baseMobStatsEntry.anint(),
                        StatTypes.LUCK, baseMobStatsEntry.luk()
                );

                for (Map.Entry<StatTypes, Integer> entryz : localStatsMap.entrySet()) {
                    Stat stat = livingStats.getStat(entryz.getKey());
                    stat.increase(entryz.getValue());

                    stat.applyPrimaryEffect(living);
                    stat.applySecondaryEffect(living);
                }
                living.setHealth(living.getMaxHealth());
                uniqueStatsComponent.getLivingStats().getLevelSystem().setLevel(baseMobStatsEntry.lvl());
            }
        }
    }

}
