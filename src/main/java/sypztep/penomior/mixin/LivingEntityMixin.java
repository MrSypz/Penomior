package sypztep.penomior.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.penomior.client.particle.TextParticle;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.CombatUtils;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Unique
    private final Deque<TextParticle> particles = new ArrayDeque<>();

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;wakeUp()V", shift = At.Shift.BY, by = 2), cancellable = true)
    private void applyDamageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        var client = MinecraftClient.getInstance();
        var world = client.world;
        if (world == null || !world.isClient()) return;

        // this in this method is target
        if (attacker instanceof LivingEntity living) {
            StatsComponent targetStats = ModEntityComponents.STATS.getNullable(this);
            StatsComponent attackerStats = ModEntityComponents.STATS.getNullable(living);

            if (targetStats == null || attackerStats == null) return;

            int attackerAccuracy = attackerStats.getAccuracy();
            int targetEvasion = targetStats.getEvasion();
            double hitrate = CombatUtils.calculateHitRate(attackerAccuracy, targetEvasion);

            System.out.println("Attacker Accuracy: " + attackerAccuracy);
            System.out.println("Target Evasion: " + targetEvasion);
            System.out.println("Hit chance: " + hitrate);

            // Here you can decide how to use these rates to determine the outcome of an attack
            // For example, you might roll a random number to see if the attack hits
            Random random = new Random();
            boolean attackHits = random.nextDouble() < hitrate * 0.01f; // Dividing by 100 to convert percentage to a decimal
            Vec3d particlePos = this.getPos().add(0.0, this.getHeight() + 0.25, 0.0);
            Vec3d particleVelocity = this.getVelocity();
            Vec3d particleVelocityForward = this.getPos();
            particleVelocityForward = particleVelocityForward.subtract(client.gameRenderer.getCamera().getPos()).normalize();
            particleVelocityForward = particleVelocityForward.multiply(this.getWidth() * 10.0);
            particleVelocity = particleVelocity.subtract(particleVelocityForward.x, -20.0, particleVelocityForward.z);

            var particle = new TextParticle(world, particlePos, particleVelocity);


            if (attackHits) {
                var text = String.format("%.1f", amount);
                if (text.endsWith(".0")) {
                    text = text.substring(0, text.length() - 2);
                }
                particle.setText(text);
                System.out.println("Attack hits!");
            } else {
                System.out.println("Attack misses!");
                particle.setText("Miss");
                cir.setReturnValue(false);
                // Handle miss here
            }
            particles.add(particle);
            client.particleManager.addParticle(particle);
        }
    }
}
