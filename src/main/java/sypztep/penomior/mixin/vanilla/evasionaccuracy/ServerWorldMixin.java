package sypztep.penomior.mixin.vanilla.evasionaccuracy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.data.MobStatsEntry;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(method = "spawnEntity", at = @At("HEAD"))
    private void applyMobStats(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof MobEntity mobEntity) {
            EntityType<?> mobEntityType = mobEntity.getType();
            for (EntityType<?> entityType : MobStatsEntry.MOBSTATS_MAP.keySet()) {
                if (mobEntityType.equals(entityType)) {
                    MobStatsEntry entry = MobStatsEntry.MOBSTATS_MAP.get(entityType);
                    if (entry == null)
                        continue;

                    StatsComponent statsComponent = ModEntityComponents.STATS.getNullable(mobEntity);
                    if (statsComponent == null)
                        continue;

                    statsComponent.setEvasion(entry.evasion());
                    statsComponent.setAccuracy(entry.accuracy());
                }
            }
        }
    }
}
