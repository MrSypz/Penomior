package sypztep.penomior.common.component;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.CombatUtils;

public class StatsComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final PlayerEntity obj;
    int accuracy, evasion;
    boolean debug;

    public StatsComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        accuracy = tag.getInt("Accuracy");
        evasion = tag.getInt("Evasion");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Accuracy", accuracy);
        tag.putInt("Evasion", evasion);
    }

    @Override
    public void tick() {
        debug = obj.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.IRON_HELMET);
    }

    @Override
    public void serverTick() {
        tick();
        if (!obj.getWorld().isClient()) {
            if (debug) {
                setEvasion(10);
            } else
                setEvasion(0);
        }
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public int getEvasion() {
        return this.evasion;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
        ModEntityComponents.STATS.sync(this.obj); // Sync the component
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        ModEntityComponents.STATS.sync(this.obj);
    }
    public double getHitRate() {
        return CombatUtils.calculateHitRate(accuracy);
    }

    public double getEvasionRate() {
        return CombatUtils.calculateEvasionRate(evasion);
    }
}
