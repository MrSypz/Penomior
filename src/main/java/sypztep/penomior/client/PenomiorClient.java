package sypztep.penomior.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import sypztep.penomior.ModConfig;
import sypztep.penomior.client.event.PenomiorTooltip;
import sypztep.penomior.client.payload.AddTextParticlesPayload;
import sypztep.penomior.client.payload.AddRefineSoundPayloadS2C;
import sypztep.penomior.client.payload.RefinePayloadS2C;
import sypztep.penomior.client.screen.PlayerInfoScreen;
import sypztep.penomior.client.screen.RefineScreen;
import sypztep.penomior.common.init.ModScreenHandler;

public class PenomiorClient implements ClientModInitializer {
    public static KeyBinding stats_screen = new KeyBinding("key.penomior.debug", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.penomior.keybind");
    public static ModConfig config = new ModConfig();
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandler.REFINE_SCREEN_HANDLER_TYPE, RefineScreen::new);
        ClientPlayNetworking.registerGlobalReceiver(RefinePayloadS2C.ID, new RefinePayloadS2C.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddRefineSoundPayloadS2C.ID, new AddRefineSoundPayloadS2C.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddTextParticlesPayload.ID, new AddTextParticlesPayload.Receiver());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (stats_screen.wasPressed()) {
                client.setScreen(new PlayerInfoScreen(client));
            }
        });

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ItemTooltipCallback.EVENT.register(new PenomiorTooltip());
    }
}
