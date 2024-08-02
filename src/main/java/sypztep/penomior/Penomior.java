package sypztep.penomior;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.penomior.common.api.iframe.EntityHurtCallback;
import sypztep.penomior.common.api.iframe.EntityKnockbackCallback;
import sypztep.penomior.common.api.iframe.PlayerAttackCallback;
import sypztep.penomior.common.command.RefineCommand;
import sypztep.penomior.common.command.RefineSetCommand;
import sypztep.penomior.common.event.HurtDurationEvent;
import sypztep.penomior.common.event.KnockBackThresoEvent;
import sypztep.penomior.common.event.PlayerAttackPercentageEvent;
import sypztep.penomior.common.init.*;
import sypztep.penomior.common.payload.RefinePayloadC2S;
import sypztep.penomior.common.reloadlistener.DamageReductionReloadListener;
import sypztep.penomior.common.reloadlistener.MobStatsReloadListener;
import sypztep.penomior.common.reloadlistener.PenomiorItemReloadListener;

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
        ModLootableModify.init();
        CommandRegistrationCallback.EVENT.register(new RefineCommand());
        CommandRegistrationCallback.EVENT.register(new RefineSetCommand());
        EntityHurtCallback.EVENT.register(new HurtDurationEvent());
        EntityKnockbackCallback.EVENT.register(new KnockBackThresoEvent());
        PlayerAttackCallback.EVENT.register(new PlayerAttackPercentageEvent());

        isCritalLoaded = FabricLoader.getInstance().isModLoaded("crital");

        ServerPlayNetworking.registerGlobalReceiver(RefinePayloadC2S.ID, new RefinePayloadC2S.Receiver());
        //Data Driven
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MobStatsReloadListener());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new PenomiorItemReloadListener());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new DamageReductionReloadListener());
    }
}
