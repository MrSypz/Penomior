package sypztep.penomior.common.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.component.UniqueStatsComponent;

public class ModEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<StatsComponent> STATS = ComponentRegistry.getOrCreate(Penomior.id("stats"), StatsComponent.class);
    public static final ComponentKey<UniqueStatsComponent> UNIQUESTATS = ComponentRegistry.getOrCreate(Penomior.id("uniquestats"), UniqueStatsComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(LivingEntity.class, STATS).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(StatsComponent::new);
        registry.beginRegistration(LivingEntity.class, UNIQUESTATS).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(UniqueStatsComponent::new);
    }
}
