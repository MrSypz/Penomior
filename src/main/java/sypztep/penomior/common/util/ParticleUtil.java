package sypztep.penomior.common.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import sypztep.penomior.client.particle.TextParticle;

import java.awt.*;

public class ParticleUtil {

    private static void spawnParticle(Entity target, String text, Color color, float maxSize,float yPos) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        if (world == null || !world.isClient()) return;

        Vec3d particlePos = target.getPos().add(0.0, target.getHeight() + 0.45 + yPos, 0.0);
        TextParticle particle = new TextParticle(world, particlePos.x, particlePos.y, particlePos.z);
        particle.setText(text);
        particle.setColor(color.getRed(), color.getGreen(), color.getBlue());
        particle.setMaxSize(maxSize);
        client.particleManager.addParticle(particle);
    }
    /*
    Spawn particle should be done in client side
     */
    public static void spawnTextParticle(Entity target, Text text, Color color, float maxSize,float yPos) {
        if (target.getWorld().isClient())
            spawnParticle(target, text.getString(), color, maxSize,yPos);
    }
    /*
    Spawn particle should be done in client side
     */
    public static void spawnTextParticle(Entity target, Text text, Color color, float maxSize) {
        if (target.getWorld().isClient())
            spawnParticle(target, text.getString(), color, maxSize,0);
    }
    public static void spawnTextParticle(Entity target, Text text, float maxSize) {
        if (target.getWorld().isClient())
            spawnParticle(target, text.getString(), new Color(255, 255, 255), maxSize,0);
    }
    public static void spawnTextParticle(Entity target, Text text) {
        if (target.getWorld().isClient())
            spawnParticle(target, text.getString(), new Color(255,255,255), - 0.045F,0);
    }
}


