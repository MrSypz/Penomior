package sypztep.penomior.common.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import sypztep.penomior.common.init.ModEntityComponents;

public class StatsComponent implements AutoSyncedComponent {
    private final LivingEntity obj;
    private int accuracy, evasion;
    private int extraAccuracy,extraEvasion; // for stats UI

    public StatsComponent(LivingEntity obj) {
        this.obj = obj;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        accuracy = tag.getInt("Accuracy");
        evasion = tag.getInt("Evasion");
        extraEvasion = tag.getInt("Extra Evasion");
        extraAccuracy = tag.getInt("Extra Accuracy");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Accuracy", accuracy);
        tag.putInt("Evasion", evasion);
        tag.putInt("Extra Evasion", extraEvasion);
        tag.putInt("Extra Accuracy", extraAccuracy);
    }

    public int getAccuracy() {
        return this.accuracy + this.extraAccuracy;
    }

    public int getEvasion() {
        return this.evasion + this.extraEvasion;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
        sync();
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        sync();
    }
    public void addExtraEvasion(int evasion) {
        this.extraEvasion += evasion;
        sync();
    }
    public void addExtraAccuracy(int accuracy) {
        this.extraAccuracy += accuracy;
        sync();
    }
    public void resetExtras() {
        this.extraEvasion = 0;
        this.extraAccuracy = 0;
        sync();
    }

    //----------------utility---------------//
    private void sync() {
        ModEntityComponents.STATS.sync(this.obj);
    }
}
