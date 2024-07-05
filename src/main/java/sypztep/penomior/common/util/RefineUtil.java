package sypztep.penomior.common.util;

import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.HashMap;
import java.util.Map;

public class RefineUtil {
    public static Map<Integer, String> refineMap = new HashMap<>();

    //------------set-----------//

    public static void setRefineLvl(ItemStack stack, int refinelvl) {
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.REFINE, refinelvl)));
    }

    public static void setAccuracy(ItemStack stack, int refinelvl, int maxLvl, int startAccuracy, int endAccuracy) {
        int refine = refineValue(refinelvl, maxLvl, startAccuracy, endAccuracy);
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.ACCURACY, refine)));
    }

    public static void setEvasion(ItemStack stack, int refinelvl, int maxLvl, int startAccuracy, int endAccuracy) {
        int refine = refineValue(refinelvl, maxLvl, startAccuracy, endAccuracy);
        stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, applied -> applied.apply(compound -> compound.putInt(PenomiorData.EVASION, refine)));
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

    public static int refineValue(int currentLvl, int maxLvl, int startAccuracy, int endAccuracy) {
        if (currentLvl < 0 || currentLvl > maxLvl) {
            throw new IllegalArgumentException("Input value out of range");
        }
        int outputRange = endAccuracy - startAccuracy;

        double normalizedInput = (double) (currentLvl) / maxLvl;
        double curvedInput = Math.pow(normalizedInput, 1.725);

        return (int) (startAccuracy + curvedInput * outputRange);
    }

    static {
        refineMap.put(16, "I");
        refineMap.put(17, "II");
        refineMap.put(18, "III");
        refineMap.put(19, "IV");
        refineMap.put(20, "V");
    }
}
