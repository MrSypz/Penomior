package sypztep.penomior.mixin.vanilla.newdamage;

import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.penomior.common.util.CombatUtils;

@Mixin(DamageUtil.class)
public class DamageUtilMixin {
    @Inject(method = "getDamageLeft", at = @At("HEAD"), cancellable = true)
    private static void newDamageLeftCalculate(LivingEntity armorWearer, float damageAmount, DamageSource damageSource, float armor, float armorToughness, CallbackInfoReturnable<Float> cir) {
//        cir.setReturnValue(CombatUtils.newDamageLeftCalculate(armorWearer,damageAmount,damageSource,armor,armorToughness));
    //TODO: Change the method that calculate damage
    }
}
