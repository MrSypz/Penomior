package sypztep.penomior.common.stats.element;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.stats.Stat;

import java.util.ArrayList;
import java.util.List;

public class IntelligenceStat extends Stat {
    public IntelligenceStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(ServerPlayerEntity player) {
        applyEffect(
                player,
                ModEntityAttributes.GENERIC_MAGIC_ATTACK_DAMAGE,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> 0.02 * this.currentValue
        );
    }

    @Override
    public void applySecondaryEffect(ServerPlayerEntity  player) {
        applyEffect(
                player,
                ModEntityAttributes.GENERIC_MAGIC_RESISTANCE,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> 0.005 * this.currentValue
        );
    }
    @Override
    public List<Text> getEffectDescription(int additionalPoints) {
        List<Text> description = new ArrayList<>();

        description.add(Text.literal("Primary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Magic Attack Damage: ")
                .append(Text.literal("+ " + (2 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        description.add(Text.literal("Secondary Effect:").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
        description.add(Text.literal("- Magic Resistance: ")
                .append(Text.literal("+ " + (0.5 * additionalPoints) + "%").styled(style -> style.withColor(Formatting.GREEN)))
                .styled(style -> style.withColor(Formatting.GRAY)));

        return description;
    }


    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("intelligence_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("intelligence_secondary");
    }
}
