package sypztep.penomior.mixin.refinerank;

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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.common.util.RefineUtil;

import java.util.function.BiConsumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
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
        if (RefineUtil.isBroken(stack) && modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID))
            return player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        else if (stack.isIn(ItemTags.ARMOR_ENCHANTABLE) && RefineUtil.isBroken(stack))
            return 0;
        return value;
    }
}
