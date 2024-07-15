package sypztep.penomior.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import sypztep.penomior.common.util.ParticleUtil;

public record AddMissingParticlesPayload(int entityId) implements CustomPayload {
    public static final Id<AddMissingParticlesPayload> ID = CustomPayload.id("add_missing");
    public static final PacketCodec<PacketByteBuf, AddMissingParticlesPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            AddMissingParticlesPayload::entityId,
            AddMissingParticlesPayload::new
    );

    public static void send(ServerPlayerEntity player, int entityId) {
        ServerPlayNetworking.send(player, new AddMissingParticlesPayload(entityId));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddMissingParticlesPayload> {
        @Override
        public void receive(AddMissingParticlesPayload payload, ClientPlayNetworking.Context context) {
            Entity entity = context.player().getWorld().getEntityById(payload.entityId());
            if (entity != null)
                ParticleUtil.spawnTextParticle(entity, Text.translatable("penomior.text.missing")); //this one can't active cuz world is server
        }
    }
}
