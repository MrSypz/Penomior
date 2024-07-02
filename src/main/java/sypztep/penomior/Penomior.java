package sypztep.penomior;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.init.ModParticles;

import java.util.logging.Logger;

public class Penomior implements ModInitializer {
    public static final String MODID = "penomior";
    public static final Logger LOGGER = Logger.getLogger(MODID);
    public static Identifier id (String path) {
        return Identifier.of(MODID, path);
    }
    @Override
    public void onInitialize() {
        ModDataComponents.init();
        ModParticles.init();
    }
}
