package sypztep.penomior.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.data.PenomiorItemData;
import sypztep.penomior.common.data.PenomiorItemDataSerializer;
import sypztep.penomior.common.util.RefineUtil;


@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "onCraftByPlayer", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (!stack.isEmpty() && !player.getWorld().isClient()) {
            String itemID = Registries.ITEM.getId(stack.getItem()).toString();
            PenomiorItemData itemData = PenomiorItemDataSerializer.getConfigCache().get(itemID);

            if (itemData != null) {
                int refineLvl = 0;
                int maxLvl = itemData.maxLvl();
                int startAccuracy = itemData.startAccuracy();
                int endAccuracy = itemData.endAccuracy();
                int startEvasion = itemData.startEvasion();
                int endEvasion = itemData.endEvasion();
                //---write data----//
                if (itemID.equals(itemData.itemID())) {
                    RefineUtil.setRefineLvl(stack, refineLvl);
                    RefineUtil.setAccuracy(stack, refineLvl, maxLvl, startAccuracy, endAccuracy);
                    RefineUtil.setEvasion(stack, refineLvl, maxLvl, startEvasion, endEvasion);
                }
            }
        }
    }
}


