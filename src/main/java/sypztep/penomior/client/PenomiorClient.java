package sypztep.penomior.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.text.Text;
import sypztep.penomior.client.payload.RefinePayloadS2C;
import sypztep.penomior.client.screen.RefineScreen;
import sypztep.penomior.common.init.ModScreenHandler;
import sypztep.penomior.common.util.RefineUtil;


public class PenomiorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandler.GRINDER_SCREEN_HANDLER_TYPE, RefineScreen::new);
        ClientPlayNetworking.registerGlobalReceiver(RefinePayloadS2C.ID, new RefinePayloadS2C.Receiver());

        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
                lines.add(Text.of( "Accuracy"+ RefineUtil.getAccuracy(stack)));
                lines.add(Text.of("Evasion"+ RefineUtil.getEvasion(stack)));
        });
    }
}
