package sypztep.penomior.common.stats.element;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.Stat;
import sypztep.penomior.common.util.AttributeModification;

import java.util.ArrayList;
import java.util.List;

public class LuckStat extends Stat {
    public LuckStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(LivingEntity player) {
        applyEffect(player,
                ModEntityAttributes.GENERIC_CRIT_CHANCE,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> (0.01 * this.currentValue)
        );
    }

    @Override
    public void applySecondaryEffect(LivingEntity player) {
        List<AttributeModification> modifications = List.of(
                new AttributeModification(
                        ModEntityAttributes.GENERIC_MAGIC_ATTACK_DAMAGE,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.002 * this.currentValue)
                ),
                new AttributeModification(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.002 * this.currentValue)
                )
        );

        applyEffects(player, modifications);
        var statsComponent = ModEntityComponents.STATS.get(player);
        if (this.currentValue % 5 == 0) {
            statsComponent.addExtraAccuracy(1);
            statsComponent.addExtraEvasion(1);
        }
    }
    @Override
    public List<Text> getEffectDescription(int additionalPoints) {
        List<Text> description = new ArrayList<>();

        description.add(Text.literal("Primary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Critical Hit Chance: ")
                .append(Text.literal("+ " + additionalPoints + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        description.add(Text.literal("Secondary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Magic Attack Damage: ")
                .append(Text.literal("+ " + (0.02 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));
        description.add(Text.literal("- Attack Speed: ")
                .append(Text.literal("+ " + (0.02 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));
        description.add(Text.literal("- Additional Accuracy and Evasion: ")
                .append(Text.literal("+ " + additionalPoints + " per 5 LUK").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        return description;
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("luck_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("luck_secondary");
    }

}
