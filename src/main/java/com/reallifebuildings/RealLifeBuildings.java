package com.reallifebuildings;

import com.reallifebuildings.item.BuilderTabletItem;
import com.reallifebuildings.block.BuildingBlocks;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;

@Mod(RealLifeBuildings.MOD_ID)
public class RealLifeBuildings {
    public static final String MOD_ID = "real_life_buildings";
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> BUILDER_TABLET =
            ITEMS.register("builder_tablet", () -> new BuilderTabletItem(new Item.Properties().stacksTo(1)));

    public RealLifeBuildings() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        BuildingBlocks.register(modBus);
        modBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(BUILDER_TABLET);
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(BuildingBlocks.CONCRETE_PANEL);
            event.accept(BuildingBlocks.FACADE_GLASS);
            event.accept(BuildingBlocks.OAK_FLOOR);
        }
    }
}
