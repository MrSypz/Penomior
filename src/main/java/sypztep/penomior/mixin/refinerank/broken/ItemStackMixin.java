package sypztep.penomior.mixin.refinerank.broken;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.ComponentHolder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.util.RefineUtil;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
    @Shadow
    public abstract int getDamage();

    @Shadow
    public abstract int getMaxDamage();

    @Unique
    private ItemStack stack = (ItemStack) (Object) this;

    @Inject(method = "applyAttributeModifiers", at = @At("HEAD"), cancellable = true)
    public void cancleDamageAttribute(EquipmentSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifierConsumer, CallbackInfo ci) {
        if (RefineUtil.isBroken(stack)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "appendAttributeModifierTooltip", at = @At(value = "STORE"))
    private double resetAttributeValue(double value, @Local(ordinal = 0, argsOnly = true) EntityAttributeModifier modifier, @Local(ordinal = 0, argsOnly = true) PlayerEntity player) {
        if (player != null) {
            if (RefineUtil.isBroken(stack) && modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID))
                return player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            else if (stack.isIn(ItemTags.ARMOR_ENCHANTABLE) && RefineUtil.isBroken(stack)) return 0;
        }
        return value;
    }

    @Inject(method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
    public void preventBreak(int amount, ServerWorld world, ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo ci) {
        if (!ModConfig.refineItemUnbreak) return;

        if (RefineUtil.isBroken(stack)) ci.cancel();
    }

    @WrapOperation(method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"))
    private void applyBroken(ItemStack stack, int amount, Operation<Void> original) {
        if (this.getDamage() + 1 >= this.getMaxDamage() && stack.get(ModDataComponents.PENOMIOR) != null && ModConfig.refineItemUnbreak)
            RefineUtil.setBroken(stack);
        else original.call(stack, amount);
    }
}
