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

public class DexterityStat extends Stat {

    public DexterityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(LivingEntity living) {
        var accuracy = ModEntityComponents.STATS.get(living);
        accuracy.addExtraAccuracy(1);
    }

    @Override
    public void applySecondaryEffect(LivingEntity living) {
        List<AttributeModification> modifications = List.of(
                new AttributeModification(
                        ModEntityAttributes.GENERIC_PROJECTILE_ATTACK_DAMAGE,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.02 * this.currentValue)
                ),
                new AttributeModification(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
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
        description.add(Text.literal("- Accuracy: ")
                .append(Text.literal("+ " + additionalPoints + " per DEX").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        description.add(Text.literal("Secondary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Projectile Attack Damage: ")
                .append(Text.literal("+ " + (2 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));
        description.add(Text.literal("- Attack Speed: ")
                .append(Text.literal("+ " + (0.5 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        return description;
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("dexterity_secondary");
    }
}
