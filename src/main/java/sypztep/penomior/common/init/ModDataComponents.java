package sypztep.penomior.common.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.penomior.Penomior;
/*
TODO : ลบทิ้ง หรือแก้ไขให้เป็น โค้ดเฉพาะ ของ version 1.20.1
 */
        public class ModDataComponents {
    public static final ComponentType<NbtComponent> PENOMIOR = new ComponentType.Builder<NbtComponent>().codec(NbtComponent.CODEC).build();

    public static void init() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Penomior.id("penomior"), PENOMIOR);
    }
}
