package com.reallifebuildings;

import com.reallifebuildings.block.BuildingBlocks;
import com.reallifebuildings.block.ModBlockEntities;
import com.reallifebuildings.item.BuilderTabletItem;
import com.reallifebuildings.item.CottageBlueprintItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(RealLifeBuildings.MOD_ID)
public class RealLifeBuildings {
    public static final String MOD_ID = "real_life_buildings";
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> BUILDER_TABLET =
            ITEMS.register("builder_tablet", () -> new BuilderTabletItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COTTAGE_BLUEPRINT =
            ITEMS.register("cottage_blueprint", () -> new CottageBlueprintItem(new Item.Properties()));
    public static final RegistryObject<Item> SANDWICH = ITEMS.register("sandwich",
            () -> new Item(new Item.Properties().food(new net.minecraft.world.food.FoodProperties.Builder()
                    .nutrition(8).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> CANNED_FOOD = ITEMS.register("canned_food",
            () -> new Item(new Item.Properties().food(new net.minecraft.world.food.FoodProperties.Builder()
                    .nutrition(6).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> FRUIT_JUICE = ITEMS.register("fruit_juice",
            () -> new Item(new Item.Properties().craftRemainder(net.minecraft.world.item.Items.GLASS_BOTTLE)
                    .food(new net.minecraft.world.food.FoodProperties.Builder()
                            .nutrition(4).saturationMod(0.5F).alwaysEat().build())));

    public RealLifeBuildings() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        BuildingBlocks.register(modBus);
        ModBlockEntities.register(modBus);
        modBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(BUILDER_TABLET);
            event.accept(COTTAGE_BLUEPRINT);
            event.accept(SANDWICH);
            event.accept(CANNED_FOOD);
            event.accept(FRUIT_JUICE);
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(BuildingBlocks.CONCRETE_PANEL);
            event.accept(BuildingBlocks.FACADE_GLASS);
            event.accept(BuildingBlocks.OAK_FLOOR);
            event.accept(BuildingBlocks.RED_BRICK);
            event.accept(BuildingBlocks.SMOOTH_STONE_PANEL);
            event.accept(BuildingBlocks.DARK_CONCRETE);
            event.accept(BuildingBlocks.INTERIOR_WOOD);
            event.accept(BuildingBlocks.KITCHEN_COUNTER);
            event.accept(BuildingBlocks.MICROWAVE);
            event.accept(BuildingBlocks.FRIDGE);
            event.accept(BuildingBlocks.TERRACE_TILE);
            event.accept(BuildingBlocks.SMART_LAMP);
            event.accept(BuildingBlocks.LIVING_ROOM_CHAIR);
            event.accept(BuildingBlocks.SOFA);
            event.accept(BuildingBlocks.WARDROBE);
            event.accept(BuildingBlocks.DOORBELL);
        }
    }
}
