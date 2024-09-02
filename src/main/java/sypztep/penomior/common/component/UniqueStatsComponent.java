package sypztep.penomior.common.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.PlayerStats;

import java.text.DecimalFormat;

public class UniqueStatsComponent implements AutoSyncedComponent {
    private final PlayerEntity obj;
    private final PlayerStats playerStats; // Add PlayerStats
    private int failstack;

    public UniqueStatsComponent(PlayerEntity obj) {
        this.obj = obj;
        this.playerStats = new PlayerStats();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        failstack = tag.getInt("Failstack");
        playerStats.readFromNbt(tag);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Failstack", failstack);
        playerStats.writeToNbt(tag);
    }
    public PlayerStats getPlayerStats() {
        return playerStats;
    }
    public void addExperience(int amount) { // sync correctly
        playerStats.getLevelSystem().addExperience(amount);
        sync();
    }
    public void setStatPoints(int amount) {
        playerStats.getLevelSystem().setStatPoints(amount);
        sync();
    }
    public int getLevel() {
        return playerStats.getLevelSystem().getLevel();
    }
    public int getNextLevel() {
        return getLevel() + 1;
    }
    public String getXpPercentage() {
        DecimalFormat df = new DecimalFormat("0.00"); // Format to two decimal places
        return df.format(playerStats.getLevelSystem().getXpPercentage()) + "%";
    }
    public int getXp() {
        return playerStats.getLevelSystem().getXp();
    }
    public int getNextXpLevel() {
        return playerStats.getLevelSystem().getXpToNextLevel();
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
