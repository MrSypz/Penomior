package sypztep.penomior.mixin.refinerank.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.util.DrawContextUtils;
import sypztep.penomior.common.util.RefineUtil;
import sypztep.tyrannus.common.util.ItemStackHelper;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private MatrixStack matrices;

    @Inject(at = @At("RETURN"), method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    public void drawItemInSlot(TextRenderer textRenderer, ItemStack itemStack, int x, int y, String countOverride, CallbackInfo ci) {
        drawtextInSlot(textRenderer, itemStack, x, y, 1F); // For double scale
    }

    @Unique
    public void drawtextInSlot(TextRenderer renderer, ItemStack stack, int i, int j, float scale) {
        final ClientWorld world = this.client.world;
        if (world == null || stack.isEmpty()) return;
        DrawContext context = ((DrawContext) (Object) this);
        NbtCompound nbt = ItemStackHelper.getNbtCompound(stack, ModDataComponents.PENOMIOR);
        int lvl = nbt.getInt(PenomiorData.REFINE);
        String string = "+" + lvl;
        int stringWidth = renderer.getWidth(string);

        int x = (int) ((i + 9) / scale) - stringWidth / 2;
        int y = (int) ((j + 4) / scale);
        int color = 0xFF4F00;
        int bordercolor = 0;

        this.matrices.push();
        this.matrices.scale(scale, scale, scale);
        this.matrices.translate(0.0F, 0.0F, 180.0F);
        if (stack.contains(ModDataComponents.PENOMIOR)) {
            if (lvl < 16 && lvl > 0)
                DrawContextUtils.drawBoldText(context, renderer, string, x, y, color, bordercolor);
             else {
                String romanString = RefineUtil.romanRefineMap.getOrDefault(lvl, "");
                int romanStringWidth = renderer.getWidth(romanString);
                int romanX = (int) ((i + 9) / scale) - romanStringWidth / 2;
                DrawContextUtils.drawBoldText(context, renderer, romanString, romanX, y, color, bordercolor);
            }
        }
        this.matrices.pop();
    }
}
