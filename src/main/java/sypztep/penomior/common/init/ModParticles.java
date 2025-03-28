package sypztep.penomior.common.init;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.penomior.Penomior;

public class ModParticles {
    public static final SimpleParticleType TEXT_PARTICLE = FabricParticleTypes.simple();

    public static void init() {
        registerParticle(TEXT_PARTICLE,"text_particle");
    }
    private static void registerParticle(ParticleType type,String name) {
        Registry.register(Registries.PARTICLE_TYPE, Penomior.id(name),type);
    }
}
