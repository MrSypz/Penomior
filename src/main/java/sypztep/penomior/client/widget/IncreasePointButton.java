package sypztep.penomior.client.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.payload.IncreaseStatsPayloadC2S;
import sypztep.penomior.common.stats.StatTypes;


public final class IncreasePointButton extends ActionWidgetButton {
    private final StatTypes statType;

    public IncreasePointButton(int x, int y, int width, int height, Text message, UniqueStatsComponent stats, StatTypes statType) {
        super(x, y, width, height, message, stats);
        this.statType = statType;
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

    protected void performAction() {
        if (localStatPoints > 0) {
            IncreaseStatsPayloadC2S.send(statType);
        }
    }
}