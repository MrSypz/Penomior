package sypztep.penomior.mixin.vanilla.handlecrit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.util.CombatUtils;
import sypztep.penomior.common.util.interfaces.MissingAccessor;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=1.5"))
    private float applyCritDmg(float original) {
        return (float) this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE);
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean doCrit(boolean original, Entity target) {
        if (!this.getWorld().isClient()) {
            if (CombatUtils.getCritChance(this) > 0 && CombatUtils.doCrit(this) && target instanceof MissingAccessor accessor && !accessor.penomior$isMissing()) {
                CombatUtils.applyParticle(target);
                return target instanceof LivingEntity && !this.hasStatusEffect(StatusEffects.BLINDNESS);
            }
        }
        return false;
    }

}