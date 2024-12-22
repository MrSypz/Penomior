package sypztep.penomior.common.stats;

import net.minecraft.nbt.NbtCompound;
import sypztep.penomior.ModConfig;

public class LevelSystem {
    private int level;
    private int xp;
    private int xpToNextLevel;
    private int statPoints;
    private static final int BASE_XP = ModConfig.baseExp;
    private static final double EXPONENT = ModConfig.exponentExp;
    private static final int MAX_LEVEL = ModConfig.maxLevel;

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
        if (level >= MAX_LEVEL && xp >= xpToNextLevel) {
            xp = xpToNextLevel;
            return;
        }
        xp += amount;
        while (xp >= xpToNextLevel && level < MAX_LEVEL)
            levelUp();
        if (level >= MAX_LEVEL)
            xp = Math.min(xp, xpToNextLevel);
    }

    public void subtractExperience(int amount) {
        if (level >= MAX_LEVEL)
            return;
        if (amount < 0)
            throw new IllegalArgumentException("Amount to subtract cannot be negative");
        xp = Math.max(0, xp - amount);
    }
    public void setXp(int xp) {
        this.xp = xp;
    }

    private void levelUp() {
        if (level >= MAX_LEVEL) {
            return;
        }
        xp -= xpToNextLevel;
        level++;
        statPoints += getStatPointsForLevel(level);
        if (level < MAX_LEVEL) {
            updateNextLvl();
        }
    }
    private int getStatPointsForLevel(int level) {
        if (level - 1 < ModConfig.statPointLadder.length) {
            return ModConfig.statPointLadder[level - 1];
        }
        return  ModConfig.statPointLadder[ ModConfig.statPointLadder.length - 1];
    }
    public void updateNextLvl() {
        xpToNextLevel = calculateXpForNextLevel(level);
    }

    public int getMaxLevel() {
        return MAX_LEVEL;
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
            return 0;
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
