package sypztep.penomior.common.stats;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public abstract class Stat {
    protected int baseValue;
    protected int currentValue;
    protected int increasePerPoint;

    public Stat(int baseValue, int increasePerPoint) {
        this.baseValue = baseValue;
        this.currentValue = baseValue;
        this.increasePerPoint = increasePerPoint;
    }
    public Stat(int baseValue) {
        this.baseValue = baseValue;
        this.currentValue = baseValue;
        this.increasePerPoint = 1;
    }

    public void readFromNbt(NbtCompound tag) {
        this.currentValue = tag.getInt("CurrentValue");
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putInt("CurrentValue", this.currentValue);
    }

    public int getBaseValue() {
        return baseValue;
    }

    public int getValue() {
        return currentValue;
    }

    public int getIncreasePerPoint() {
        return increasePerPoint;
    }

    public void setIncreasePerPoint(int increasePerPoint) {
        this.increasePerPoint = increasePerPoint;
    }

    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }
    public abstract void applyPrimaryEffect(PlayerEntity player);
    public abstract void applySecondaryEffect(PlayerEntity player);

    // Reset the stat to its base value
    public void reset() {
        this.currentValue = baseValue;
    }

    // Optionally, you can have a method to modify the base value
    public void modifyBaseValue(int value) {
        this.baseValue += value;
        reset();
    }
}
