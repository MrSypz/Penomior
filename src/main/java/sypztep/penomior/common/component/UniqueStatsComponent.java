package sypztep.penomior.common.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.stats.PlayerStats;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.StatTypes;

public class UniqueStatsComponent implements AutoSyncedComponent , CommonTickingComponent {
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
    public void addExperience(int amount) {
        playerStats.addExperience(amount);
        sync();
    }
    public int getLevel() {
        return playerStats.getLevel();
    }
    public int getXp() {
        return playerStats.getXp();
    }
    public int getNextXpLevel() {
        return playerStats.getXpToNextLevel();
    }
    public int getFailstack() {
        return failstack;
    }
    public void setFailstack(int failstack) {
        this.failstack = failstack;
        sync();
    }

    public PlayerEntity getObj() {
        return obj;
    }

    //----------------utility---------------//
    public void sync() {
        ModEntityComponents.UNIQUESTATS.sync(this.obj);
    }

    @Override
    public void tick() {
        if (!obj.getWorld().isClient())
            Penomior.LOGGER.info(String.valueOf(playerStats.getStat(StatTypes.STRENGTH).getValue()));
    }
}
