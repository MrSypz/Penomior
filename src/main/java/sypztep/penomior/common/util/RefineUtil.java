package sypztep.penomior.common.util;

import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.data.PenomiorItemData;
import sypztep.penomior.common.data.PenomiorItemDataSerializer;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.HashMap;
import java.util.Map;

public class RefineUtil {
    public static Map<Integer, String> romanRefineMap = new HashMap<>();
    private static final double[] normalSuccessRates = {
            100.0, 90.0, 80.0, 70.0, 60.0, 50.0, 40.0, 30.0, 20.0, 10.0,
            9.0, 8.0, 7.0, 6.0, 5.0
    };

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
    public static void setDurability(ItemStack stack, int maxDurability) {
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.DURABILITY, maxDurability)));
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


    public static int refineValue(int currentLvl, int maxLvl, int startAccuracy, int endAccuracy) {
        if (currentLvl < 0 || currentLvl > maxLvl)
            throw new IllegalArgumentException("Input value out of range");
        int outputRange = endAccuracy - startAccuracy;

        double normalizedInput = (double) (currentLvl) / maxLvl;
        double curvedInput = Math.pow(normalizedInput, 1.725);

        return (int) (startAccuracy + curvedInput * outputRange);
    }

    //------------write-data-----------//
    public static void writeRefineData(ItemStack stack, int refineLvl) {
        String itemID = PenomiorItemData.getItemId(stack);
        PenomiorItemData itemData = PenomiorItemDataSerializer.getConfigCache().get(itemID);
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

    static {
        romanRefineMap.put(16, "I");
        romanRefineMap.put(17, "II");
        romanRefineMap.put(18, "III");
        romanRefineMap.put(19, "IV");
        romanRefineMap.put(20, "V");
    }
}
