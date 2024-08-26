package sypztep.penomior.mixin.vanilla.agility;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.init.ModEntityAttributes;

@Mixin(BowItem.class)
public class BowItemMixin {
	@Unique
	private static float customPullProgress;

	@Inject(method = "onStoppedUsing", at = @At("HEAD"))
	private void getAttribute(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (user instanceof PlayerEntity player) {
			customPullProgress = (float) player.getAttributeValue(ModEntityAttributes.GENERIC_PLAYER_DRAWSPEED);
		}
	}
	@ModifyVariable(method = "getPullProgress", at = @At(value = "STORE", ordinal = 0))
	private static float quickerDraw(float value) {
		return value + customPullProgress;
	}
}
