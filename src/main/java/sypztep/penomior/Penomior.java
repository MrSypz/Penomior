package sypztep.penomior;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.penomior.common.api.iframe.EntityHurtCallback;
import sypztep.penomior.common.api.iframe.EntityKnockbackCallback;
import sypztep.penomior.common.api.iframe.PlayerAttackCallback;
import sypztep.penomior.common.command.RefineCommand;
import sypztep.penomior.common.command.RefineSetCommand;
import sypztep.penomior.common.command.SetPointCommand;
import sypztep.penomior.common.event.*;
import sypztep.penomior.common.init.*;
import sypztep.penomior.common.payload.IncreaseStatsPayloadC2S;
import sypztep.penomior.common.payload.RefineButtonPayloadC2S;
import sypztep.penomior.common.payload.RefinePayloadC2S;
import sypztep.penomior.common.reloadlistener.BaseMobStatsReloadListener;
import sypztep.penomior.common.reloadlistener.PenomiorItemReloadListener;

public class Penomior implements ModInitializer {
    public static final String MODID = "penomior";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }


    @Override
    public void onInitialize() {
        ModDataComponents.init();
        ModParticles.init();
        ModPayload.init();
        ModItems.init();
        ModScreenHandler.init();
        ModItemGroups.init();
        ModLootableModify.init();
        CommandRegistrationCallback.EVENT.register(new RefineCommand());
        CommandRegistrationCallback.EVENT.register(new RefineSetCommand());
        CommandRegistrationCallback.EVENT.register(new SetPointCommand());
        EntityHurtCallback.EVENT.register(new HurtDurationEvent());
        EntityKnockbackCallback.EVENT.register(new KnockBackThresoEvent());
        PlayerAttackCallback.EVENT.register(new PlayerAttackPercentageEvent());
        ServerLivingEntityEvents.AFTER_DEATH.register(new XPDistributionEvent());

        ServerPlayNetworking.registerGlobalReceiver(RefinePayloadC2S.ID, new RefinePayloadC2S.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(IncreaseStatsPayloadC2S.ID, new IncreaseStatsPayloadC2S.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(RefineButtonPayloadC2S.ID, new RefineButtonPayloadC2S.Receiver());
        //Data Driven
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BaseMobStatsReloadListener());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new PenomiorItemReloadListener());
    }
}
