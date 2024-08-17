package sypztep.penomior.common.stats;

import net.minecraft.nbt.NbtCompound;

public class LevelSystem {
    private int level;
    private int xp;
    private int xpToNextLevel;
    private int statPoints;
    private static final int BASE_XP = 100; // Base XP for level 1
    private static final double EXPONENT = 1.5; // Exponent for XP curve
    private static final int MAX_LEVEL = 100; // Maximum level cap

    public LevelSystem() {
        this.level = 1;
        this.xp = 0;
        this.statPoints = 0;
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

    private void levelUp() {
        if (level >= MAX_LEVEL) {
            return; // Prevent leveling up if at max level
        }
        xp -= xpToNextLevel;
        level++;
        statPoints += 1;
        if (level < MAX_LEVEL) {
            xpToNextLevel = calculateXpForNextLevel(level);
        }
    }

    public int getLevel() {
        return level;
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
