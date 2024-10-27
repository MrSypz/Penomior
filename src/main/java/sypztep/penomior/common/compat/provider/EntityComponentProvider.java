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
        var target = ModEntityComponents.STATS.getNullable(entityAccessor.getEntity());
        var attacker = ModEntityComponents.STATS.getNullable(entityAccessor.getPlayer());
        var targetStats = ModEntityComponents.UNIQUESTATS.getNullable(entityAccessor.getPlayer());
        var attackerStats = ModEntityComponents.UNIQUESTATS.getNullable(entityAccessor.getEntity());

        if (target != null && attacker != null && attackerStats != null && targetStats != null) {
            int attackerAccuracy = attacker.getAccuracy();
            int targetEvasion = target.getEvasion();

            double hitRate = CombatUtils.hitRate(attackerStats, targetStats, attackerAccuracy, targetEvasion);

            double fleeRate = CombatUtils.fleeRate(attackerStats, targetStats, targetEvasion, attackerAccuracy, 1);

            double hitChance = CombatUtils.calculateHitRate(hitRate, fleeRate);


            iTooltip.add(Text.translatable("penomior.hitchance", String.format("%.2f", hitChance)));
        }
    }

    @Override
    public Identifier getUid() {
        return Penomior.id("stats_config");
    }
}
