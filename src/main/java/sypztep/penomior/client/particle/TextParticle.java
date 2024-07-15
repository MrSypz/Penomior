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
    private float scale;
    private float maxSize;

    public TextParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
        this.maxAge = 20;
        this.scale = 0.0F;
        this.maxSize = - 0.045F;
    }

    public Particle scale(float scale) {
        this.scale *= scale;
        return super.scale(scale);
    }

    public void setMaxSize(float maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void setColor(float red, float green, float blue) {
        super.setColor(red, green, blue);
    }
    public void setColor(int red, int green, int blue) {
        float redFloat = red / 255.0f;
        float greenFloat = green / 255.0f;
        float blueFloat = blue / 255.0f;
        super.setColor(redFloat, greenFloat, blueFloat);
    }

    @Override
    public void tick() {
        Easing ELASTIC_OUT = new Easing.ElasticOut();
        if (this.age++ <= 10) {
            this.scale = MathHelper.lerp(ELASTIC_OUT.ease((float)this.age / 10.0F, 0.0F, 1.0F, 1.0F), 0.0F, this.maxSize);
        } else {
            this.scale = MathHelper.lerp(Easing.CUBIC_IN.ease(((float)this.age - 10.0F) / 10.0F, 0.0F, 1.0F, 1.0F), this.maxSize, 0.0F);
        }
        if (this.age > this.maxAge && this.scale >= 0) {
            this.markDead();
        }
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        float textSizeScale = scale;
        float particleX = (float) (prevPosX + (x - prevPosX) * (double) tickDelta - cameraPos.x);
        float particleY = (float) (prevPosY + (y - prevPosY) * (double) tickDelta - cameraPos.y);
        float particleZ = (float) (prevPosZ + (z - prevPosZ) * (double) tickDelta - cameraPos.z);

        Matrix4f matrix = new Matrix4f();
        matrix = matrix.translation(particleX, particleY, particleZ);
        matrix = matrix.rotate(camera.getRotation());
        matrix = matrix.rotate((float) Math.PI, 0.0F, 1.0F, 0.0F);
        matrix = matrix.scale(textSizeScale, textSizeScale, textSizeScale);

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
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
