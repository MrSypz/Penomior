package sypztep.penomior.common.stats;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class Stat {
    protected int baseValue;
    protected int currentValue;
    protected int increasePerPoint;
    protected int totalPointsUsed; // Track total points used

    // Constructor with default increase per point of 1
    public Stat(int baseValue) {
        this(baseValue, 1);
    }

    // Constructor with specified increase per point
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
        this.totalPointsUsed += (points * increasePerPoint); // Track points used
    }

    public abstract void applyPrimaryEffect(ServerPlayerEntity player);
    public abstract void applySecondaryEffect(ServerPlayerEntity player);

    // Reset the stat to its base value and clean up attribute instances
    public void reset(ServerPlayerEntity player, LevelSystem levelSystem) {
        levelSystem.addStatPoints(this.totalPointsUsed);

        this.currentValue = baseValue;
        this.totalPointsUsed = 0;

        // Reapply the stat's effects
        applyPrimaryEffect(player);
        applySecondaryEffect(player);
    }


    /**
     * Method to get the modifier ID. Subclasses should override this to provide a specific ID.
     */
    protected Identifier getPrimaryId() {
        // Default ID or throw an exception if not overridden
        throw new UnsupportedOperationException("Subclasses must override getPrimaryId() to provide a specific modifier ID.");
    }
    protected Identifier getSecondaryId() {
        throw new UnsupportedOperationException("Subclasses must override getSecondaryId() to provide a specific modifier ID.");
    }
}

