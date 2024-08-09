package sypztep.penomior.mixin.vanilla.naturehealthregen;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.init.ModEntityAttributes;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract void heal(float amount);

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void natureHealthRegen(CallbackInfo ci) {
        float natureHealthRegen = (float) this.getAttributeValue(ModEntityAttributes.GENERIC_HEALTH_REGEN);
        if (natureHealthRegen > 0.00f && this.age % 20 == 0) {
            this.heal(natureHealthRegen);
        }
    }
}
