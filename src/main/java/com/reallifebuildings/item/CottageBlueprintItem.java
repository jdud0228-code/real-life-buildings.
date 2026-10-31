package com.reallifebuildings.item;

import com.reallifebuildings.block.BuildingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.BlockState;

public class CottageBlueprintItem extends Item {
    public CottageBlueprintItem(Properties properties) { super(properties.stacksTo(1)); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (player == null) return InteractionResult.FAIL;
        boolean creative = player.getAbilities().instabuild;
        if (!creative && !hasMaterials(player)) {
            if (level.isClientSide) player.displayClientMessage(Component.literal(
                    "Для коттеджа нужны: дубовый пол x198, бетон x220, тёмный бетон x99, стекло x8, кирпич x71."), true);
            return InteractionResult.FAIL;
        }
        BlockPos base = context.getClickedPos().relative(context.getClickedFace());
        Direction forward = player.getDirection();
        if (player.isShiftKeyDown()) forward = forward.getClockWise();
        Direction right = forward.getClockWise();

        for (int x = 0; x < 9; x++) for (int z = 0; z < 11; z++) for (int y = 0; y <= 9; y++) {
            BlockPos pos = at(base, right, forward, x, y, z);
            if (!level.isInWorldBounds(pos) || !level.getBlockState(pos).canBeReplaced()) {
                if (level.isClientSide) player.displayClientMessage(Component.literal(
                        "Для коттеджа нужна свободная площадка 9 x 11 и высота 10 блоков."), true);
                return InteractionResult.FAIL;
            }
        }
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!creative) consume(player);

        BlockState floor = BuildingBlocks.OAK_FLOOR.get().defaultBlockState();
        BlockState wall = BuildingBlocks.CONCRETE_PANEL.get().defaultBlockState();
        BlockState glass = net.minecraft.world.level.block.Blocks.GLASS_PANE.defaultBlockState();
        BlockState roof = BuildingBlocks.DARK_CONCRETE.get().defaultBlockState();
        BlockState brick = BuildingBlocks.RED_BRICK.get().defaultBlockState();
        BlockState stair = Blocks.OAK_STAIRS.defaultBlockState();

        for (int x = 0; x < 9; x++) for (int z = 0; z < 11; z++) {
            level.setBlock(at(base, right, forward, x, 0, z), floor, 3);
            level.setBlock(at(base, right, forward, x, 5, z), floor, 3);
            level.setBlock(at(base, right, forward, x, 9, z), roof, 3);
            boolean edge = x == 0 || x == 8 || z == 0 || z == 10;
            if (edge) for (int y = 1; y <= 8; y++) {
                if (y == 5) continue;
                boolean door = z == 0 && x == 4 && y <= 2;
                if (door) continue;
                boolean window = (y == 2 || y == 7) && ((z == 5 && (x == 0 || x == 8))
                        || (x == 4 && (z == 0 || z == 10)));
                BlockState chosen = window ? glass : ((y == 1 || y == 6) ? brick : wall);
                level.setBlock(at(base, right, forward, x, y, z), chosen, 3);
            }
        }
        // Add a working two-block entrance door.
        BlockState doorLower = Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.FACING, forward)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
        level.setBlock(at(base, right, forward, 4, 1, 0), doorLower, 3);
        level.setBlock(at(base, right, forward, 4, 2, 0),
                doorLower.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3);

        // Exterior entrance lights are on when the cottage is completed.
        BlockState porchLight = BuildingBlocks.SMART_LAMP.get().defaultBlockState()
                .setValue(com.reallifebuildings.block.SmartLampBlock.LIT, true);
        level.setBlock(at(base, right, forward, 2, 3, 0), porchLight, 3);
        level.setBlock(at(base, right, forward, 6, 3, 0), porchLight, 3);

        // Simple interior partitions on both floors.
        for (int y : new int[]{1, 2, 3, 4, 6, 7, 8}) {
            int divider = y < 5 ? 5 : 4;
            for (int z = 1; z < 10; z++) {
                if (z == 3 || z == 7) continue;
                level.setBlock(at(base, right, forward, divider, y, z), wall, 3);
            }
        }
        // A compact internal staircase connects both levels.
        for (int i = 0; i < 5; i++) {
            level.setBlock(at(base, right, forward, 2 + i, 1 + i, 2 + i), stair, 3);
        }
        player.displayClientMessage(Component.literal("Двухэтажный коттедж построен!"), true);
        return InteractionResult.CONSUME;
    }

    private static boolean hasMaterials(Player p) {
        return count(p, BuildingBlocks.OAK_FLOOR.get().asItem()) >= 198
                && count(p, BuildingBlocks.CONCRETE_PANEL.get().asItem()) >= 220
                && count(p, BuildingBlocks.DARK_CONCRETE.get().asItem()) >= 99
                && count(p, BuildingBlocks.FACADE_GLASS.get().asItem()) >= 8
                && count(p, BuildingBlocks.RED_BRICK.get().asItem()) >= 71;
    }
    private static int count(Player p, Item item) {
        int n = 0;
        for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
            ItemStack stack = p.getInventory().getItem(i);
            if (stack.is(item)) n += stack.getCount();
        }
        return n;
    }
    private static void consume(Player p) {
        take(p, BuildingBlocks.OAK_FLOOR.get().asItem(), 198);
        take(p, BuildingBlocks.CONCRETE_PANEL.get().asItem(), 220);
        take(p, BuildingBlocks.DARK_CONCRETE.get().asItem(), 99);
        take(p, BuildingBlocks.FACADE_GLASS.get().asItem(), 8);
        take(p, BuildingBlocks.RED_BRICK.get().asItem(), 71);
    }
    private static void take(Player p, Item item, int amount) {
        for (int i = 0; i < p.getInventory().getContainerSize() && amount > 0; i++) {
            ItemStack stack = p.getInventory().getItem(i);
            if (!stack.is(item)) continue;
            int n = Math.min(amount, stack.getCount());
            stack.shrink(n);
            amount -= n;
        }
    }
    private static BlockPos at(BlockPos base, Direction right, Direction forward, int x, int y, int z) {
        return base.offset(right.getStepX() * x + forward.getStepX() * z, y,
                right.getStepZ() * x + forward.getStepZ() * z);
    }
}
