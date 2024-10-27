package sypztep.penomior.mixin.vanilla.naturehealthregen;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityAttributes;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract void heal(float amount);

    @Shadow @Final private DamageTracker damageTracker;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void natureHealthRegen(CallbackInfo ci) {
        float natureHealthRegen = (float) this.getAttributeValue(ModEntityAttributes.GENERIC_HEALTH_REGEN);
        Penomior.LOGGER.info(String.valueOf(this.damageTracker.getTimeSinceLastAttack()));
        if (natureHealthRegen > 0.00f && this.age % 60 == 0 && this.damageTracker.getTimeSinceLastAttack() > 300) { // 16 thing? and 3 sec
            this.heal(natureHealthRegen);
        }
    }
}
