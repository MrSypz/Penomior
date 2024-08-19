package sypztep.penomior.client.widget.tab;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.penomior.client.widget.TabWidgetButton;
import sypztep.penomior.common.payload.RefineButtonPayloadC2S;

import java.util.Collections;

public class RefineButtonWidget extends TabWidgetButton {


    public RefineButtonWidget(int x, int y, int width, int height, Text message, HandledScreen<?> screen, Identifier icon) {
        super(x, y, width, height, message, null, screen, icon,
                Collections.singletonList(Text.literal("Refinement")),
                Collections.singletonList(Text.literal("Refine your item to make it stronger")));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        RefineButtonPayloadC2S.send();
        super.onClick(mouseX, mouseY);
    }
}
