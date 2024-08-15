package sypztep.penomior.client.object;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.stats.StatTypes;

public class IncreasePointButton extends ActionWidgetButton {
    private final UniqueStatsComponent playerStats;
    private final StatTypes statType;

    public IncreasePointButton(int x, int y, int width, int height, Text message, UniqueStatsComponent playerStats, StatTypes statType) {
        super(x, y, width, height, message);
        this.playerStats = playerStats;
        this.statType = statType;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        increaseStatPoint();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    private void increaseStatPoint() {
        if (playerStats.getPlayerStats().getLevelSystem().getStatPoints() > 0) {
            playerStats.getPlayerStats().getLevelSystem().useStatPoint(statType,1,playerStats.getPlayerStats());
        }
    }
}
