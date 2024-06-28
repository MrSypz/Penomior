package sypztep.penomior.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.init.ModEntityComponents;

import java.util.Random;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;wakeUp()V", shift = At.Shift.BY, by = 2), cancellable = true)
    private void applyDamageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        // this in this method is target
        if (attacker instanceof PlayerEntity player) {
            StatsComponent targetStats = ModEntityComponents.STATS.getNullable(this);
            StatsComponent attackerStats = ModEntityComponents.STATS.getNullable(player);

            if (targetStats == null || attackerStats == null) return;

            double targetEvasionRate = targetStats.getEvasionRate();
            double attackerHitRate = attackerStats.getHitRate();

            System.out.println("Target Evasion Rate: " + targetEvasionRate);
            System.out.println("Attacker Hit Rate: " + attackerHitRate);
            System.out.println("Hit chance: " + (attackerHitRate - targetEvasionRate) * 100);

            // Here you can decide how to use these rates to determine the outcome of an attack
            // For example, you might roll a random number to see if the attack hits
            Random random = new Random();
            boolean attackHits = random.nextDouble() < (attackerHitRate - targetEvasionRate);

            if (attackHits) {
                System.out.println("Attack hits!");
                // Apply damage or other effects here
            } else {
                System.out.println("Attack misses!");
                cir.setReturnValue(false);
                // Handle miss here
            }
        }
    }

}
