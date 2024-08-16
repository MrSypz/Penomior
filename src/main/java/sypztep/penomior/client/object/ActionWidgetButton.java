package sypztep.penomior.client.object;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import sypztep.penomior.common.util.DrawContextUtils;

@Environment(EnvType.CLIENT)
public abstract class ActionWidgetButton extends ClickableWidget {
    public ActionWidgetButton(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        DrawContextUtils.drawRect(context,getX(),getY(),getWidth(),getHeight(), 0xF0292929);
        DrawContextUtils.renderHorizontalLine(context,getX() + 4 ,getY() + getHeight() / 2,9,1,400,0xFFFFFFFF);
        DrawContextUtils.renderVerticalLine(context,getX() + getWidth() / 2 ,getY() + 4,9,1,400,0xFFFFFFFF);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
