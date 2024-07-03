package sypztep.penomior.mixin;

import net.minecraft.component.ComponentHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.util.RefineUtil;


@Mixin(Item.class)
public abstract class ItemMixin implements ComponentHolder {
    @Inject(method = "onCraftByPlayer", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (!stack.isEmpty() && !player.getWorld().isClient()) {
            if (stack.getItem() instanceof SwordItem) {
                RefineUtil.setRefineLvl(stack,20);
                RefineUtil.setAccuracy(stack,RefineUtil.getRefineLvl(stack));
            } else
            if (stack.getItem() instanceof ArmorItem) {
                RefineUtil.setRefineLvl(stack,20);
                RefineUtil.setEvasion(stack,RefineUtil.getRefineLvl(stack));
            }
        }
    }
}


