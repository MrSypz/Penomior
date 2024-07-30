package sypztep.penomior.common.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec2f;

public class PositionElementUtil {

    private static final int BASE_WIDTH = 1920;
    private static final int BASE_HEIGHT = 1080;

    public static int getScaledX(int screenWidth, int positionX, float scale) {
        return (int) ((positionX * scale) * (screenWidth / (float) BASE_WIDTH));
    }

    public static int getScaledY(int screenHeight, int positionY, float scale) {
        return (int) ((positionY * scale) * (screenHeight / (float) BASE_HEIGHT));
    }

    public static int getScaledWidth(int screenWidth, int originalWidth, float scale) {
        return (int) ((originalWidth * scale) * (screenWidth / (float) BASE_WIDTH));
    }

    public static int getScaledHeight(int screenHeight, int originalHeight, float scale) {
        return (int) ((originalHeight * scale) * (screenHeight / (float) BASE_HEIGHT));
    }

    public static void drawScaledRect(DrawContext context, int positionX, int positionY, int width, int height, int color, Vec2f scale) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

        int x = getScaledX(screenWidth, positionX, scale.x);
        int y = getScaledY(screenHeight, positionY, scale.y);
        int scaledWidth = getScaledWidth(screenWidth, width, scale.x);
        int scaledHeight = getScaledHeight(screenHeight, height, scale.y);

        DrawContextUtils.drawRect(context, x, y, scaledWidth, scaledHeight, color);
    }
}