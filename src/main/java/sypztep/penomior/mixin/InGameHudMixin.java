package sypztep.penomior.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.util.RomanUtil;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderHotbarItem", at = @At("TAIL"))
    private void custom(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        drawtextInSlot(context,client.textRenderer,stack,x,y);
    }
    @Unique
    public void drawtextInSlot(DrawContext context, TextRenderer textRenderer, ItemStack stack, int x, int y) {
        if (stack.isEmpty()) {
            return;
        }
        NbtCompound nbt = getNbtCompound(stack);
        context.getMatrices().push();
        if (stack.contains(ModDataComponents.PENOMIOR)) {
            int anInt = nbt.getInt(PenomiorData.LVL);
            context.getMatrices().translate(0.0f, 0.0f, 200.0f);
            if (anInt < 16)
                context.drawText(textRenderer, String.valueOf(anInt), x + 8 , y + 9 , 0xFFFFFF, true);
            else context.drawText(textRenderer, RomanUtil.customMap.get(anInt), x+8,y+9,0xFFFFFF, true);
        }
        context.getMatrices().pop();
    }
    @Unique
    public NbtCompound getNbtCompound(ItemStack stack) {
        NbtCompound value = new NbtCompound();
        @Nullable var data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data != null)
            value = data.copyNbt();
        return value;
    }
}
