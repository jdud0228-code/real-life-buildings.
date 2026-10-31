package com.reallifebuildings.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class KitchenApplianceBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public KitchenApplianceBlock(Properties properties) {
        super(properties.lightLevel(state -> state.getValue(LIT) ? 4 : 0));
        registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (!level.isClientSide) {
            boolean running = !state.getValue(LIT);
            level.setBlock(pos, state.setValue(LIT, running), 3);
            level.playSound(null, pos, running ? SoundEvents.FURNACE_FIRE_CRACKLE
                    : SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.5F, running ? 1.0F : 0.8F);
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                    running ? "Микроволновка включена" : "Микроволновка выключена"), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
