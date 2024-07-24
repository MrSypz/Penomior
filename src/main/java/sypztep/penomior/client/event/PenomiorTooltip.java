package sypztep.penomior.client.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.crital.common.data.CritalData;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.util.RefineUtil;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.List;

public class PenomiorTooltip implements ItemTooltipCallback {
    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
        if (stack.get(ModDataComponents.PENOMIOR) != null) {
            if (Penomior.isCritalLoaded && ItemStackHelper.getNbtCompound(stack).contains(CritalData.TIER_FLAG))
                return;

            if (RefineUtil.isBroken(stack))
                lines.add(Text.literal("Broken ✗").formatted(Formatting.RED));
            else lines.add(Text.literal("Can Refine ✔").formatted(Formatting.GREEN));
            int accuracy = RefineUtil.getAccuracy(stack);
            int evasion = RefineUtil.getEvasion(stack);
            if (accuracy > 0)
                lines.add(Text.literal(" ▶ ").formatted(Formatting.GOLD)
                        .append(Text.literal(" Accuracy: " + accuracy).formatted(Formatting.GRAY)));
            if (evasion > 0)
                lines.add(Text.literal(" ▶ ").formatted(Formatting.GOLD)
                        .append(Text.literal(" Evasion: " + evasion).formatted(Formatting.GRAY)));
        }
    }
}
