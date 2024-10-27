package sypztep.penomior.common.stats.element;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.stats.Stat;
import sypztep.penomior.common.util.AttributeModification;

import java.util.ArrayList;
import java.util.List;


public class VitalityStat extends Stat {
    public VitalityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(LivingEntity living) {
        applyEffect(
                living,
                EntityAttributes.GENERIC_MAX_HEALTH,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> baseValue * (0.05 * this.currentValue)
        );
    }

    @Override
    public void applySecondaryEffect(LivingEntity living) {
        List<AttributeModification> modifications = List.of(
                new AttributeModification(
                        ModEntityAttributes.GENERIC_HEALTH_REGEN,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.02 * this.currentValue)
                ),
                new AttributeModification(
                        ModEntityAttributes.GENERIC_PHYSICAL_RESISTANCE,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.005 * this.currentValue)
                )
        );
        applyEffects(living, modifications);
    }
    @Override
    public List<Text> getEffectDescription(int additionalPoints) {
        List<Text> description = new ArrayList<>();

        description.add(Text.literal("Primary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Max Health: ")
                .append(Text.literal("+ " + (5 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        description.add(Text.literal("Secondary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Health Regen: ")
                .append(Text.literal("+ " + (0.02 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));
        description.add(Text.literal("- Physical Resistance: ")
                .append(Text.literal("+ " + (0.5 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        return description;
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("vitality_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("vitality_secondary");
    }
}
