package com.reallifebuildings.block;

import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BuildingBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, "real_life_buildings");
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "real_life_buildings");

    public static final RegistryObject<Block> CONCRETE_PANEL = register("concrete_panel",
            () -> new Block(Block.Properties.copy(Blocks.WHITE_CONCRETE)));
    public static final RegistryObject<Block> FACADE_GLASS = register("facade_glass",
            () -> new Block(Block.Properties.copy(Blocks.GLASS).noOcclusion()));
    public static final RegistryObject<Block> OAK_FLOOR = register("oak_floor",
            () -> new Block(Block.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> RED_BRICK = register("red_brick",
            () -> new Block(Block.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<Block> SMOOTH_STONE_PANEL = register("smooth_stone_panel",
            () -> new Block(Block.Properties.copy(Blocks.SMOOTH_STONE)));
    public static final RegistryObject<Block> DARK_CONCRETE = register("dark_concrete",
            () -> new Block(Block.Properties.copy(Blocks.BLACK_CONCRETE)));
    public static final RegistryObject<Block> INTERIOR_WOOD = register("interior_wood",
            () -> new Block(Block.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> KITCHEN_COUNTER = register("kitchen_counter",
            () -> new Block(Block.Properties.copy(Blocks.SMOOTH_STONE)));
    public static final RegistryObject<Block> TERRACE_TILE = register("terrace_tile",
            () -> new Block(Block.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SMART_LAMP = register("smart_lamp", SmartLampBlock::new);
    public static final RegistryObject<Block> LIVING_ROOM_CHAIR = register("living_room_chair",
            () -> new SeatBlock(Block.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> SOFA = register("sofa",
            () -> new SeatBlock(Block.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistryObject<Block> WARDROBE = register("wardrobe",
            () -> new WardrobeBlock(Block.Properties.copy(Blocks.BARREL)));
    public static final RegistryObject<Block> DOORBELL = register("doorbell",
            () -> new DoorbellBlock(Block.Properties.copy(Blocks.STONE).strength(1.5F)));

    private BuildingBlocks() { }

    private static RegistryObject<Block> register(String name, Supplier<Block> block) {
        RegistryObject<Block> registered = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
        return registered;
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
