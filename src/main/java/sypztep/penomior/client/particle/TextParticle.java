package sypztep.penomior.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sypztep.penomior.client.particle.util.Easing;

import java.awt.*;

public class TextParticle extends Particle {
    private String text;
    public TextParticle(ClientWorld world, Vec3d pos, Vec3d velocity) {
        super(world, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);
        velocityMultiplier = 0.99F;
        gravityStrength = 0.85F;
        maxAge = 32;
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        float textSizeScale = -0.045F; // Change this value to adjust text size
        float particleX = (float) (prevPosX + (x - prevPosX) * (double) tickDelta - cameraPos.x);
        float particleY = (float) (prevPosY + (y - prevPosY) * (double) tickDelta - cameraPos.y);
        float particleZ = (float) (prevPosZ + (z - prevPosZ) * (double) tickDelta - cameraPos.z);

        Matrix4f matrix = new Matrix4f();
        matrix = matrix.translation(particleX, particleY, particleZ);
        matrix = matrix.rotate(camera.getRotation());
        matrix = matrix.rotate((float) Math.PI, 0.0F, 1.0F, 0.0F);
        matrix = matrix.scale(textSizeScale, textSizeScale, textSizeScale);

        MinecraftClient client = MinecraftClient.getInstance();
        var textRenderer = client.textRenderer;
        var vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();

        float textX = textRenderer.getWidth(text) / -2.0F;
        float textY = 0.0F;

        int textColor = new Color(red, green, blue, alpha).getRGB();

        matrix.translate(new Vector3f(0.0f, 0.0f, 0.03f));
        textRenderer.draw(text, textX + 1, textY, 0, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        matrix.translate(new Vector3f(0.0f, 0.0f, 0.03f));
        textRenderer.draw(text, textX - 1, textY, 0, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        matrix.translate(new Vector3f(0.0f, 0.0f, 0.03f));
        textRenderer.draw(text, textX, textY + 1, 0, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        matrix.translate(new Vector3f(0.0f, 0.0f, 0.03f));
        textRenderer.draw(text, textX, textY - 1, 0, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        matrix.translate(new Vector3f(0.0f, 0.0f, 0.03f));
        textRenderer.draw(text, textX, textY, textColor, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        vertexConsumers.draw();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }
}
