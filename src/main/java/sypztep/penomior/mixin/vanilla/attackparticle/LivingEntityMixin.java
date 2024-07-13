package sypztep.penomior.mixin.vanilla.attackparticle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.util.ParticleUtil;

@Mixin(LivingEntity.class)
@Environment(EnvType.CLIENT)
public class LivingEntityMixin {
    @Unique
    private float previousHealth = 0.0F;

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void numberDamageParticle(CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        float oldHealth = previousHealth;
        float newHealth = entity.getHealth();
        if (oldHealth != newHealth) {
            previousHealth = newHealth;
            ParticleUtil.spawnNumberParticle(entity, Math.abs(newHealth - oldHealth));
//            ParticleUtil.spawnTextParticle(entity, Text.translatable("penomior.text.missing")); //this one can't active cuz world is server
        }
    }
}
