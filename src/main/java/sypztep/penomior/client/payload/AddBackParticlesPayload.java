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

import java.awt.*;

public record AddBackParticlesPayload(int entityId) implements CustomPayload {
    public static final Id<AddBackParticlesPayload> ID = CustomPayload.id("add_back");
    public static final PacketCodec<PacketByteBuf, AddBackParticlesPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            AddBackParticlesPayload::entityId,
            AddBackParticlesPayload::new
    );

    public static void send(ServerPlayerEntity player, int entityId) {
        ServerPlayNetworking.send(player, new AddBackParticlesPayload(entityId));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddBackParticlesPayload> {
        @Override
        public void receive(AddBackParticlesPayload payload, ClientPlayNetworking.Context context) {
            Entity entity = context.player().getWorld().getEntityById(payload.entityId());
            if (entity != null)
                ParticleUtil.spawnTextParticle(entity, Text.translatable("penomior.text.back"),new Color(1f,0,0),-0.045f); //this one can't active cuz world is server
        }
    }
}
