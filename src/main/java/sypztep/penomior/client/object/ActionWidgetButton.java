package sypztep.penomior.client.object;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.util.ColorUtils;
import sypztep.penomior.common.util.DrawContextUtils;

public abstract class ActionWidgetButton extends ClickableWidget {
    private static final int HOVER_COLOR = 0xFF4D4D4D; // Example hover color
    private static final int DEFAULT_COLOR = 0xF0292929; // Default color

    private int currentColor = DEFAULT_COLOR;
    private int targetColor = DEFAULT_COLOR;
    private float transitionProgress = 0.0f; // 0.0 to 1.0
    private static final float TRANSITION_SPEED = 0.1f; // Speed of the transition
    protected int requiredStatPoints;
    protected UniqueStatsComponent stats;
    protected int localStatPoints;

    public ActionWidgetButton(int x, int y, int width, int height, Text message, UniqueStatsComponent stats) {
        super(x, y, width, height, message);
        this.requiredStatPoints = 1;
        this.stats = stats;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        localStatPoints = stats.getPlayerStats().getLevelSystem().getStatPoints();

        boolean hovered = isHovered() && localStatPoints > 0;
        int lineColor = (localStatPoints > 0) ? 0xFFFFFFFF : 0xFF888888; // Dim color if localStatPoints is 0

        targetColor = hovered ? HOVER_COLOR : DEFAULT_COLOR;

        if (hovered)
            transitionProgress += TRANSITION_SPEED * delta;
         else
            transitionProgress -= TRANSITION_SPEED * delta;

        // Clamp transition progress to the range [0, 1]
        transitionProgress = Math.min(Math.max(transitionProgress, 0.0f), 1.0f);

        // Apply cubic ease-out function to transition progress
        float easedProgress = easeOutCubic(transitionProgress);

        // Smoothly transition color based on eased progress
        currentColor = ColorUtils.interpolateColor(DEFAULT_COLOR, targetColor, easedProgress);

        // Draw the widget with the current color
        DrawContextUtils.drawRect(context, getX(), getY(), getWidth(), getHeight(), currentColor);

        // Draw lines with conditional dimming based on localStatPoints
        DrawContextUtils.renderHorizontalLine(context, getX() + 4, getY() + getHeight() / 2, 9, 1, 400, lineColor);
        DrawContextUtils.renderVerticalLine(context, getX() + getWidth() / 2, getY() + 4, 9, 1, 400, lineColor);
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return super.clicked(mouseX, mouseY) && localStatPoints > 0;
    }

    private float easeOutCubic(float t) {
        return 1 - (float) Math.pow(1 - t, 3);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}