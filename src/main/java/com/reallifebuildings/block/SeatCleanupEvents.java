package com.reallifebuildings.block;

import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "real_life_buildings", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SeatCleanupEvents {
    private SeatCleanupEvents() { }

    @SubscribeEvent
    public static void removeEmptySeat(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof ArmorStand stand
                && stand.getPersistentData().getBoolean("real_life_buildings_seat")
                && stand.getPassengers().isEmpty()
                && !stand.level().isClientSide) {
            stand.discard();
        }
    }
}
