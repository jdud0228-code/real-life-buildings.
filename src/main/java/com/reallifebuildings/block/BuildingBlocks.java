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
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "real_life_buildings");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "real_life_buildings");

    public static final RegistryObject<Block> CONCRETE_PANEL = register("concrete_panel",
            () -> new Block(Block.Properties.copy(Blocks.WHITE_CONCRETE)));
    public static final RegistryObject<Block> FACADE_GLASS = register("facade_glass",
            () -> new Block(Block.Properties.copy(Blocks.GLASS).noOcclusion()));
    public static final RegistryObject<Block> OAK_FLOOR = register("oak_floor",
            () -> new Block(Block.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> RED_BRICK = register("red_brick",\n            () -> new Block(Block.Properties.copy(Blocks.BRICKS)));\n    public static final RegistryObject<Block> SMOOTH_STONE_PANEL = register("smooth_stone_panel",\n            () -> new Block(Block.Properties.copy(Blocks.SMOOTH_STONE)));\n    public static final RegistryObject<Block> DARK_CONCRETE = register("dark_concrete",\n            () -> new Block(Block.Properties.copy(Blocks.BLACK_CONCRETE)));\n\n    private BuildingBlocks() { }

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
