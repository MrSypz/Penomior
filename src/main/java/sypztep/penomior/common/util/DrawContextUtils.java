package sypztep.penomior.common.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public final class DrawContextUtils {
    public static void drawBoldText(DrawContext context, TextRenderer renderer, String string, int i, int j, int color,int bordercolor) {
        context.drawText(renderer, string, i+1, j, bordercolor, false);
        context.drawText(renderer, string, i-1, j, bordercolor, false);
        context.drawText(renderer, string, i, j+1, bordercolor, false);
        context.drawText(renderer, string, i, j-1, bordercolor, false);
        context.drawText(renderer, string, i, j, color, false);
    }
    public static void drawBorder(DrawContext context, int color, int thickness) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        // Top border
        context.fill(0, 0, width, thickness, color);
        // Bottom border
        context.fill(0, height - thickness, width, height, color);
        // Left border
        context.fill(0, 0, thickness, height, color);
        // Right border
        context.fill(width - thickness, 0, width, height, color);
    }
    public static void renderVerticalLine(DrawContext context, int positionX, int positionY, int height,int thickness, int z, int color) {
        context.fill(positionX, positionY, positionX + thickness, positionY + height, z, color);
    }
    public static void renderHorizontalLine(DrawContext context, int positionX, int positionY, int width, int thickness, int z, int color) {
        context.fill(positionX, positionY, positionX + width, positionY + thickness, z, color);
    }
    //Fill the entire screen
    public static void fillScreen(DrawContext context, int red, int green, int blue, int alpha) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        int color = ColorUtils.rgbaToHex(red, green, blue, alpha);

        context.fill(0, 0, width, height, color);
    }
    public static void fillScreen(DrawContext context, int red, int green, int blue) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        int color = ColorUtils.fromRgb(red, green, blue);

        context.fill(0, 0, width, height, color);
    }
    public static void fillScreen(DrawContext context, int color) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        context.fill(0, 0, width, height, color);
    }
    public static void renderVerticalLineWithCenterGradient(DrawContext context, int x, int y, int width, int height, int z, int centerColor, int edgeColor) {
        int centerY = y + height / 2;

        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                int pixelX = x + dx;
                int pixelY = y + dy;

                // Calculate the distance from the center vertically
                float distance = (float) Math.abs(pixelY - centerY);
                float normalizedDistance = Math.min(distance / (height / 2), 1.0f); // Normalize distance to [0, 1]

                // Interpolate color based on distance
                int color = ColorUtils.interpolateColor(centerColor, edgeColor, normalizedDistance);

                // Set pixel color
                context.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, z, color);
            }
        }
    }
    public static void renderHorizontalLineWithCenterGradient(DrawContext context, int x, int y, int width, int height, int z, int centerColor, int edgeColor) {
        int centerX = x + width / 2;

        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                int pixelX = x + dx;
                int pixelY = y + dy;

                // Calculate the distance from the center horizontally
                float distance = (float) Math.abs(pixelX - centerX);
                float normalizedDistance = Math.min(distance / ((float) width / 2), 1.0f); // Normalize distance to [0, 1]

                // Interpolate color based on distance
                int color = ColorUtils.interpolateColor(centerColor, edgeColor, normalizedDistance);

                // Set pixel color
                context.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, z, color);
            }
        }
    }
    //fade Animation
    public static void renderHorizontalLineWithCenterGradient(DrawContext context, int x, int y, int width, int height, int z, int centerColor, int edgeColor, float alpha) {
        int centerX = x + width / 2;

        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                int pixelX = x + dx;
                int pixelY = y + dy;

                // Calculate the distance from the center horizontally
                float distance = (float) Math.abs(pixelX - centerX);
                float normalizedDistance = Math.min(distance / ((float) width / 2), 1.0f); // Normalize distance to [0, 1]

                // Interpolate color based on distance
                int gradientColor = ColorUtils.interpolateColor(centerColor, edgeColor, normalizedDistance);

                // Adjust alpha for fade-in effect
                int originalAlpha = (gradientColor >> 24) & 0xFF; // Extract the alpha from the gradient color
                int finalAlpha = (int) (alpha * originalAlpha); // Apply the fade-in effect

                // Combine the color components and set the final color with adjusted alpha
                int finalColor = (gradientColor & 0x00FFFFFF) | (finalAlpha << 24);

                // Set pixel color
                context.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, z, finalColor);
            }
        }
    }

    public static void drawRect(DrawContext context, int contentX, int contentY, int contentWidth, int contentHeight, int color) {
        context.fill(contentX, contentY, contentX + contentWidth, contentY + contentHeight, color);
    }
}
