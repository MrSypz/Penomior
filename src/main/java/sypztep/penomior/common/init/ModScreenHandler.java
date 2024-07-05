package sypztep.penomior.common.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import sypztep.penomior.common.screen.RefineScreenHandler;

public class ModScreenHandler {
    public static ScreenHandlerType<RefineScreenHandler> GRINDER_SCREEN_HANDLER_TYPE;
    public static void init() {
        GRINDER_SCREEN_HANDLER_TYPE = Registry.register(Registries.SCREEN_HANDLER, "grinder",
                new ScreenHandlerType<>((syncId, inventory) -> new RefineScreenHandler(syncId, inventory, ScreenHandlerContext.EMPTY), FeatureFlags.VANILLA_FEATURES));

    }
}