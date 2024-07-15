package sypztep.penomior.mixin.vanilla.attackIframe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.api.iframe.EntityHurtCallback;
import sypztep.penomior.common.api.iframe.EntityKnockbackCallback;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(at = @At("TAIL"), method = "applyDamage", cancellable = true)
    private void onEntityHurt(final DamageSource source, final float amount, CallbackInfo ci) {
        ActionResult result = EntityHurtCallback.EVENT.invoker().hurtEntity((LivingEntity) (Object) this, source,
                amount);
        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "takeKnockback", cancellable = true)
    private void onTakingKnockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        ActionResult result = EntityKnockbackCallback.EVENT.invoker().takeKnockback(entity, entity.getAttacker(), strength, x, z);
        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
