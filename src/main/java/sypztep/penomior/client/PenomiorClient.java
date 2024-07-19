package sypztep.penomior.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import sypztep.penomior.client.event.PenomiorTooltip;
import sypztep.penomior.client.payload.AddBackParticlesPayload;
import sypztep.penomior.client.payload.AddMissingParticlesPayload;
import sypztep.penomior.client.payload.AddRefineSoundPayloadS2C;
import sypztep.penomior.client.payload.RefinePayloadS2C;
import sypztep.penomior.client.screen.RefineScreen;
import sypztep.penomior.common.api.infoscreen.InfoScreenApi;
import sypztep.penomior.common.api.infoscreen.PlayerInfoProviderRegistry;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.init.ModScreenHandler;


public class PenomiorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandler.REFINE_SCREEN_HANDLER_TYPE, RefineScreen::new);
        ClientPlayNetworking.registerGlobalReceiver(RefinePayloadS2C.ID, new RefinePayloadS2C.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddRefineSoundPayloadS2C.ID, new AddRefineSoundPayloadS2C.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddMissingParticlesPayload.ID, new AddMissingParticlesPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddBackParticlesPayload.ID, new AddBackParticlesPayload.Receiver());

        ItemTooltipCallback.EVENT.register(new PenomiorTooltip());

        PlayerInfoProviderRegistry.registerProvider((api, player) -> {
            InfoScreenApi.addInformation("accuracy", ModEntityComponents.STATS.get(player).getAccuracy());
            InfoScreenApi.addInformation("evasion", ModEntityComponents.STATS.get(player).getEvasion());
        }, -1); // High priority
    }
}
