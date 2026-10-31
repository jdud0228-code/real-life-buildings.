package com.reallifebuildings.block;

import com.reallifebuildings.RealLifeBuildings;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RealLifeBuildings.MOD_ID);
    public static final RegistryObject<BlockEntityType<WardrobeBlockEntity>> WARDROBE =
            TYPES.register("wardrobe", () -> BlockEntityType.Builder
                    .of(WardrobeBlockEntity::new, BuildingBlocks.WARDROBE.get()).build(null));

    private ModBlockEntities() { }
    public static void register(IEventBus bus) { TYPES.register(bus); }
}
