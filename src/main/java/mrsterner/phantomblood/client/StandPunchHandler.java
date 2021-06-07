package mrsterner.phantomblood.client;

import mrsterner.phantomblood.common.registry.PBSoundEvents;
import mrsterner.phantomblood.stand.Stand;
import mrsterner.phantomblood.stand.StandMode;
import mrsterner.phantomblood.stand.StandUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class StandPunchHandler implements ServerTickEvents.StartWorldTick{
    double ticksSinceSound;

    @Override
    public void onStartTick(ServerWorld world) {

        ticksSinceSound++;
        world.getPlayers().stream()
                .filter(it -> StandUtils.isStandActive(it) && StandUtils.getStandMode(it) == StandMode.ATTACKING)
                .forEach(player -> {
                    int level = StandUtils.getStandLevel(player);
                    world.getOtherEntities(player, player.getBoundingBox().expand(2.0* MathHelper.sin(player.yaw), 0.0, 2.0*MathHelper.cos(player.yaw))).stream()
                            .filter(it -> it instanceof LivingEntity)
                            .forEach(it -> {
                                if (ticksSinceSound > 10) {
                                    ticksSinceSound = 0;
                                    //world.playSoundFromEntity(null,it,PBSoundEvents.PUNCH,SoundCategory.PLAYERS,0.0001F,1);
                                    world.playSound(null,player.getBlockPos(),PBSoundEvents.PUNCH,SoundCategory.PLAYERS,0.15F,1);
                                }
                                it.damage(DamageSource.player(player), level == 0 ? 3.0f : 6.0f);
                            });
                });
    }
}