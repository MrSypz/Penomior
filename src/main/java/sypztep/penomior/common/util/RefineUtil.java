package sypztep.penomior.common.util;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.apache.commons.lang3.mutable.MutableBoolean;
import sypztep.penomior.client.payload.AddRefineSoundPayloadS2C;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.data.PenomiorItemEntry;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.init.ModItems;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.*;

public final class RefineUtil {
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
    public static void setExtraDamage(ItemStack stack, int refinelvl, int maxLvl, int startExtraDamage, int endExtraDamage) {
        int refine = refineValue(refinelvl, maxLvl, startExtraDamage, endExtraDamage);
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.DAMAGE, refine)));
    }
    public static void setExtraProtect(ItemStack stack, int refinelvl, int maxLvl, int startProtect, int endProtect) {
        int refine = refineValue(refinelvl, maxLvl, startProtect, endProtect);
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.PROTECTION, refine)));
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
    public static int getExtraDamage(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponents.PENOMIOR).getInt(PenomiorData.DAMAGE);
    }
    public static int getExtraProtect(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponents.PENOMIOR).getInt(PenomiorData.PROTECTION);
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
    public static List<ItemStack> getItemStackFromAllEquippedSlots(LivingEntity living) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = living.getEquippedStack(slot);
            if (!itemStack.isEmpty() && !RefineUtil.isBroken(itemStack) && isItemInCorrectSlot(itemStack, slot)) {
                itemStacks.add(itemStack);
            }
        }
        return itemStacks;
    }


    private static boolean isItemInCorrectSlot(ItemStack stack, EquipmentSlot slot) {
        for (AttributeModifierSlot attributeModifierSlot : AttributeModifierSlot.values()) {
            MutableBoolean mutableBoolean = new MutableBoolean(false);
            if (!stack.isOf(Items.SHIELD)) {
                stack.applyAttributeModifier(attributeModifierSlot, (entityAttributeRegistryEntry, attributeModifier) -> {
                    if (attributeModifierSlot.matches(slot))
                        mutableBoolean.setTrue();
                });
                if (mutableBoolean.isTrue()) {
                    return true;
                }
            } else return true;
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
        // Retrieve the item ID from the ItemStack
        String itemID = PenomiorItemEntry.getItemId(stack);
        // Retrieve PenomiorItemEntry data using the item ID
        Optional<PenomiorItemEntry> itemDataOpt = PenomiorItemEntry.getPenomiorItemData(itemID);
        // If item data is present, update the item attributes
        itemDataOpt.ifPresent(itemData -> {
            int maxLvl = itemData.maxLvl();
            int startAccuracy = itemData.startAccuracy();
            int endAccuracy = itemData.endAccuracy();
            int startEvasion = itemData.startEvasion();
            int endEvasion = itemData.endEvasion();
            int maxDurability = itemData.maxDurability();
            int startDamage = itemData.starDamage();
            int endDamage = itemData.endDamage();
            int startProtect = itemData.startProtection();
            int endProtect = itemData.endProtection();

            // Update item attributes using RefineUtil
            RefineUtil.setRefineLvl(stack, refineLvl);
            RefineUtil.setAccuracy(stack, refineLvl, maxLvl, startAccuracy, endAccuracy);
            RefineUtil.setEvasion(stack, refineLvl, maxLvl, startEvasion, endEvasion);
            RefineUtil.setDurability(stack, maxDurability);
            RefineUtil.setExtraDamage(stack, refineLvl, maxLvl, startDamage, endDamage);
            RefineUtil.setExtraProtect(stack, refineLvl, maxLvl, startProtect, endProtect);
        });
    }
    public static void setBroken(ItemStack stack) {
        if (stack.get(ModDataComponents.PENOMIOR) != null)
            RefineUtil.setDurability(stack,0);
    }
    public static boolean isBroken(ItemStack stack) {
        return (stack.get(ModDataComponents.PENOMIOR) != null && RefineUtil.getDurability(stack) <= 0);
    }

    public static void initializeItemData(ItemStack stack, PenomiorItemEntry itemData) {
        if (stack.get(ModDataComponents.PENOMIOR) == null) {
            RefineUtil.setRefineLvl(stack, 0);
            RefineUtil.setDurability(stack, itemData.maxDurability());
        }
    }
    public static void processRefinement(ItemStack slotOutput, int failStack, int currentRefineLvl, int maxLvl, int startAccuracy, int endAccuracy, int startEvasion, int endEvasion, int startDamage, int endDamage, int startProtect, int endProtect, ServerPlayerEntity serverPlayer, PlayerEntity player) {
        if (RefineUtil.handleRefine(slotOutput, failStack)) { // Random Success Rate
            handleSuccess(slotOutput, currentRefineLvl, maxLvl, startAccuracy, endAccuracy, startEvasion, endEvasion, startDamage, endDamage, startProtect, endProtect, serverPlayer, player);
        } else {
            handleFailure(slotOutput, failStack, currentRefineLvl, maxLvl, startAccuracy, endAccuracy, startEvasion, endEvasion, startDamage, endDamage, startProtect, endProtect, serverPlayer, player);
        }
//        this.decrementStack();
    }

    public static  void handleSuccess(ItemStack slotOutput, int currentRefineLvl, int maxLvl, int startAccuracy, int endAccuracy, int startEvasion, int endEvasion, int startDamage, int endDamage, int startProtect, int endProtect, ServerPlayerEntity serverPlayer, PlayerEntity player) {
        int newRefineLvl = currentRefineLvl + 1;
        RefineUtil.setRefineLvl(slotOutput, newRefineLvl);
        updateStats(slotOutput, newRefineLvl, maxLvl, startAccuracy, endAccuracy, startEvasion, endEvasion, startDamage, endDamage, startProtect, endProtect);
        RefineUtil.successRefine(player);
        AddRefineSoundPayloadS2C.send(serverPlayer, player.getId(), RefineUtil.RefineSound.SUCCESS.select());
    }

    public static  void handleFailure(ItemStack slotOutput, int failStack, int currentRefineLvl, int maxLvl, int startAccuracy, int endAccuracy, int startEvasion, int endEvasion, int startDamage, int endDamage, int startProtect, int endProtect, ServerPlayerEntity serverPlayer, PlayerEntity player) {
        if (currentRefineLvl > 16) { // 17 - 20
            int newRefineLvl = Math.max(currentRefineLvl - 1, 0);
            RefineUtil.setRefineLvl(slotOutput, newRefineLvl);
            updateStats(slotOutput, newRefineLvl, maxLvl, startAccuracy, endAccuracy, startEvasion, endEvasion, startDamage, endDamage, startProtect, endProtect);
        }
        int newDurability = Math.max(RefineUtil.getDurability(slotOutput) - 10, 0);
        RefineUtil.setDurability(slotOutput, newDurability);
        RefineUtil.failRefine(player, failStack);
        AddRefineSoundPayloadS2C.send(serverPlayer, player.getId(), RefineUtil.RefineSound.FAIL.select());
    }

    public static void updateStats(ItemStack slotOutput, int refineLvl, int maxLvl, int startAccuracy, int endAccuracy, int startEvasion, int endEvasion, int startDamage, int endDamage, int startProtect, int endProtect) {
        RefineUtil.setEvasion(slotOutput, refineLvl, maxLvl, startEvasion, endEvasion);
        RefineUtil.setAccuracy(slotOutput, refineLvl, maxLvl, startAccuracy, endAccuracy);
        RefineUtil.setExtraDamage(slotOutput, refineLvl, maxLvl, startDamage, endDamage);
        RefineUtil.setExtraProtect(slotOutput, refineLvl, maxLvl, startProtect, endProtect);
    }

    public static void processRepair(ItemStack materialInput, ItemStack slotOutput,int maxDurability, int durability, int repairPoint, ServerPlayerEntity serverPlayer, PlayerEntity player) {
        if (durability >= 100) {
            return; // No need to repair if durability is already at or above 100
        }
        if (materialInput.isOf(ModItems.MOONLIGHT_CRESCENT)) {
            repairItem(slotOutput,maxDurability, durability, repairPoint, serverPlayer, player);
        } else if (materialInput.isOf(slotOutput.getItem())) {
            repairItem(slotOutput,maxDurability, durability, 10, serverPlayer, player);
        }
    }

    public static  void repairItem(ItemStack slotOutput,int maxDurability, int durability, int repairPoint, ServerPlayerEntity serverPlayer, PlayerEntity player) {
        int newDurability = Math.min(durability + repairPoint, maxDurability);
        RefineUtil.setDurability(slotOutput, newDurability);
        AddRefineSoundPayloadS2C.send(serverPlayer, player.getId(), RefineUtil.RefineSound.REPAIR.select());
//        decrementStack();
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
