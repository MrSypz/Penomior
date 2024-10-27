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

import java.util.ArrayList;
import java.util.List;

public class AgilityStat extends Stat {
    public AgilityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(LivingEntity living) {
        applyEffect(living, EntityAttributes.GENERIC_ATTACK_SPEED, getPrimaryId(), EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> (0.01 * this.currentValue));
    }

    @Override
    public void applySecondaryEffect(LivingEntity living) {
        var evasion = ModEntityComponents.STATS.get(living);
        evasion.addExtraEvasion(1);

        applyEffect(living, ModEntityAttributes.GENERIC_PLAYER_DRAWSPEED, getSecondaryId(), EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> (0.005 * this.currentValue));
    }

    @Override
    public List<Text> getEffectDescription(int additionalPoints) {
        List<Text> description = new ArrayList<>();

        description.add(Text.literal("Primary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Attack Speed: ")
                .append(Text.literal("+ " +additionalPoints + "% per AGI").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        description.add(Text.literal("Secondary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Evasion: ")
                .append(Text.literal("+ " + additionalPoints + " per AGI").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));
        description.add(Text.literal("- Bow Draw Speed: ")
                .append(Text.literal("+ " + (0.5 * additionalPoints) + "% per AGI").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        return description;
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("agility_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("agility_secondary");
    }
}

