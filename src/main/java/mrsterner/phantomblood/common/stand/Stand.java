package mrsterner.phantomblood.common.stand;


import mrsterner.phantomblood.common.registry.PBSoundEvents;
import mrsterner.phantomblood.common.timestop.TimeStopUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

import java.util.Locale;

public enum Stand {
    NONE(0, (server, player, handler1, buf, responseSender) -> {}),
    THE_WORLD(60000, (server, player, handler1, buf, responseSender) -> {
        server.execute(() -> {
            int energy = StandUtils.getStandEnergy(player);
            int energyForAbility = StandUtils.getStand(player).energyForAbility;
            long ticks = StandUtils.getStandLevel(player) == 0 ? 120 : 200;
            if (energy >= energyForAbility) {
                StandUtils.setStandEnergy(player, energy - energyForAbility);
                TimeStopUtils.setTimeStoppedTicks(player.world, ticks);
                TimeStopUtils.setTimeStopper(player.world, player);
                player.world.playSound(null, player.getBlockPos(), PBSoundEvents.THE_WORLD, SoundCategory.PLAYERS, 1, 1f);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, (int) ticks));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int) ticks));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (int) ticks));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, (int) ticks));
                server.getPlayerManager().sendToAll(ServerPlayNetworking.createS2CPacket(new Identifier("phantomblood:stop_time"), PacketByteBufs.create().writeUuid(player.getUuid()).writeVarLong(ticks)));
            }
        });
    }),
    STAR_PLATINUM(40000, (server, player, handler1, buf, responseSender) -> {
        server.execute(() -> {
            int energy = StandUtils.getStandEnergy(player);
            int energyForAbility = StandUtils.getStand(player).energyForAbility;
            long ticks = StandUtils.getStandLevel(player) == 0 ? 90 : 180;
            if (energy >= energyForAbility) {
                StandUtils.setStandEnergy(player, energy - energyForAbility);
                TimeStopUtils.setTimeStoppedTicks(player.world, ticks);
                TimeStopUtils.setTimeStopper(player.world, player);
                player.world.playSound(null, player.getBlockPos(), PBSoundEvents.THE_WORLD, SoundCategory.PLAYERS, 1, 1f);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, (int) ticks, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int) ticks, 2));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (int) ticks));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, (int) ticks));
                server.getPlayerManager().sendToAll(ServerPlayNetworking.createS2CPacket(new Identifier("phantomblood:stop_time"), PacketByteBufs.create().writeUuid(player.getUuid()).writeVarLong(ticks)));
            }
        });
    }),
    KILLER_QUEEN(8000, (server, player, handler1, buf, responseSender) -> {}),
    CRAZY_DIAMOND(800, (server, player, handler1, buf, responseSender) -> {});

    public int energyForAbility;
    public ServerPlayNetworking.PlayChannelHandler handler;
    Stand(int i, ServerPlayNetworking.PlayChannelHandler handlerIn) {
        energyForAbility = i;
        handler = handlerIn;
    }

    @Override
    public String toString() {
        return "stand.phantomblood."+name().toLowerCase(Locale.ROOT);
    }
}