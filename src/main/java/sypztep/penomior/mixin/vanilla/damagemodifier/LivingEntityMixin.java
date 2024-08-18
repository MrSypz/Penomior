package sypztep.penomior.mixin.vanilla.damagemodifier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.penomior.common.util.CombatUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract float getHeadYaw();
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "modifyAppliedDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;)F"), argsOnly = true)
	private float attackModifyDamage(float amount, DamageSource source) {
		LivingEntity target = (LivingEntity) (Object) this;
		return CombatUtils.damageModifier(target,amount,source);
	}
}
