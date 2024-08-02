package sypztep.penomior.mixin.vanilla.critattribute;

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
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.init.ModEntityAttributes;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=1.5"))
    private float applyCritDmg(float original) {
        if (ModConfig.inDev) {
            return (float) this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE);
        }
        return original;
    }
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean doCrit(boolean original, Entity target) {
        if (ModConfig.inDev) {
            double critChance = this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_CHANCE);
            if (!(critChance > 0))
                return false;
            else
                return target instanceof LivingEntity && this.random.nextFloat() < critChance && !this.hasStatusEffect(StatusEffects.BLINDNESS);
        }
        return original;
    }
}
