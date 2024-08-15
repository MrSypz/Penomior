package sypztep.penomior.common.stats;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import sypztep.penomior.common.init.ModEntityAttributes;


public class VitalityStat extends Stat {
    public VitalityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }

    @Override
    public void applyPrimaryEffect(PlayerEntity player) {
        double baseHealth = player.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH);
        double additionalHealth = baseHealth * (0.05 * this.currentValue);
        player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(baseHealth + additionalHealth);
    }

    @Override
    public void applySecondaryEffect(PlayerEntity player) {
        double baseCritChance = player.getAttributeBaseValue(ModEntityAttributes.GENERIC_HEALTH_REGEN);
        double additionalCritChance = baseCritChance * (0.02 * this.currentValue);
        player.getAttributeInstance(ModEntityAttributes.GENERIC_HEALTH_REGEN).setBaseValue(baseCritChance + additionalCritChance);
    }
}
