package sypztep.penomior.mixin.vanilla.extraattribute;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.penomior.common.init.ModEntityAttributes;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "createLivingAttributes", at = @At("RETURN"), cancellable = true)
    private static void registryExtraStats(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();
        builder.add(ModEntityAttributes.GENERIC_CRIT_DAMAGE);
        builder.add(ModEntityAttributes.GENERIC_CRIT_CHANCE);

        builder.add(ModEntityAttributes.GENERIC_HEALTH_REGEN);

        builder.add(ModEntityAttributes.GENERIC_MAGIC_RESISTANCE);
        builder.add(ModEntityAttributes.GENERIC_PHYSICAL_RESISTANCE);
        builder.add(ModEntityAttributes.GENERIC_PROJECTILE_RESISTANCE);

        builder.add(ModEntityAttributes.GENERIC_MAGIC_ATTACK_DAMAGE);
        builder.add(ModEntityAttributes.GENERIC_MELEE_ATTACK_DAMAGE);
        builder.add(ModEntityAttributes.GENERIC_PROJECTILE_ATTACK_DAMAGE);

        builder.add(ModEntityAttributes.GENERIC_PLAYER_VERS_ENTITY_DAMAGE);
        builder.add(ModEntityAttributes.GENERIC_PLAYER_VERS_PLAYER_DAMAGE);

        cir.setReturnValue(builder);
    }

}
