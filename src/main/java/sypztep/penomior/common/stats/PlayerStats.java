package sypztep.penomior.common.stats;

import net.minecraft.nbt.NbtCompound;

import java.util.EnumMap;
import java.util.Map;

public class PlayerStats {
    private final Map<StatTypes, Stat> stats;
    private final LevelSystem levelSystem;

    public PlayerStats() {
        stats = new EnumMap<>(StatTypes.class);
        levelSystem = new LevelSystem();
        for (StatTypes statType : StatTypes.values()) {
            stats.put(statType, statType.createStat(1));
        }
    }

    public Stat getStat(StatTypes statType) {
        return stats.get(statType);
    }

    public void allocatePoints(StatTypes statType, int points) {
        Stat stat = getStat(statType);
        if (stat != null) {
            stat.increase(points);
        }
    }

    public void resetStats() {
        stats.values().forEach(Stat::reset);
        // Optionally, reset level system or other components

    }

    public Map<StatTypes, Stat> getAllStats() {
        return stats;
    }
    public void addExperience(int amount) {
        levelSystem.addExperience(amount);
    }

    public int getLevel() {
        return levelSystem.getLevel();
    }

    public int getXp() {
        return levelSystem.getXp();
    }

    public int getXpToNextLevel() {
        return levelSystem.getXpToNextLevel();
    }

    public int getStatPoints() {
        return levelSystem.getStatPoints();
    }

    public void useStatPoint(StatTypes statType, int points) {
        levelSystem.useStatPoint(statType, points, this);
    }

    //------------------------utility---------------------
    public void writeToNbt(NbtCompound tag) {
        stats.forEach((type, stat) -> {
            NbtCompound statTag = new NbtCompound();
            stat.writeToNbt(statTag);
            tag.put(type.name(), statTag);
        });
        levelSystem.writeToNbt(tag);
    }

    public void readFromNbt(NbtCompound tag) {
        stats.forEach((type, stat) -> stat.readFromNbt(tag.getCompound(type.name())));
        levelSystem.readFromNbt(tag);
    }
}

