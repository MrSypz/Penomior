package sypztep.penomior.common.util;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.apache.commons.lang3.mutable.MutableBoolean;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.data.PenomiorItemData;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.*;

public class RefineUtil {
    public static Map<Integer, String> romanRefineMap = new HashMap<>();
    public static Map<Integer, SoundEvent> soundEventsMap = new HashMap<>();
    private static final double[] normalSuccessRates = {
            100.0, 95.0, 90.0, 80.0, 70.0, 60.0, 50.0, 40.0, 30.0, 20.0, 10.0,
            9.0, 8.0, 7.0, 6.0, 5.0, // Levels 1-15
            25.0, // PRI (16)
            17.5, // DUO (17)
            12.5, // TRI (18)
            7.5,  // TET (19)
            2.5   // PEN (20)
    };
    private static final double SUCCESS_RATE_CAP = 90.0; // Cap for success rate
    private static double successRate;
    //------------set-----------//

    public static void setRefineLvl(ItemStack stack, int refinelvl) {
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.REFINE, refinelvl)));
    }

    public static void setAccuracy(ItemStack stack, int refinelvl, int maxLvl, int startAccuracy, int endAccuracy) {
        int refine = refineValue(refinelvl, maxLvl, startAccuracy, endAccuracy);
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.ACCURACY, refine)));
    }

    public static void setEvasion(ItemStack stack, int refinelvl, int maxLvl, int startEvasion, int endEvasion) {
        int refine = refineValue(refinelvl, maxLvl, startEvasion, endEvasion);
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.EVASION, refine)));
    }

    public static void setDurability(ItemStack stack, int durability) {
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.DURABILITY, durability)));
    }

    public static double getSuccessRate() {
        return successRate;
    }

    private static void setSuccessRate(double v) {
        successRate = v;
    }

    //------------get-----------//
    public static int getRefineLvl(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponents.PENOMIOR).getInt(PenomiorData.REFINE);
    }

    public static int getAccuracy(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponents.PENOMIOR).getInt(PenomiorData.ACCURACY);
    }

    public static int getEvasion(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponents.PENOMIOR).getInt(PenomiorData.EVASION);
    }

    public static int getDurability(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponents.PENOMIOR).getInt(PenomiorData.DURABILITY);
    }

    //------------excute-------------//
    public static int refineValue(int currentLvl, int maxLvl, int startValue, int endValue) {
        if (currentLvl < 0 || currentLvl > maxLvl)
            throw new IllegalArgumentException("Input value out of range");
        int outputRange = endValue - startValue;

        double normalizedInput = (double) (currentLvl) / maxLvl;
        double curvedInput = Math.pow(normalizedInput, 1.725);

        return (int) (startValue + curvedInput * outputRange);
    }

    public static boolean handleRefine(ItemStack slotOutput, int failStack) {
        double successRate = calculateSuccessRate(slotOutput, failStack);
        Random random = new Random();
        double randomValue = random.nextDouble();
        return randomValue < successRate;
    }

    public static double calculateSuccessRate(ItemStack slotOutput, int failStack) {
        int currentRefineLvl = getRefineLvl(slotOutput);
        if (currentRefineLvl >= normalSuccessRates.length)
            return 0.0; // or handle appropriately for out-of-bounds condition

        double baseSuccessRate = normalSuccessRates[currentRefineLvl] * 0.01;
        double successRate = baseSuccessRate + (failStack * (baseSuccessRate * 0.1));
        successRate = Math.min(successRate, SUCCESS_RATE_CAP * 0.01);

        return successRate;
    }

    public static List<NbtCompound> getNbtFromAllEquippedSlots(LivingEntity living) {
        List<NbtCompound> nbtList = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = living.getEquippedStack(slot);
            if (!itemStack.isEmpty() && !RefineUtil.isBroken(itemStack) && isItemInCorrectSlot(itemStack, slot)) {
                nbtList.add(ItemStackHelper.getNbtCompound(itemStack, ModDataComponents.PENOMIOR));
            }
        }
        return nbtList;
    }

    private static boolean isItemInCorrectSlot(ItemStack stack, EquipmentSlot slot) {
        for (AttributeModifierSlot attributeModifierSlot : AttributeModifierSlot.values()) {
            MutableBoolean mutableBoolean = new MutableBoolean(false);
            stack.applyAttributeModifier(attributeModifierSlot, (entityAttributeRegistryEntry, attributeModifier) -> {
                if (attributeModifierSlot.matches(slot))
                    mutableBoolean.setTrue();
            });
            if (mutableBoolean.isTrue()) {
                return true;
            }
        }
        return false;
    }

    public static void getCalculateSuccessRate(ItemStack slotOutput, int failStack) {
        setSuccessRate(calculateSuccessRate(slotOutput, failStack));
    }

    public static void successRefine(PlayerEntity player) {
        ModEntityComponents.STATS.get(player).setFailstack(0);
    }

    public static void failRefine(PlayerEntity player, int failstack) {
        ModEntityComponents.STATS.get(player).setFailstack(failstack + 1);
    }

    //------------write-data-----------// from craft item
    public static void writeRefineData(ItemStack stack, int refineLvl) {
        String itemID = PenomiorItemData.getItemId(stack);
        PenomiorItemData itemData = PenomiorItemData.getPenomiroItemData(itemID);
        if (itemData != null) {
            if (itemID.equals(itemData.itemID())) {
                int maxLvl = itemData.maxLvl();
                int startAccuracy = itemData.startAccuracy();
                int endAccuracy = itemData.endAccuracy();
                int startEvasion = itemData.startEvasion();
                int endEvasion = itemData.endEvasion();
                int maxDurability = itemData.maxDurability();
                RefineUtil.setRefineLvl(stack, refineLvl);
                RefineUtil.setAccuracy(stack, refineLvl, maxLvl, startAccuracy, endAccuracy);
                RefineUtil.setEvasion(stack, refineLvl, maxLvl, startEvasion, endEvasion);
                RefineUtil.setDurability(stack, maxDurability);
            }
        }
    }

    public static boolean isBroken(ItemStack stack) {
        return (stack.get(ModDataComponents.PENOMIOR) != null && RefineUtil.getDurability(stack) <= 0);
    }

    public static void initializeItemData(ItemStack stack, PenomiorItemData itemData) {
        if (stack.get(ModDataComponents.PENOMIOR) == null) {
            RefineUtil.setRefineLvl(stack, 0);
            RefineUtil.setDurability(stack, itemData.maxDurability());
        }
    }

    static {
        romanRefineMap.put(16, "I");
        romanRefineMap.put(17, "II");
        romanRefineMap.put(18, "III");
        romanRefineMap.put(19, "IV");
        romanRefineMap.put(20, "V");

        soundEventsMap.put(0, SoundEvents.ENTITY_ITEM_BREAK);
        soundEventsMap.put(1, SoundEvents.ITEM_TRIDENT_HIT_GROUND);
        soundEventsMap.put(2, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE);
    }

    public enum RefineSound {
        FAIL(0),
        SUCCESS(1),
        REPAIR(2);

        private final int select;

        RefineSound(int select) {
            this.select = select;
        }

        public int select() {
            return select;
        }
    }
}
