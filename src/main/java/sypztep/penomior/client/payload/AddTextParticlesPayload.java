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
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.util.ParticleUtil;

import java.awt.*;

public record AddTextParticlesPayload(int entityId,int selector) implements CustomPayload {
    public static final Id<AddTextParticlesPayload> ID = CustomPayload.id("add_text_particle");
    public static final PacketCodec<PacketByteBuf, AddTextParticlesPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            AddTextParticlesPayload::entityId,
            PacketCodecs.VAR_INT,
            AddTextParticlesPayload::selector,
            AddTextParticlesPayload::new
    );

    public static void send(ServerPlayerEntity player, int entityId, TextParticle selector) {
        ServerPlayNetworking.send(player, new AddTextParticlesPayload(entityId, selector.flag));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddTextParticlesPayload> {
        @Override
        public void receive(AddTextParticlesPayload payload, ClientPlayNetworking.Context context) {
            Entity entity = context.player().getWorld().getEntityById(payload.entityId());
            int selector = payload.selector();
            if (entity != null) {
                if (selector == TextParticle.MISSING.flag && ModConfig.missingIndicator)
                    ParticleUtil.spawnTextParticle(entity, Text.translatable("penomior.text.missing"));
                if (selector == TextParticle.BACKATTACK.flag && ModConfig.damageIndicator)
                    ParticleUtil.spawnTextParticle(entity, Text.translatable("penomior.text.back"),new Color(1f,1f,1f),-0.045f,0.15f);
                if (selector == TextParticle.CRITICAL.flag && ModConfig.damageIndicator)
                    ParticleUtil.spawnTextParticle(entity, Text.translatable("penomior.text.critical"),new Color(1.0f, 0.310f, 0.0f), -0.055f,-0.275f);
            }
        }
    }
    public enum TextParticle {
        MISSING(0),
        BACKATTACK(1),
        CRITICAL(2);
        private final int flag;
        TextParticle(int flag) {
            this.flag = flag;
        }
    }
}
