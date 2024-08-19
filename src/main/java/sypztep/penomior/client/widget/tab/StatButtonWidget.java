package sypztep.penomior.client.widget.tab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.penomior.client.screen.PlayerInfoScreen;
import sypztep.penomior.client.widget.TabWidgetButton;

import java.util.Collections;

public class StatButtonWidget extends TabWidgetButton {

    public StatButtonWidget(int x, int y, int width, int height, Text message, MinecraftClient player, HandledScreen<?> screen, Identifier icon) {
        super(x, y, width, height, message, player, screen, icon,
                Collections.singletonList(Text.literal("Stat Information")),
                Collections.singletonList(Text.literal("Information about yourself :)")));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        if (client != null)
            client.setScreen(new PlayerInfoScreen(client));
        else client.player.sendMessage(Text.literal("Client is null"));
    }
}


