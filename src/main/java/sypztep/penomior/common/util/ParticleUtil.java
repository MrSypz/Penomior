package sypztep.penomior.common.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import sypztep.penomior.client.particle.TextParticle;

public class ParticleUtil {
    public static void spawnDamageParticle(LivingEntity target, float amount, boolean attackHits) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        if (world == null || !world.isClient()) return;

        Vec3d particlePos = target.getPos().add(0.0, target.getHeight() + 0.25, 0.0);
        TextParticle particle = new TextParticle(world, particlePos.x, particlePos.y, particlePos.z);

        if (attackHits) {
            String text = String.format("%.1f", amount);
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            particle.setText(text);
        } else {
            particle.setText("Miss");
        }

        client.particleManager.addParticle(particle);
    }

    public static void spawnTextParticle(LivingEntity target, String word) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        if (world == null || !world.isClient()) return;

        Vec3d particlePos = target.getPos().add(0.0, target.getHeight() + 0.25, 0.0);
        TextParticle particle = new TextParticle(world, particlePos.x, particlePos.y, particlePos.z);

        particle.setText(word);
        particle.setColor(255,0,0);

        // Add particle to the world
        client.particleManager.addParticle(particle);
    }
}

