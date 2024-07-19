package sypztep.penomior.mixin.vanilla.projectileclipthrough;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.util.interfaces.MissingAccessor;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {
    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;deflect(Lnet/minecraft/entity/ProjectileDeflection;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Z)Z"), cancellable = true)
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        MissingAccessor accessor = (MissingAccessor) this;
        if (accessor.penomior$isMissing() && ModConfig.missingArrowPassthough)
            ci.cancel();
    }
}