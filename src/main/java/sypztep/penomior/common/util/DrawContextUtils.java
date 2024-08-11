package sypztep.penomior.common.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public final class DrawContextUtils {
    public static void drawBoldText(DrawContext context, TextRenderer renderer, String string, int i, int j, int color,int bordercolor) {
        context.drawText(renderer, string, i+1, j, bordercolor, false);
        context.drawText(renderer, string, i-1, j, bordercolor, false);
        context.drawText(renderer, string, i, j+1, bordercolor, false);
        context.drawText(renderer, string, i, j-1, bordercolor, false);
        context.drawText(renderer, string, i, j, color, false);
    }
    public static void drawFilledPolygon(DrawContext context, int x, int y, int[] xPoints, int[] yPoints, int color) {
        // Ensure that the number of points is at least 3
        if (xPoints.length < 3 || yPoints.length < 3 || xPoints.length != yPoints.length) {
            return; // A polygon cannot be formed with fewer than 3 points or mismatched arrays
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        MatrixStack matrixStack = context.getMatrices();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        int centerX = x;
        int centerY = y;
        for (int i = 0; i < xPoints.length; i++) {
            centerX += xPoints[i];
            centerY += yPoints[i];
        }
        centerX /= xPoints.length;
        centerY /= yPoints.length;

        // Start the polygon at the center point
        bufferBuilder.vertex(matrix, centerX, centerY, 0.0F).color(red, green, blue, alpha);

        // Add all points around the polygon
        for (int i = 0; i < xPoints.length; i++) {
            bufferBuilder.vertex(matrix, xPoints[i], yPoints[i], 0.0F).color(red, green, blue, alpha);
        }

        // Close the polygon by connecting to the first point again
        bufferBuilder.vertex(matrix, xPoints[0], yPoints[0], 0.0F).color(red, green, blue, alpha);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
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
                float normalizedDistance = Math.min(distance / ((float) height / 2), 1.0f); // Normalize distance to [0, 1]

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

                float distance = (float) Math.abs(pixelX - centerX);
                float normalizedDistance = Math.min(distance / ((float) width / 2), 1.0f); // Normalize distance to [0, 1]

                int color = ColorUtils.interpolateColor(centerColor, edgeColor, normalizedDistance);

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

                float distance = (float) Math.abs(pixelX - centerX);
                float normalizedDistance = Math.min(distance / ((float) width / 2), 1.0f); // Normalize distance to [0, 1]

                int gradientColor = ColorUtils.interpolateColor(centerColor, edgeColor, normalizedDistance);

                int originalAlpha = (gradientColor >> 24) & 0xFF; // Extract the alpha from the gradient color

                int finalAlpha = (int) (alpha * originalAlpha); // Apply the fade-in effect
                int finalColor = (gradientColor & 0x00FFFFFF) | (finalAlpha << 24);
                context.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, z, finalColor);
            }
        }
    }

    public static void drawRect(DrawContext context, int contentX, int contentY, int contentWidth, int contentHeight, int color) {
        context.fill(contentX, contentY, contentX + contentWidth, contentY + contentHeight, color);
    }
}
