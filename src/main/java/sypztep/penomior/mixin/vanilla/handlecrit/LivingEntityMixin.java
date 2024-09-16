package sypztep.penomior.mixin.vanilla.handlecrit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.util.CombatUtils;
import sypztep.penomior.common.util.interfaces.MissingAccessor;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float applyDamageFirst(float amount, DamageSource source) {
        if (!this.getWorld().isClient()) {
            Entity projectileSource = source.getSource();
            if (projectileSource instanceof PersistentProjectileEntity projectile && CombatUtils.doCrit((LivingEntity) source.getAttacker()) && source.getAttacker() instanceof MissingAccessor accessor && !accessor.penomior$isMissing()) {
                projectile.setCritical(true);
                CombatUtils.applyParticle(source.getSource());
                return (float) (amount * ((LivingEntity) source.getAttacker()).getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE));
            }
        }
        return amount;
    }
}
