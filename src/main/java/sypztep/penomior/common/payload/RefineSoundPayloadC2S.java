package sypztep.penomior.common.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import sypztep.penomior.client.payload.AddRefineSoundPayloadS2C;
import sypztep.penomior.common.screen.RefineScreenHandler;

public record RefineSoundPayloadC2S(int select) implements CustomPayload {
    public static final Id<RefineSoundPayloadC2S> ID = CustomPayload.id("refine_sound");
    public static final PacketCodec<PacketByteBuf, RefineSoundPayloadC2S> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, RefineSoundPayloadC2S::select, RefineSoundPayloadC2S::new);
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(int select) {
        ClientPlayNetworking.send(new RefineSoundPayloadC2S(select));
    }
    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<RefineSoundPayloadC2S> {
        @Override
        public void receive(RefineSoundPayloadC2S payload, ServerPlayNetworking.Context context) {
            AddRefineSoundPayloadS2C.send(context.player(), context.player().getId(), payload.select());
        }
    }
}
