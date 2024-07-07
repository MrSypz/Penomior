package sypztep.penomior.mixin.vanilla.attacklowerIframe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.api.EntityHurtCallback;
import sypztep.penomior.common.api.PlayerAttackCallback;
import sypztep.penomior.common.api.PlayerEntityAccessor;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityAccessor {

	@Inject(at = @At("TAIL"), method = "applyDamage", cancellable = true)
	private void onEntityHurt(final DamageSource source, final float amount, CallbackInfo ci) {
		ActionResult result = EntityHurtCallback.EVENT.invoker().hurtEntity((PlayerEntity) (Object) this, source,
				amount);
		if (result == ActionResult.FAIL) {
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "attack", cancellable = true)
	private void onPlayerAttack(final Entity target, CallbackInfo ci) {
		ActionResult result = PlayerAttackCallback.EVENT.invoker().attackEntity((PlayerEntity)(Object)this, target);
		if (result == ActionResult.FAIL) {
			ci.cancel();
		}
	}

	@Unique
	private boolean swinging = false;

	@Override
	public void setSwingingHand(boolean swinging) {
		this.swinging = swinging;
	}

	@Override
	public boolean isSwingingHand() {
		return this.swinging;
	}
}
