package sypztep.penomior.common.util;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public final class DamageTrackerUtil {
    private final Map<ServerPlayerEntity, Integer> damageMap = new HashMap<>();
    private int totalDamage = 0;

    public void recordDamage(ServerPlayerEntity player, int damage) {
        damageMap.put(player, damageMap.getOrDefault(player, 0) + damage);
        totalDamage += damage;
    }

    public Map<ServerPlayerEntity, Integer> getDamageMap() {
        return damageMap;
    }

    public int getTotalDamage() {
        return totalDamage;
    }
}