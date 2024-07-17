package sypztep.penomior;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.penomior.common.api.InfoScreenApi;
import sypztep.penomior.common.api.PlayerInfoProviderRegistry;
import sypztep.penomior.common.api.iframe.EntityHurtCallback;
import sypztep.penomior.common.api.iframe.EntityKnockbackCallback;
import sypztep.penomior.common.api.iframe.PlayerAttackCallback;
import sypztep.penomior.common.command.RefineCommand;
import sypztep.penomior.common.command.RefineSetCommand;
import sypztep.penomior.common.data.PenomiorItemDataSerializer;
import sypztep.penomior.common.event.HurtDurationEvent;
import sypztep.penomior.common.event.KnockBackThresoEvent;
import sypztep.penomior.common.event.PlayerAttackPercentageEvent;
import sypztep.penomior.common.init.*;
import sypztep.penomior.common.payload.RefinePayloadC2S;
import sypztep.penomior.common.payload.RefineSoundPayloadC2S;

public class Penomior implements ModInitializer {
    public static final String MODID = "penomior";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }

    public static boolean isCritalLoaded = false;

    @Override
    public void onInitialize() {
        ModDataComponents.init();
        ModParticles.init();
        ModPayload.init();
        ModItems.init();
        ModScreenHandler.init();
        ModItemGroups.init();
        ModLootableModify.registerLootTableModifiers();
        CommandRegistrationCallback.EVENT.register(new RefineCommand());
        CommandRegistrationCallback.EVENT.register(new RefineSetCommand());
        EntityHurtCallback.EVENT.register(new HurtDurationEvent());
        EntityKnockbackCallback.EVENT.register(new KnockBackThresoEvent());
        PlayerAttackCallback.EVENT.register(new PlayerAttackPercentageEvent());

        isCritalLoaded = FabricLoader.getInstance().isModLoaded("crital");

        ServerPlayNetworking.registerGlobalReceiver(RefinePayloadC2S.ID, new RefinePayloadC2S.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(RefineSoundPayloadC2S.ID, new RefineSoundPayloadC2S.Receiver());
        // Initialize the serializer
        PenomiorItemDataSerializer.serializer.loadConfig();

        PlayerInfoProviderRegistry.registerProvider((api, player) -> {
            InfoScreenApi.addInformation("accuracy", ModEntityComponents.STATS.get(player).getAccuracy());
            InfoScreenApi.addInformation("evasion", ModEntityComponents.STATS.get(player).getEvasion());
        });
    }
}
