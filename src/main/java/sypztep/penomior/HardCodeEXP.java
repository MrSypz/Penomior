package sypztep.penomior;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.apache.commons.lang3.mutable.MutableFloat;
import sypztep.penomior.common.init.ModEntityComponents;

public class HardCodeEXP implements ServerLivingEntityEvents.AfterDeath, ServerLivingEntityEvents.AllowDamage {
    private final MutableFloat mutableFloat = new MutableFloat();

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        LivingEntity livingEntity = entity.getPrimeAdversary();
        if (livingEntity != null) {
            ModEntityComponents.UNIQUESTATS.get(livingEntity).addExperience(5);
        }
    }

    @Override
    public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
        if (entity.isAlive() && source.getAttacker() instanceof LivingEntity)
            mutableFloat.add(amount);
        return false;
    }
}
