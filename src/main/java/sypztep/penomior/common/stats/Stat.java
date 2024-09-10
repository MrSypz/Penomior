package sypztep.penomior.common.stats;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.penomior.common.util.AttributeModification;

import java.util.List;
import java.util.function.ToDoubleFunction;

public abstract class Stat {
    protected int baseValue;
    protected int currentValue  ;
    protected int increasePerPoint;
    protected int totalPointsUsed; // Track total points used

    public Stat(int baseValue) {
        this(baseValue, 1);
    }

    public Stat(int baseValue, int increasePerPoint) {
        this.baseValue = baseValue;
        this.currentValue = baseValue;
        this.increasePerPoint = increasePerPoint;
        this.totalPointsUsed = 0;
    }

    public void readFromNbt(NbtCompound tag) {
        this.currentValue = tag.getInt("CurrentValue");
        this.totalPointsUsed = tag.getInt("TotalPointsUsed"); // Read total points used from NBT
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putInt("CurrentValue", this.currentValue);
        tag.putInt("TotalPointsUsed", this.totalPointsUsed); // Write total points used to NBT
    }

    public int getBaseValue() {
        return baseValue;
    }

    public int getValue() {
        return currentValue;
    }

    public int getIncreasePerPoint() {
        return increasePerPoint = 1 + (currentValue / 10);
    }

    public void setIncreasePerPoint(int increasePerPoint) {
        this.increasePerPoint = increasePerPoint;
    }

    public void increase(int points) {
        this.currentValue += points;
        this.totalPointsUsed += (points * increasePerPoint); // Track แต้มที่ใช้
    }

    public abstract void applyPrimaryEffect(LivingEntity player);
    public abstract void applySecondaryEffect(LivingEntity player);
    protected void applyEffect(LivingEntity living, RegistryEntry<EntityAttribute> attribute, Identifier modifierId, EntityAttributeModifier.Operation operation, ToDoubleFunction<Double> effectFunction) {
        EntityAttributeInstance attributeInstance = living.getAttributeInstance(attribute);
        if (attributeInstance != null) {
            double baseValue = living.getAttributeBaseValue(attribute);
            double effectValue = effectFunction.applyAsDouble(baseValue);

            if (modifierId == null) {
                throw new IllegalArgumentException("modifierId cannot be null");
            }
            // Remove existing modifier
            EntityAttributeModifier existingModifier = attributeInstance.getModifier(modifierId);
            if (existingModifier != null) {
                attributeInstance.removeModifier(existingModifier);
            }

            // Apply new modifier with the specified operation
            EntityAttributeModifier mod = new EntityAttributeModifier(modifierId, effectValue, operation);
            attributeInstance.addPersistentModifier(mod);
        }
    }
    protected void applyEffects(LivingEntity living, List<AttributeModification> modifications) {
        for (AttributeModification modification : modifications) {
            EntityAttributeInstance attributeInstance = living.getAttributeInstance(modification.attribute());
            if (attributeInstance != null) {
                double baseValue = living.getAttributeBaseValue(modification.attribute());
                double effectValue = modification.effectFunction().applyAsDouble(baseValue);

                if (modification.modifierId() == null) {
                    throw new IllegalArgumentException("modifierId cannot be null");
                }
                EntityAttributeModifier existingModifier = attributeInstance.getModifier(modification.modifierId());
                if (existingModifier != null) {
                    attributeInstance.removeModifier(existingModifier);
                }

                EntityAttributeModifier mod = new EntityAttributeModifier(modification.modifierId(), effectValue, modification.operation());
                attributeInstance.addPersistentModifier(mod);
            }
        }
    }

    // Reset the stat to its base value and clean up attribute instances
    public void reset(ServerPlayerEntity player, LevelSystem levelSystem) {
        levelSystem.addStatPoints(this.totalPointsUsed);

        this.currentValue = baseValue;
        this.totalPointsUsed = 0;

        applyPrimaryEffect(player);
        applySecondaryEffect(player);
    }
    public abstract List<Text> getEffectDescription(int additionalPoints);
    protected Identifier getPrimaryId() {
        return null;
    }
    protected Identifier getSecondaryId(){
        return null;
    }
}

