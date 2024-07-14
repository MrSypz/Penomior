package sypztep.penomior.common.api;

import net.minecraft.client.network.ClientPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoProviderRegistry {
    private static final List<PlayerInfoProvider> providers = new ArrayList<>();

    public static void registerProvider(PlayerInfoProvider provider) {
        providers.add(provider);
    }

    public static void invokeProviders(InfoScreenApi api, ClientPlayerEntity player) {
        for (PlayerInfoProvider provider : providers) {
            provider.providePlayerInfo(api, player);
        }
    }
}
