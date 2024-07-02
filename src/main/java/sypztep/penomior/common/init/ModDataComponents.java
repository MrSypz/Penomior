package sypztep.penomior.common.init;

import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.penomior.Penomior;

public class ModDataComponents {
    public static final ComponentType<NbtComponent> PENOMIOR = new ComponentType.Builder<NbtComponent>().codec(NbtComponent.CODEC).build();

    public static void init() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Penomior.id("penomior"), PENOMIOR);
    }
}
