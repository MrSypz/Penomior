package sypztep.penomior.common.api.infoscreen;

import net.minecraft.client.network.ClientPlayerEntity;

public interface PlayerInfoProvider {
    void providePlayerInfo(InfoScreenApi api, ClientPlayerEntity player);
}

