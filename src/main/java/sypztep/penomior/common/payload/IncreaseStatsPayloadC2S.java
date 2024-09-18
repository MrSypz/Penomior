package sypztep.penomior.common.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.Stat;
import sypztep.penomior.common.stats.StatTypes;

public record IncreaseStatsPayloadC2S(StatTypes statType) implements CustomPayload {
    public static final Id<IncreaseStatsPayloadC2S> ID = CustomPayload.id("increase_stats");
    public static final PacketCodec<PacketByteBuf, IncreaseStatsPayloadC2S> CODEC = PacketCodec.of(IncreaseStatsPayloadC2S::encode, IncreaseStatsPayloadC2S::decode);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void encode(PacketByteBuf buf) {
        buf.writeEnumConstant(statType);
    }

    public static IncreaseStatsPayloadC2S decode(PacketByteBuf buf) {
        StatTypes statType = buf.readEnumConstant(StatTypes.class);
        return new IncreaseStatsPayloadC2S(statType);
    }

    public static void send(StatTypes statType) {
        ClientPlayNetworking.send(new IncreaseStatsPayloadC2S(statType));
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<IncreaseStatsPayloadC2S> {
        @Override
        public void receive(IncreaseStatsPayloadC2S payload, ServerPlayNetworking.Context context) {
            UniqueStatsComponent stats = ModEntityComponents.UNIQUESTATS.get(context.player());
            Stat stat = stats.getLivingStats().getStat(payload.statType);
            stats.getLivingStats().useStatPoint(payload.statType, 1);
            stat.applyPrimaryEffect(context.player());
            stat.applySecondaryEffect(context.player());
            stats.sync();
        }
    }
}
