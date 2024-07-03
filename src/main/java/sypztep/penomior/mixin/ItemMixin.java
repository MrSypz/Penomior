package sypztep.penomior.mixin;

import net.minecraft.component.ComponentHolder;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModDataComponents;


@Mixin(Item.class)
public abstract class ItemMixin implements ComponentHolder {
    @Inject(method = "onCraftByPlayer", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (!stack.isEmpty() && !player.getWorld().isClient()) {
            if (stack.getItem() instanceof SwordItem) {
                stack.apply(ModDataComponents.PENOMIOR, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
                    itemnbt.putInt(PenomiorData.REFINE, 0);
                }));
            }
        }
    }
}


