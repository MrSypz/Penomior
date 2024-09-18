package sypztep.penomior.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerEntityUtils {
    @Deprecated
    public static List<PlayerEntity> getPlayersInChunk(ServerWorld world, int chunkX, int chunkZ) {
        ChunkPos targetChunkPos = new ChunkPos(chunkX, chunkZ);

        Collection<ServerPlayerEntity> allPlayers = world.getPlayers();

        return allPlayers.stream()
                .filter(player -> isPlayerInChunk(player, targetChunkPos))
                .collect(Collectors.toList());
    }

    public static List<PlayerEntity> getPlayersInChunk(ServerWorld world, int chunkX, int chunkZ, int range) {
        ChunkPos targetChunkPos = new ChunkPos(chunkX, chunkZ);

        Collection<ServerPlayerEntity> allPlayers = world.getPlayers();

        return allPlayers.stream()
                .filter(player -> isPlayerInChunkRange(player, targetChunkPos, range))
                .collect(Collectors.toList());
    }

    private static boolean isPlayerInChunk(PlayerEntity player, ChunkPos targetChunkPos) {
        return player.getChunkPos().equals(targetChunkPos);
    }

    private static boolean isPlayerInChunkRange(PlayerEntity player, ChunkPos centralChunkPos, int range) {
        ChunkPos playerChunkPos = player.getChunkPos();

        int dx = Math.abs(playerChunkPos.x - centralChunkPos.x);
        int dz = Math.abs(playerChunkPos.z - centralChunkPos.z);

        // ระยะ 3 x 3
        return dx <= range && dz <= range;
    }

}
