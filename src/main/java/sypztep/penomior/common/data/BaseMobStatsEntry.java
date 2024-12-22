package sypztep.penomior.common.data;

import net.minecraft.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public record BaseMobStatsEntry(int str, int agi, int dex,int vit, int anint, int luk,int lvl,int exp) {
	public static final Map<EntityType<?>, BaseMobStatsEntry> BASEMOBSTATS_MAP = new HashMap<>();
}
