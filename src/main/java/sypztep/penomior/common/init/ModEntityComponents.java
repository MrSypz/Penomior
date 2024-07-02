package sypztep.penomior.common.init;

import net.minecraft.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.component.StatsComponent;

public class ModEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<StatsComponent> STATS = ComponentRegistry.getOrCreate(Penomior.id("stats"), StatsComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(LivingEntity.class, STATS).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(StatsComponent::new);
    }
}
