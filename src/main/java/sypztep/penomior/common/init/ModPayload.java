package sypztep.penomior.common.init;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import sypztep.penomior.client.payload.*;
import sypztep.penomior.common.payload.RefinePayloadC2S;

public class ModPayload {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(RefinePayloadS2C.ID, RefinePayloadS2C.CODEC); // Server to Client

        PayloadTypeRegistry.playS2C().register(AddRefineSoundPayloadS2C.ID, AddRefineSoundPayloadS2C.CODEC); // Server to Client
        PayloadTypeRegistry.playS2C().register(AddMissingParticlesPayload.ID, AddMissingParticlesPayload.CODEC); // Server to Client
        PayloadTypeRegistry.playS2C().register(AddBackParticlesPayload.ID, AddBackParticlesPayload.CODEC); // Server to Client
        initClient();
    }
    private static void initClient() {
        PayloadTypeRegistry.playC2S().register(RefinePayloadC2S.ID, RefinePayloadC2S.CODEC); // Client to Server
    }
}
