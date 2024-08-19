package sypztep.penomior.client.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import sypztep.penomior.Penomior;

import java.util.List;

public class TabWidgetButton extends ClickableWidget {
    protected final MinecraftClient client;
    private final Identifier buttonTexture;
    private final Identifier buttonHoverTexture;
    private final Identifier icon;
    private final HandledScreen<?> screen;
    private final List<Text> tooltip;
    private final List<Text> shiftTooltip;

    public TabWidgetButton(int x, int y, int width, int height, Text message, MinecraftClient player, HandledScreen<?> screen, Identifier icon, List<Text> tooltip, List<Text> shiftTooltip) {
        super(x, y, width, height, message);
        this.client = player;
        this.screen = screen;
        this.tooltip = tooltip;
        this.shiftTooltip = shiftTooltip;
        this.buttonTexture = Penomior.id("hud/container/tab/tab_left_unselected");
        this.buttonHoverTexture = Penomior.id("hud/container/tab/tab_left_selected");
        this.icon = icon;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawGuiTexture(buttonTexture, getX(), getY(), getWidth(), getHeight());
        context.drawGuiTexture(icon, getX() + 10, getY() + 4,18,18);

        if (isHovered()) {
            context.drawGuiTexture(buttonHoverTexture, getX(), getY(), getWidth(), getHeight());
            context.drawGuiTexture(icon, getX() + 8, getY() + 4,18,18);

            boolean isShiftHeld = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT);
            if (isShiftHeld) {
                renderShiftTooltip(context, mouseX, mouseY);
            } else {
                renderTooltip(context, mouseX, mouseY);
            }
        }
    }
    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
    protected void renderTooltip(DrawContext context, int mouseX, int mouseY) {
        if (!tooltip.isEmpty()) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip, mouseX, mouseY);
        }
    }

    protected void renderShiftTooltip(DrawContext context, int mouseX, int mouseY) {
        if (!shiftTooltip.isEmpty()) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, shiftTooltip, mouseX, mouseY);
        }
    }
}
