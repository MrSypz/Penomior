package sypztep.penomior.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModDataComponents;

public class PenomiorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            NbtCompound nbt = getNbtCompound(stack);
            if (stack.contains(ModDataComponents.PENOMIOR)) {
                int anInt = nbt.getInt(PenomiorData.LVL);
                lines.add(Text.of(String.valueOf(anInt)));
            }
        });
    }
    public void drawItemInSlot(DrawContext context, TextRenderer textRenderer, ItemStack stack, int x, int y) {
        if (stack.isEmpty()) {
            return;
        }
        NbtCompound nbt = getNbtCompound(stack);
        context.getMatrices().push();
        if (stack.contains(ModDataComponents.PENOMIOR)) {
            int anInt = nbt.getInt(PenomiorData.LVL);
            context.getMatrices().translate(0.0f, 0.0f, 200.0f);
            context.drawText(textRenderer, String.valueOf(anInt), x + 19 - 2, y + 6 + 3, 0xFFFFFF, true);
        }
    }
    public static NbtCompound getNbtCompound(ItemStack stack) {
        NbtCompound value = new NbtCompound();
        @Nullable var data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data != null)
            value = data.copyNbt();
        return value;
    }
}
