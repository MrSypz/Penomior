package sypztep.penomior.common.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Unique;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityComponents;

import java.util.ArrayList;
import java.util.List;

public class DrawContextUtils {
    public static void drawBoldText(DrawContext context, TextRenderer renderer, String string, int i, int j, int color,int bordercolor) {
        context.drawText(renderer, string, i+1, j, bordercolor, false);
        context.drawText(renderer, string, i-1, j, bordercolor, false);
        context.drawText(renderer, string, i, j+1, bordercolor, false);
        context.drawText(renderer, string, i, j-1, bordercolor, false);
        context.drawText(renderer, string, i, j, color, false);
    }
}
