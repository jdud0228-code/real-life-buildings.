package com.reallifebuildings.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SeatBlock extends Block {
    public SeatBlock(Properties properties) { super(properties); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (player.isPassenger() || player.isShiftKeyDown()) return InteractionResult.PASS;
        if (!level.isClientSide) {
            ArmorStand seat = new ArmorStand(level, pos.getX() + 0.5, pos.getY() + 0.15, pos.getZ() + 0.5);
            seat.setInvisible(true);
            seat.setMarker(true);
            seat.setNoGravity(true);
            seat.setInvulnerable(true);
            seat.getPersistentData().putBoolean("real_life_buildings_seat", true);
            level.addFreshEntity(seat);
            if (player.startRiding(seat, true)) {
                level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.35F, 0.8F);
            } else {
                seat.discard();
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
