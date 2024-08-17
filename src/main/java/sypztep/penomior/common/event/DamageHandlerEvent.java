package sypztep.penomior.common.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.penomior.common.util.DamageTrackerUtil;

public class DamageHandlerEvent implements ServerLivingEntityEvents.AllowDamage {
    public static final DamageTrackerUtil damageTracker = new DamageTrackerUtil();

    @Override
    public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
        if (source.getSource() instanceof ServerPlayerEntity player) {
            damageTracker.recordDamage(player, (int) amount);
        }
        return true;
    }
}