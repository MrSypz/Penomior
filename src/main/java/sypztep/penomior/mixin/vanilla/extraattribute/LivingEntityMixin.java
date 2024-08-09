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
        cir.setReturnValue(cir.getReturnValue().add(ModEntityAttributes.GENERIC_CRIT_DAMAGE).add(ModEntityAttributes.GENERIC_CRIT_CHANCE).add(ModEntityAttributes.GENERIC_HEALTH_REGEN));
    }
}
