package com.reallifebuildings.block;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class KitchenApplianceBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final Map<Item, Item> COOKING = Map.ofEntries(
            Map.entry(Items.BEEF, Items.COOKED_BEEF),
            Map.entry(Items.CHICKEN, Items.COOKED_CHICKEN),
            Map.entry(Items.PORKCHOP, Items.COOKED_PORKCHOP),
            Map.entry(Items.MUTTON, Items.COOKED_MUTTON),
            Map.entry(Items.RABBIT, Items.COOKED_RABBIT),
            Map.entry(Items.COD, Items.COOKED_COD),
            Map.entry(Items.SALMON, Items.COOKED_SALMON),
            Map.entry(Items.POTATO, Items.BAKED_POTATO));

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
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (state.getValue(LIT)) {
            player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                    "message.real_life_buildings.microwave_busy"), true);
            return InteractionResult.CONSUME;
        }
        for (ItemStack stack : player.getInventory().items) {
            Item cooked = COOKING.get(stack.getItem());
            if (cooked != null && !stack.isEmpty()) {
                stack.shrink(1);
                ItemStack result = new ItemStack(cooked);
                if (!player.getInventory().add(result)) player.drop(result, false);
                level.setBlock(pos, state.setValue(LIT, true), 3);
                level.scheduleTick(pos, this, 40);
                level.playSound(null, pos, SoundEvents.FURNACE_FIRE_CRACKLE,
                        SoundSource.BLOCKS, 0.7F, 1.0F);
                player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                        "message.real_life_buildings.microwave_cooked", result.getHoverName()), true);
                return InteractionResult.CONSUME;
            }
        }
        player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                "message.real_life_buildings.microwave_no_food"), true);
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.5F, 0.8F);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, net.minecraft.world.level.BlockGetter level,
                         BlockPos pos, net.minecraft.core.Direction direction) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos,
                     net.minecraft.util.RandomSource random) {
        if (state.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, false), 3);
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.4F, 1.0F);
        }
    }
}
