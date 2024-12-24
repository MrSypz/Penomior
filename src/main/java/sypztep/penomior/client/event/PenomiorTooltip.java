package sypztep.penomior.client.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.util.RefineUtil;

import java.util.List;

public final class PenomiorTooltip implements ItemTooltipCallback {

    @Override
    public void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
//        if (stack.get(ModDataComponents.PENOMIOR) != null) {
//        โค้ดเก่า TODO: หาวิธี ทำให้มี Component แยกเป็นของตัวเอง

            if (stack.getNbt() != null) {
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
