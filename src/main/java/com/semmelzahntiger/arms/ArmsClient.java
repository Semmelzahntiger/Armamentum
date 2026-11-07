package com.semmelzahntiger.arms;

import com.semmelzahntiger.arms.client.data.ClientDataManager;
import com.semmelzahntiger.arms.client.events.ClientDataEvents;
import com.semmelzahntiger.arms.client.events.InputDebugEvents;
import com.semmelzahntiger.arms.client.events.InputEvents;
import com.semmelzahntiger.arms.client.input.InputPriorityProfiles;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Arms.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Arms.MODID, value = Dist.CLIENT)
public class ArmsClient {
    public ArmsClient(IEventBus modEventBus, ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        ClientDataManager.init();
        NeoForge.EVENT_BUS.register(ClientDataEvents.class);
        InputPriorityProfiles.init();
        modEventBus.register(InputEvents.class);
        NeoForge.EVENT_BUS.register(InputDebugEvents.class);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        Arms.LOGGER.info("HELLO FROM CLIENT SETUP");
        Arms.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
