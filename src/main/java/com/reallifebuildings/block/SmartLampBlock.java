package com.reallifebuildings.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class SmartLampBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SmartLampBlock() {
        super(Properties.copy(Blocks.REDSTONE_LAMP)
                .lightLevel(state -> state.getValue(LIT) ? 15 : 0));
        registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        boolean turnOn = !state.getValue(LIT);
        level.setBlock(pos, state.setValue(LIT, turnOn), 3);
        level.playSound(null, pos, turnOn
                ? net.minecraft.sounds.SoundEvents.LEVER_CLICK
                : net.minecraft.sounds.SoundEvents.LEVER_CLICK,
                net.minecraft.sounds.SoundSource.BLOCKS, 0.35F, turnOn ? 1.4F : 0.8F);
        return InteractionResult.CONSUME;
    }
}
