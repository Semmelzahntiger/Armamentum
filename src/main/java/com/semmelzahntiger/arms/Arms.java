package com.semmelzahntiger.arms;

import com.semmelzahntiger.arms.client.data.ClientData;
import com.semmelzahntiger.arms.client.data.ClientDataManager;
import com.semmelzahntiger.arms.client.events.ClientEventListeners;
import com.semmelzahntiger.arms.client.events.RenderRegistrationEvents;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Arms.MODID)
public class Arms {
    public static final String MODID = "arms";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final IEventBus GAME_BUS = NeoForge.EVENT_BUS;
    public Arms(IEventBus modEventBus, ModContainer modContainer) {
        GAME_BUS.register(ClientEventListeners.class);
        modEventBus.register(RenderRegistrationEvents.class);
        EntityRegister.ENTITY_REGISTER.register(modEventBus);
        ClientDataManager.init();
    }
}
