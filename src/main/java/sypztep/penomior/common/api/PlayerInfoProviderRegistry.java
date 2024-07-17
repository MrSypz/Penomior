package sypztep.penomior.common.api;

import net.minecraft.client.network.ClientPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PlayerInfoProviderRegistry {
    private static final Map<Integer, List<PlayerInfoProvider>> providers = new TreeMap<>();

    public static void registerProvider(PlayerInfoProvider provider) {
        registerProvider(provider, 0);
    }

    public static void registerProvider(PlayerInfoProvider provider, int priority) {
        providers.computeIfAbsent(priority, k -> new ArrayList<>()).add(provider);
    }

    public static void invokeProviders(InfoScreenApi api, ClientPlayerEntity player) {
        for (List<PlayerInfoProvider> providerList : providers.values()) {
            for (PlayerInfoProvider provider : providerList) {
                provider.providePlayerInfo(api, player);
            }
        }
    }
}
