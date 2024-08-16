package sypztep.penomior.mixin.vanilla.critattribute;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.penomior.client.payload.AddTextParticlesPayload;
import sypztep.penomior.common.init.ModEntityAttributes;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=1.5"))
    private float applyCritDmg(float original) {
        PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getChunkPos()).forEach(foundPlayer -> AddTextParticlesPayload.send(foundPlayer, this.getId(), AddTextParticlesPayload.TextParticle.CRITICAL)); // Attacker
            return (float) this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE);
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean doCrit(boolean original, Entity target) {
        double critChance = this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_CHANCE);
        if (!(critChance > 0))
            return false;
        else
            return target instanceof LivingEntity && this.random.nextFloat() < critChance && !this.hasStatusEffect(StatusEffects.BLINDNESS);
    }
}
