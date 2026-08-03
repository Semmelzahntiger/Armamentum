package com.semmelzahntiger.arms;

import com.semmelzahntiger.arms.client.data.ClientDataManager;
import com.semmelzahntiger.arms.client.events.ClientEventListeners;
import com.semmelzahntiger.arms.client.events.RenderRegistrationEvents;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import com.semmelzahntiger.arms.common.register.BlockRegister;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import com.semmelzahntiger.arms.common.register.ItemRegister;
import com.semmelzahntiger.arms.core.misc.InstanceEventDistributor;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import util.ServerTaskScheduler;

@Mod(Arms.MODID)
public class Arms {
    public static final String MODID = "arms";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final IEventBus GAME_BUS = NeoForge.EVENT_BUS;
    public Arms(IEventBus modEventBus, ModContainer modContainer) {
        InstanceEventDistributor.subscribe(GAME_BUS);
        GAME_BUS.register(ClientEventListeners.class);
        GAME_BUS.register(ServerTaskScheduler.class);
        modEventBus.register(RenderRegistrationEvents.class);
        EntityRegister.ENTITY_REGISTER.register(modEventBus);
        BlockRegister.BLOCK_REGISTER.register(modEventBus);
        BlockEntityRegister.BLOCK_ENTITY_REGISTER.register(modEventBus);
        ItemRegister.ITEM_REGISTER.register(modEventBus);
        ClientDataManager.init();
    }
}
