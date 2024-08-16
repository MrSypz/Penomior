package sypztep.penomior.common.stats;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityAttributes;

public class VitalityStat extends Stat {
    public VitalityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(ServerPlayerEntity player) {
        // Retrieve the attribute instance
        EntityAttributeInstance attributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

        if (attributeInstance != null) {
            // Calculate the additional health
            double baseHealth = player.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH);
            double additionalHealth = baseHealth * (0.05 * this.currentValue);

            EntityAttributeModifier existingModifier = attributeInstance.getModifier(getPrimaryId());
            if (existingModifier != null) {
                attributeInstance.removeModifier(existingModifier);
            }

            // Create and add the new modifier
            EntityAttributeModifier mod = new EntityAttributeModifier(getPrimaryId(), additionalHealth, EntityAttributeModifier.Operation.ADD_VALUE);
            attributeInstance.addPersistentModifier(mod);
        }
    }

    @Override
    public void applySecondaryEffect(ServerPlayerEntity player) {
        // Retrieve the attribute instance
        EntityAttributeInstance attributeInstance = player.getAttributeInstance(ModEntityAttributes.GENERIC_HEALTH_REGEN);
//
        if (attributeInstance != null) {
            // Calculate the additional health regeneration
            double baseCritChance = player.getAttributeBaseValue(ModEntityAttributes.GENERIC_HEALTH_REGEN);
            double additionalCritChance = (baseCritChance + 0.25f) * (0.02 * this.currentValue) ;

            // Remove existing modifiers with the same UUID (if any)
            EntityAttributeModifier existingModifier = attributeInstance.getModifier(getSecondaryId());
            if (existingModifier != null) {
                attributeInstance.removeModifier(existingModifier);
            }

            // Create and add the new modifier
            EntityAttributeModifier mod = new EntityAttributeModifier(getSecondaryId(), additionalCritChance, EntityAttributeModifier.Operation.ADD_VALUE);
            attributeInstance.addPersistentModifier(mod);
        }
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("vitality_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("vitality_secondary");
    }
}
