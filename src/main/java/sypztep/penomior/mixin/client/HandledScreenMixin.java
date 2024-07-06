package sypztep.penomior.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.util.RefineUtil;
import sypztep.tyrannus.common.util.ItemStackHelper;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "drawSlot",at = @At("TAIL"))
    private void drawText(DrawContext context, Slot slot, CallbackInfo ci){
        int i = slot.x;
        int j = slot.y;
        ItemStack itemStack = slot.getStack();
        MinecraftClient client = MinecraftClient.getInstance();
        drawtextInSlot(context,client.textRenderer,itemStack,i,j);
    }
    @Unique
    public void drawtextInSlot(DrawContext context, TextRenderer textRenderer, ItemStack stack, int x, int y) {
        if (stack.isEmpty()) {
            return;
        }
        NbtCompound nbt = ItemStackHelper.getNbtCompound(stack,ModDataComponents.PENOMIOR);
        context.getMatrices().push();
        if (stack.contains(ModDataComponents.PENOMIOR)) {
            int anInt = nbt.getInt(PenomiorData.REFINE);
            context.getMatrices().translate(0.0f, 0.0f, 260.0f);
            if (anInt < 16 && anInt > 0)
                context.drawText(textRenderer, String.valueOf(anInt), x + 10 - textRenderer.getWidth(String.valueOf(anInt)), y + 5 , 0xFFFFFF, true);
            else context.drawText(textRenderer, RefineUtil.romanRefineMap.get(anInt), x + textRenderer.getWidth(String.valueOf(anInt)) - 7,y + 5,0xFFFFFF, true);
        }
        context.getMatrices().pop();
    }
}
