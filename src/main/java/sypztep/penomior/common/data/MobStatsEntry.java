package sypztep.penomior.common.data;

import net.minecraft.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public record MobStatsEntry(int evasion, int accuracy) {
	public static final Map<EntityType<?>, MobStatsEntry> MOBSTATS_MAP = new HashMap<>();
}
