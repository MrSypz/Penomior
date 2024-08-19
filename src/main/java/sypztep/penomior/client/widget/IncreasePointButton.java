package sypztep.penomior.client.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.payload.IncreaseStatsPayloadC2S;
import sypztep.penomior.common.stats.Stat;
import sypztep.penomior.common.stats.StatTypes;
import sypztep.penomior.common.stats.element.*;

import java.util.List;


public final class IncreasePointButton extends ActionWidgetButton {
    private final StatTypes statType;

    public IncreasePointButton(int x, int y, int width, int height, Text message, UniqueStatsComponent stats, StatTypes statType) {
        super(x, y, width, height, message, stats);
        this.statType = statType;
        initializeTooltip();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (localStatPoints >= requiredStatPoints) {
            performAction();
        } else {
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.of("Not enough stat points!"), false);
        }
    }

    private void performAction() {
        if (localStatPoints > 0) {
            IncreaseStatsPayloadC2S.send(statType);
        }
    }
    private void initializeTooltip() {
        Stat stat = getStatForType(statType);
        List<Text> descriptions = stat.getEffectDescription(1); // Example for 1 additional point
        tooltip.addAll(descriptions);
    }

    private Stat getStatForType(StatTypes statType) {
        // Implement this method to return the appropriate Stat object based on statType
        return switch (statType) {
            case AGILITY -> new AgilityStat(0); // Replace with actual base value
            case DEXTERITY -> new DexterityStat(0); // Replace with actual base value
            case INTELLIGENCE -> new IntelligenceStat(0); // Replace with actual base value
            case LUCK -> new LuckStat(0); // Replace with actual base value
            case STRENGTH -> new StrengthStat(0); // Replace with actual base value
            case VITALITY -> new VitalityStat(0); // Replace with actual base value
        };
    }
}