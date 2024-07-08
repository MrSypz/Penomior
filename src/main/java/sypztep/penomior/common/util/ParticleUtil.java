package sypztep.penomior.common.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import sypztep.penomior.client.particle.TextParticle;

public class ParticleUtil {

    private static void spawnParticle(LivingEntity target, String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        if (world == null || !world.isClient()) return;

        Vec3d particlePos = target.getPos().add(0.0, target.getHeight() + 0.45, 0.0);
        TextParticle particle = new TextParticle(world, particlePos.x, particlePos.y, particlePos.z);
        particle.setText(text);

        client.particleManager.addParticle(particle);
    }

    public static void spawnTextParticle(LivingEntity target, Text text) {
        spawnParticle(target, text.getString());
    }

    public static void spawnNumberParticle(LivingEntity target, float amount) {
        String text = String.format("%.1f", amount);
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        spawnParticle(target, text);
    }
}


