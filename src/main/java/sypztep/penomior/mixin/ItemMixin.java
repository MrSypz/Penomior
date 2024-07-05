package sypztep.penomior.mixin;

import net.minecraft.component.ComponentHolder;
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
public abstract class ItemMixin implements ComponentHolder {
    @Inject(method = "onCraftByPlayer", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (!stack.isEmpty() && !player.getWorld().isClient()) {
            // Instantiate or get the PenomiorItemDataSerializer
            PenomiorItemDataSerializer serializer = new PenomiorItemDataSerializer();

            // Get the item ID from the crafted item
            String itemID = Registries.ITEM.getId(stack.getItem()).toString();

            // Retrieve the PenomiorItemData for the item
            PenomiorItemData itemData = serializer.loadConfig().get(itemID);

            if (itemData != null) {
                // Apply the refine level and other stats based on the item type
                if (stack.getItem() instanceof SwordItem) {
                    int refineLvl = 0;
                    int maxLvl = itemData.getMaxLvl();
                    int startAccuracy = itemData.getStartAccuracy();
                    int endAccuracy = itemData.getEndAccuracy();

                    RefineUtil.setRefineLvl(stack, refineLvl);
                    RefineUtil.setAccuracy(stack, refineLvl, maxLvl, startAccuracy, endAccuracy);
                } else if (stack.getItem() instanceof ArmorItem) {
                    int refineLvl = 0;
                    int maxLvl = itemData.getMaxLvl();
                    int startAccuracy = itemData.getStartAccuracy();
                    int endAccuracy = itemData.getEndAccuracy();

                    RefineUtil.setRefineLvl(stack, refineLvl);
                    RefineUtil.setEvasion(stack, refineLvl, maxLvl, startAccuracy, endAccuracy);
                }
            }
        }
    }
}


