package sypztep.penomior.common.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import sypztep.penomior.common.init.ModEntityComponents;

public class UniqueStatsComponent implements AutoSyncedComponent {
    private final PlayerEntity obj;
    private int failstack;

    public UniqueStatsComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        failstack = tag.getInt("Failstack");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Failstack", failstack);
    }
    public int getFailstack() {
        return failstack;
    }
    public void setFailstack(int failstack) {
        this.failstack = failstack;
        sync();
    }
    //----------------utility---------------//
    private void sync() {
        ModEntityComponents.STATS.sync(this.obj);
    }
}
