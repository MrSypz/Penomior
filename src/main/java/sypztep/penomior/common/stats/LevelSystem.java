package sypztep.penomior.common.stats;

import net.minecraft.nbt.NbtCompound;
import sypztep.penomior.ModConfig;

public class LevelSystem {
    private int level;
    private int xp;
    private int xpToNextLevel;
    private int statPoints;
    private static final int BASE_XP = ModConfig.baseExp; // Base XP for level 1
    private static final double EXPONENT = ModConfig.exponentExp; // Exponent for XP curve
    private static final int MAX_LEVEL = ModConfig.maxLevel; // Maximum level cap

    public LevelSystem() {
        this.level = 1;
        this.xp = 0;
        this.statPoints = ModConfig.startStatpoints;
        this.xpToNextLevel = calculateXpForNextLevel(level);
    }

    private int calculateXpForNextLevel(int level) {
        return (int) (BASE_XP * Math.pow(level, EXPONENT));
    }

    public void addExperience(int amount) {
        if (level >= MAX_LEVEL) {
            // Optionally, handle the case where XP is added but the level cap is reached
            return;
        }
        xp += amount;
        while (xp >= xpToNextLevel && level < MAX_LEVEL) {
            levelUp();
        }
    }
    public void subtractExperience(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to subtract cannot be negative");
        }
        xp = Math.max(0, xp - amount); // Ensure XP doesn't go below 0// Handle level adjustment if necessarywhile (xp < xpToNextLevel && level > 1) {
    }
    public void setXp(int xp) {
        this.xp = xp;
    }

    private void levelUp() {
        if (level >= MAX_LEVEL) {
            return; // Prevent leveling up if at max level
        }
        xp -= xpToNextLevel;
        level++;
        statPoints += getStatPointsForLevel(level);
        if (level < MAX_LEVEL) {
            xpToNextLevel = calculateXpForNextLevel(level);
        }
    }
    private int getStatPointsForLevel(int level) {
        if (level - 1 < ModConfig.statPointLadder.length) {
            return ModConfig.statPointLadder[level - 1];
        }
        return  ModConfig.statPointLadder[ ModConfig.statPointLadder.length - 1];
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }
    public double getXpPercentage() {
        if (this.xpToNextLevel == 0) {
            return 0; // To avoid division by zero
        }
        return ((double) this.xp / this.xpToNextLevel) * 100;
    }

    public int getStatPoints() {
        return statPoints;
    }

    public void setStatPoints(int statPoints) {
        this.statPoints = statPoints;
    }
    public void addStatPoints(int statPoints) {
        this.statPoints += statPoints;
    }
    public void subtractStatPoints(int points) {
        this.statPoints -= points;
    }
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Level", level);
        tag.putInt("XP", xp);
        tag.putInt("XPToNextLevel", xpToNextLevel);
        tag.putInt("StatPoints", statPoints);
    }

    public void readFromNbt(NbtCompound tag) {
        this.level = tag.getInt("Level");
        this.xp = tag.getInt("XP");
        this.xpToNextLevel = tag.getInt("XPToNextLevel");
        this.statPoints = tag.getInt("StatPoints");
    }
}
