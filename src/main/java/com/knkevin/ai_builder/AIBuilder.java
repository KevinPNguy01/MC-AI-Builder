package com.knkevin.ai_builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.knkevin.ai_builder.command.arguments.ModCommandArguments;
import com.knkevin.ai_builder.items.ModItems;
import com.knkevin.ai_builder.models.Model;
import com.knkevin.ai_builder.models.util.Palette;
import com.knkevin.ai_builder.packets.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AIBuilder.MOD_ID)
public class AIBuilder {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "ai_builder";

    @Nullable
    public static Model model;

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public AIBuilder(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModCommandArguments.register(modEventBus);
        PacketHandler.init();

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.MODEL_HAMMER);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}