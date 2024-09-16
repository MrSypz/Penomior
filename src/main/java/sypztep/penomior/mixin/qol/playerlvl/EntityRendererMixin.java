package sypztep.penomior.mixin.qol.playerlvl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void renderLvl(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof LivingEntity)
            renderLvlIfPresent(entity, matrices, vertexConsumers, light, tickDelta);
    }

    @Unique
    protected void renderLvlIfPresent(T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta) {
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (d > 1024.0) {
            return;
        }
        Vec3d vec3d = entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, entity.getYaw(tickDelta));
        if (vec3d == null) {
            return;
        }
        boolean bl = !entity.isSneaky();
        matrices.push();
        matrices.translate(vec3d.x, vec3d.y + 0.5, vec3d.z);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(0.025f, -0.025f, 0.025f);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float f = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
        int j = (int) (f * 255.0f) << 24;
        TextRenderer textRenderer = this.getTextRenderer();
        Text lvl = Text.literal("Level : " + ModEntityComponents.UNIQUESTATS.get(entity).getLevel());
        float g = (float) -textRenderer.getWidth(lvl) / 2;
        textRenderer.draw(lvl, g, (float) -10, Colors.WHITE, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);
        matrices.pop();
    }
}
