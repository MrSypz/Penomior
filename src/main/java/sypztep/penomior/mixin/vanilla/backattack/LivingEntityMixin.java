package sypztep.penomior.mixin.vanilla.backattack;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.penomior.ModConfig;
import sypztep.penomior.client.payload.AddTextParticlesPayload;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract float getHeadYaw();
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "modifyAppliedDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;)F"), argsOnly = true)
	private float backAttackModifyDamage(float amount, DamageSource source) {
		if (!ModConfig.backattack) {
			return amount;
		}
		LivingEntity target = (LivingEntity) (Object) this;
		Entity attacker = source.getAttacker();
		if (!source.isIn(DamageTypeTags.IS_PLAYER_ATTACK) || source.isIn(DamageTypeTags.IS_PROJECTILE)) { //not apply projectile it too op
			return amount;
		}
		if (attacker instanceof LivingEntity livingAttacker && source != null) {
			if (Math.abs(MathHelper.subtractAngles(getHeadYaw(), source.getSource().getHeadYaw())) <= 75) {
					PlayerLookup.tracking((ServerWorld) target.getWorld(), target.getChunkPos()).forEach(foundPlayer -> AddTextParticlesPayload.send(foundPlayer, this.getId(), AddTextParticlesPayload.TextParticle.BACKATTACK)); // Attacker
					if (livingAttacker instanceof PlayerEntity)
						PlayerLookup.tracking((ServerWorld) livingAttacker.getWorld(), livingAttacker.getChunkPos()).forEach(foundPlayer -> AddTextParticlesPayload.send(foundPlayer, this.getId(), AddTextParticlesPayload.TextParticle.BACKATTACK)); // Attacker
				return amount * 1.5F;
			}
		}
		return amount;
	}
}
