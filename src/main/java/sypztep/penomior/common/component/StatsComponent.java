package sypztep.penomior.common.component;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.apache.commons.lang3.mutable.MutableInt;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatsComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final LivingEntity obj;
    int accuracy, evasion;
    boolean debug;

    public StatsComponent(LivingEntity obj) {
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

    public List<NbtCompound> getNbtFromAllEquippedSlots() {
        List<NbtCompound> nbtList = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = obj.getEquippedStack(slot);
            if (!itemStack.isEmpty())
                nbtList.add(ItemStackHelper.getNbtCompound(itemStack, ModDataComponents.PENOMIOR));
        }
        return nbtList;
    }
    public int getAccuracyfromEquipment() {
        MutableInt accuracyPoint = new MutableInt();
        for (NbtCompound compounds : getNbtFromAllEquippedSlots())
            accuracyPoint.add(compounds.getInt(PenomiorData.REFINE));
        return accuracyPoint.getValue();
    }

    public int getEvasionfromEquipment() {
        MutableInt evasionPoint = new MutableInt();
        for (NbtCompound compounds : getNbtFromAllEquippedSlots())
            evasionPoint.add(compounds.getInt(PenomiorData.REFINE));
        return evasionPoint.getValue();
    }
}
