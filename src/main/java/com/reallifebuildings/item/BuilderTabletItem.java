package com.reallifebuildings.item;

import com.reallifebuildings.block.BuildingBlocks;
import com.reallifebuildings.client.BuilderTabletScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class BuilderTabletItem extends Item {
    public BuilderTabletItem(Properties properties) { super(properties); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> BuilderTabletScreen::open);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (player == null) return InteractionResult.FAIL;
        boolean creative = player.getAbilities().instabuild;
        if (!creative && !hasBuildingMaterials(player)) {
            if (level.isClientSide) {
                player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                        "Не хватает материалов: дубовый пол x64, бетонная панель x64, тёмный бетон x64, стекло x8, кирпич x16."), true);
            }
            return InteractionResult.FAIL;
        }

        BlockPos base = context.getClickedPos().relative(context.getClickedFace());
        Direction forward = player.getDirection();
        if (player.isShiftKeyDown()) forward = forward.getClockWise();
        Direction right = forward.getClockWise();

        // Validate the complete footprint before changing any blocks.
        for (int x = 0; x < 7; x++) {
            for (int z = 0; z < 9; z++) {
                for (int y = 0; y <= 4; y++) {
                    BlockPos pos = localPos(base, right, forward, x, y, z);
                    if (!level.isInWorldBounds(pos) || !level.getBlockState(pos).canBeReplaced()) {
                        if (level.isClientSide) {
                            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                                    "Нет свободного места для дома 7 x 9 блоков."), true);
                        }
                        return InteractionResult.FAIL;
                    }
                }
            }
        }
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!creative) consumeBuildingMaterials(player);

        BlockState floor = BuildingBlocks.OAK_FLOOR.get().defaultBlockState();
        BlockState wall = BuildingBlocks.CONCRETE_PANEL.get().defaultBlockState();
        BlockState glass = net.minecraft.world.level.block.Blocks.GLASS_PANE.defaultBlockState();
        BlockState roof = BuildingBlocks.DARK_CONCRETE.get().defaultBlockState();
        BlockState detail = BuildingBlocks.RED_BRICK.get().defaultBlockState();

        for (int x = 0; x < 7; x++) {
            for (int z = 0; z < 9; z++) {
                level.setBlock(localPos(base, right, forward, x, 0, z), floor, 3);

                boolean perimeter = x == 0 || x == 6 || z == 0 || z == 8;
                if (perimeter) {
                    for (int y = 1; y <= 3; y++) {
                        boolean doorway = z == 0 && x >= 3 && x <= 3 && y <= 2;
                        boolean window = y == 2 && ((z == 4 && (x == 0 || x == 6))
                                || (x == 3 && (z == 0 || z == 8)));
                        if (doorway) continue;
                        BlockState chosen = window ? glass : (y == 1 && (x == 0 || x == 6) ? detail : wall);
                        level.setBlock(localPos(base, right, forward, x, y, z), chosen, 3);
                    }
                }
                level.setBlock(localPos(base, right, forward, x, 4, z), roof, 3);
            }
        }
        // Install a real two-block vanilla door in the front entrance.
        BlockState doorLower = Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.FACING, forward)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
        BlockState doorUpper = doorLower.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
        level.setBlock(localPos(base, right, forward, 3, 1, 0), doorLower, 3);
        level.setBlock(localPos(base, right, forward, 3, 2, 0), doorUpper, 3);

        // Interior partitions divide the floor plan into four functional zones.
        for (int y = 1; y <= 3; y++) {
            for (int z = 1; z <= 7; z++) {
                if (z == 3 || z == 4) continue;
                level.setBlock(localPos(base, right, forward, 4, y, z), wall, 3);
            }
            for (int x = 1; x <= 5; x++) {
                if (x == 2) continue;
                level.setBlock(localPos(base, right, forward, x, y, 4), wall, 3);
            }
        }
        player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                player.isShiftKeyDown() ? "Дом построен с поворотом!" : "Современный дом построен!"), true);
        return InteractionResult.CONSUME;
    }

    private static boolean hasBuildingMaterials(Player player) {
        return countItem(player, BuildingBlocks.OAK_FLOOR.get().asItem()) >= 64
                && countItem(player, BuildingBlocks.CONCRETE_PANEL.get().asItem()) >= 64
                && countItem(player, BuildingBlocks.DARK_CONCRETE.get().asItem()) >= 64
                && countItem(player, BuildingBlocks.FACADE_GLASS.get().asItem()) >= 8
                && countItem(player, BuildingBlocks.RED_BRICK.get().asItem()) >= 16;
    }

    private static int countItem(Player player, net.minecraft.world.item.Item item) {
        int count = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(item)) count += stack.getCount();
        }
        return count;
    }

    private static void consumeBuildingMaterials(Player player) {
        consumeItem(player, BuildingBlocks.OAK_FLOOR.get().asItem(), 64);
        consumeItem(player, BuildingBlocks.CONCRETE_PANEL.get().asItem(), 64);
        consumeItem(player, BuildingBlocks.DARK_CONCRETE.get().asItem(), 64);
        consumeItem(player, BuildingBlocks.FACADE_GLASS.get().asItem(), 8);
        consumeItem(player, BuildingBlocks.RED_BRICK.get().asItem(), 16);
    }

    private static void consumeItem(Player player, net.minecraft.world.item.Item item, int amount) {
        for (int slot = 0; slot < player.getInventory().getContainerSize() && amount > 0; slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (!stack.is(item)) continue;
            int taken = Math.min(amount, stack.getCount());
            stack.shrink(taken);
            amount -= taken;
        }
    }

    private static BlockPos localPos(BlockPos base, Direction right, Direction forward,
                                     int x, int y, int z) {
        return base.offset(right.getStepX() * x + forward.getStepX() * z, y,
                right.getStepZ() * x + forward.getStepZ() * z);
    }
}
