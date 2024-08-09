package sypztep.penomior.common.init;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.component.UniqueStatsComponent;

public class ModEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<StatsComponent> STATS = ComponentRegistry.getOrCreate(Penomior.id("stats"), StatsComponent.class);
    public static final ComponentKey<UniqueStatsComponent> UNIQUESTATS = ComponentRegistry.getOrCreate(Penomior.id("uniquestats"), UniqueStatsComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(LivingEntity.class, STATS).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(StatsComponent::new);
        registry.beginRegistration(PlayerEntity.class, UNIQUESTATS).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(UniqueStatsComponent::new);
    }
}
