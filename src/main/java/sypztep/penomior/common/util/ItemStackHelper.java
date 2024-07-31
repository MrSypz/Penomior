package sypztep.penomior.common.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ItemStackHelper {
    public static Map<String, Double> getAttributeAmounts(PlayerEntity player) {
        Map<String, Double> attributeAmounts = new HashMap<>();
        for (ItemStack stack : RefineUtil.getItemStackFromAllEquippedSlots(player)) {
            accumulateAttributeModifiersValue(player, stack, attributeAmounts);
        }
        return attributeAmounts;
    }

    private static void accumulateAttributeModifiersValue(@Nullable PlayerEntity player, ItemStack stack, Map<String, Double> attributeAmounts) {
        for (AttributeModifierSlot attributeModifierSlot : AttributeModifierSlot.values()) {
            applyAttributeModifier(attributeModifierSlot, (attribute, modifier) -> {
                double value = calculateFinalAttributeValue(player, modifier,stack);
                String attributeName = attribute.value().getTranslationKey();

                attributeAmounts.merge(attributeName, value, Double::sum);
            }, stack);
        }
    }

    private static double calculateFinalAttributeValue(@Nullable PlayerEntity player, EntityAttributeModifier modifier,ItemStack stack) {
        double d = modifier.value();
        if (player != null) {
            if (modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID)) {
                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if (!RefineUtil.isBroken(stack) && RefineUtil.getRefineLvl(stack) > 0)
                    d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) + RefineUtil.getExtraDamage(stack);
            } else if (modifier.idMatches(Item.BASE_ATTACK_SPEED_MODIFIER_ID)) {
                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
            }
        }
        return d;
    }

    public static void applyAttributeModifier(AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifierConsumer, ItemStack stack) {
        AttributeModifiersComponent attributeModifiersComponent = stack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
        if (!attributeModifiersComponent.modifiers().isEmpty()) {
            attributeModifiersComponent.applyModifiers(slot, attributeModifierConsumer);
        } else {
            stack.getItem().getAttributeModifiers().applyModifiers(slot, attributeModifierConsumer);
        }
    }
}

