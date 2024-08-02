package sypztep.penomior.common.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class AnimationUtils {
    public static float getPositionOffset(float progress, float finalYOffset) {
        return finalYOffset * (1.0f - (float) Math.pow(1.0f - progress, 3)); // Ease-out effect
    }
    public static float getPositionOffset(float progress, float finalYOffset, int screenHeight) {
        float scaledFinalYOffset = finalYOffset * (screenHeight / 1000.0f); // Example scaling factor, adjust as needed
        return scaledFinalYOffset * (1.0f - (float) Math.pow(1.0f - progress, 3));
    }
    public static int getAlpha(float progress) {
        float alphaValue = Math.min(progress, 1.0f) * 255;
        return (int) alphaValue; // Convert to 0-255 range
    }
    public static void drawFadeText(DrawContext context,TextRenderer textRenderer, String text, int x, int y, int alpha) {
        alpha = Math.max(0, Math.min(alpha, 255));
        int color = (alpha << 24) | (255 << 16) | (255 << 8) | 255; // RGBA

        context.drawText(textRenderer, text, x, y, color, false);
    }
    public static void drawFadeText(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int alpha) {
        alpha = Math.max(0, Math.min(alpha, 255));
        int color = (alpha << 24) | (255 << 16) | (255 << 8) | 255; // RGBA

        context.drawText(textRenderer, text, x, y, color, false);
    }
}


