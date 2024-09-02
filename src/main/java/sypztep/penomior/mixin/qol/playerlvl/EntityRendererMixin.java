package sypztep.penomior.mixin.qol.playerlvl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.init.ModEntityComponents;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Shadow
    @Final
    protected EntityRenderDispatcher dispatcher;

    @Inject(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal = 0))
    private void playerLevel(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        float f = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
        int j = (int) (f * 255.0f) << 24;
        boolean bl = !entity.isSneaky();
        TextRenderer textRenderer = this.getTextRenderer();
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        if (entity instanceof PlayerEntity player) {
            Text lvl = Text.literal("Level : " + ModEntityComponents.UNIQUESTATS.get(player).getLevel());
            float g = (float) -textRenderer.getWidth(lvl) / 2;
            textRenderer.draw(lvl, g, (float) -10, Colors.WHITE, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);
        }
    }
}
