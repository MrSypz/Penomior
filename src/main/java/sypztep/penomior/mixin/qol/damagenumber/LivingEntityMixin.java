package sypztep.penomior.mixin.qol.damagenumber;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.util.ParticleUtil;

import java.awt.*;

@Mixin(LivingEntity.class) @Environment(EnvType.CLIENT)
public abstract class LivingEntityMixin {

    @Unique
    private float previousHealth;
    @Inject(method = "tick()V", at = @At("TAIL"))
    private void damageNumberIndicator(CallbackInfo info) {
        if (!ModConfig.damageNumberIndicator) return;

        LivingEntity entity = (LivingEntity) (Object) this;

        var world = entity.getWorld();
        if (world == null || !world.isClient()) return;

        if (previousHealth == 0) {
            previousHealth = entity.getHealth();
        }

        float oldHealth = previousHealth;
        float newHealth = entity.getHealth();
        float damage = oldHealth - newHealth;
        String health = String.format("%.2f", damage);

        if (oldHealth != newHealth && Math.abs(damage) != entity.getMaxHealth() && damage != 0) {
            previousHealth = newHealth;
            ParticleUtil.spawnTextParticle(entity, Text.of(health), new Color(ModConfig.damageColor), -0.055f, -0.3f);
        }
    }
}
