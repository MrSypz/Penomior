package sypztep.penomior.common.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.LevelSystem;
import sypztep.penomior.common.stats.LivingStats;

import java.text.DecimalFormat;

public class UniqueStatsComponent implements AutoSyncedComponent {
    private final LivingEntity obj;
    private final LivingStats livingStats;
    private int failstack;

    public UniqueStatsComponent(LivingEntity obj) {
        this.obj = obj;
        this.livingStats = new LivingStats();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        failstack = tag.getInt("Failstack");
        livingStats.readFromNbt(tag);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Failstack", failstack);
        livingStats.writeToNbt(tag);
    }

    public LivingEntity getObj() {
        return obj;
    }

    public LivingStats getLivingStats() {
        return livingStats;
    }
    public void addExperience(int amount) { // sync correctly
        livingStats.getLevelSystem().addExperience(amount);
        sync();
    }
    public void setStatPoints(int amount) {
        livingStats.getLevelSystem().setStatPoints(amount);
        sync();
    }
    public int getLevel() {
        return livingStats.getLevelSystem().getLevel();
    }
    public LevelSystem getLevelSystem() {
        return livingStats.getLevelSystem();
    }
    public int getNextLevel() {
        return getLevel() + 1;
    }
    public String getXpPercentage() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(livingStats.getLevelSystem().getXpPercentage()) + "%";
    }
    public int getXp() {
        return livingStats.getLevelSystem().getXp();
    }
    public int getNextXpLevel() {
        return livingStats.getLevelSystem().getXpToNextLevel();
    }
    public int getFailstack() {
        return failstack;
    }
    public void setFailstack(int failstack) {
        this.failstack = failstack;
        sync();
    }

    //----------------utility---------------//
    public void sync() {
        ModEntityComponents.UNIQUESTATS.sync(this.obj);
    }
}
