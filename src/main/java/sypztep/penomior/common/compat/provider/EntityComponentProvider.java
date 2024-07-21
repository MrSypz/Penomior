package sypztep.penomior.common.compat.provider;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.CombatUtils;

public enum EntityComponentProvider implements IEntityComponentProvider {
    INSTANCE;
    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        var component = ModEntityComponents.STATS.getNullable(entityAccessor.getEntity());
        var user = ModEntityComponents.STATS.getNullable(entityAccessor.getPlayer());

        if (component != null && user != null) {
            int evasion = component.getEvasion();
            int accuracy = component.getAccuracy();
            double hitChance = CombatUtils.calculateHitRate(user.getAccuracy(), evasion);

            iTooltip.add(Text.translatable("penomior.evasion.point", evasion));
            iTooltip.add(Text.translatable("penomior.accuracy.point", accuracy));
            iTooltip.add(Text.translatable("penomior.hitchance", String.format("%.2f", hitChance)));
        }
    }

    @Override
    public Identifier getUid() {
        return Penomior.id("stats_config");
    }
}
