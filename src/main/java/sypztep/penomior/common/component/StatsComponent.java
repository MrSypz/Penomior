package sypztep.penomior.common.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import sypztep.penomior.common.init.ModEntityComponents;

public class StatsComponent implements AutoSyncedComponent {
    private final LivingEntity obj;
    int accuracy, evasion, failstack;

    public StatsComponent(LivingEntity obj) {
        this.obj = obj;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        accuracy = tag.getInt("Accuracy");
        evasion = tag.getInt("Evasion");
        failstack = tag.getInt("Failstack");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Accuracy", accuracy);
        tag.putInt("Evasion", evasion);
        tag.putInt("Failstack", failstack);
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public int getEvasion() {
        return this.evasion;
    }

    public int getFailstack() {
        return failstack;
    }

    public void setFailstack(int failstack) {
        this.failstack = failstack;
        sync();
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
        sync();
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        sync();
    }

    //----------------utility---------------//
    private void sync() {
        ModEntityComponents.STATS.sync(this.obj);
    }
}
