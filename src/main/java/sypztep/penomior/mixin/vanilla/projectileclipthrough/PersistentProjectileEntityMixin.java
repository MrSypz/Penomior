package sypztep.penomior.mixin.vanilla.projectileclipthrough;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.util.interfaces.MissingAccessor;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {

    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract void setVelocity(double x, double y, double z, float power, float uncertainty);

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;deflect(Lnet/minecraft/entity/ProjectileDeflection;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Z)Z"), cancellable = true)
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof MissingAccessor accessor) {
            if (ModConfig.missingArrowPassthough && accessor.penomior$isMissing()) {
                ci.cancel();
            }
        }
    }
}
