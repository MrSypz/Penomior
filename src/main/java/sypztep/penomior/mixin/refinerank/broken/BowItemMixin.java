package sypztep.penomior.mixin.refinerank.broken;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.penomior.common.util.RefineUtil;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Inject(method = "onStoppedUsing",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void spawnprojectileTask(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, PlayerEntity playerEntity) {
        if(RefineUtil.isBroken(stack)) ci.cancel();
    }
    @Inject(method = "use",at = @At("HEAD"), cancellable = true)
    private void cancleUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if(RefineUtil.isBroken(stack))
            cir.setReturnValue(TypedActionResult.fail(stack));
    }
}
