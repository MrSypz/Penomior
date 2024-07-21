package sypztep.penomior.common.compat;

import net.minecraft.entity.LivingEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import sypztep.penomior.common.compat.provider.EntityComponentProvider;

public class JadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        //TODO register data providers
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        //TODO register component providers, icon providers, callbacks, and config options here
        registration.registerEntityComponent(EntityComponentProvider.INSTANCE, LivingEntity.class);

    }
}
