package sypztep.penomior.common.stats;

import net.minecraft.nbt.NbtCompound;

public class LevelSystem {
    private int level;
    private int xp;
    private int xpToNextLevel;
    private int statPoints;
    private static final int BASE_XP = 100; // Base XP for level 1
    private static final double EXPONENT = 1.5; // Exponent for XP curve

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
        xp += amount;
        while (xp >= xpToNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        xp -= xpToNextLevel;
        level++;
        statPoints += 1; // Or add custom points
        xpToNextLevel = calculateXpForNextLevel(level);
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

    public int getStatPoints() {
        return statPoints;
    }

    public void useStatPoint(StatTypes types, int points, PlayerStats playerStats) {
        if (statPoints >= points) {
            statPoints -= points;
            playerStats.allocatePoints(types, points);
        }
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
