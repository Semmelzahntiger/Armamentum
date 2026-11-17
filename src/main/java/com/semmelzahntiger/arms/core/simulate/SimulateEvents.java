package com.semmelzahntiger.arms.core.simulate;

import com.semmelzahntiger.arms.core.simulate.chunks.ChunkBlockHeightCache;
import com.semmelzahntiger.arms.core.simulate.chunks.ChunkMaxHeightCache;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import util.LogUtil;

public class SimulateEvents {
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
//        Entity entity = event.getEntity();
//        if(!(entity instanceof SimulatableProjectile)) {
//            return;
//        }
//        Vec3 nextPos = entity.position().add(entity.getDeltaMovement());
//        BlockPos nextBlockPos = new BlockPos((int) nextPos.x, (int) nextPos.y, (int) nextPos.z);
//        ServerLevel level = (ServerLevel) entity.level();
//        if(level.isPositionEntityTicking(nextBlockPos)) {
//            return;
//        }
//        CompoundTag tag = new CompoundTag();
//        boolean simulatable = entity.save(tag);
//        if(!simulatable) {
//            return;
//        }
//        SimulatedProjectile simulatedProjectile = ((SimulatableProjectile) entity).getSimulatedProjectile(entity, tag);
//        UUID entityUUID = entity.getUUID();
//        entity.discard();
//        SimulationManager manager = SimulationManager.get(level);
//        manager.addSimulatedProjectile(entityUUID, simulatedProjectile);
    }
    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        ChunkAccess chunk = event.getChunk();
        Level level = chunk.getLevel();
        if(!(level instanceof ServerLevel serverLevel) || !(chunk instanceof LevelChunk levelChunk)) {
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        server.getPlayerList().broadcastSystemMessage(Component.literal("Unloading Chunk at: " + levelChunk.getPos()), false);
        LogUtil.getLogger().info("Unloading Chunk at: {}", levelChunk.getPos());
        ChunkMaxHeightCache chunkMaxHeightCache = ChunkMaxHeightCache.getCache(serverLevel);
        chunkMaxHeightCache.cacheSectionHeight(levelChunk);
        ChunkBlockHeightCache chunkBlockHeightCache = ChunkBlockHeightCache.getCache(serverLevel);
        chunkBlockHeightCache.cacheChunkHeights(chunk.getPos());
    }
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();
        if(!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        ChunkBlockHeightCache chunkBlockHeightCache = ChunkBlockHeightCache.getCache(serverLevel);
        chunkBlockHeightCache.tickProcessQueue();
//        SimulationManager manager = serverLevel.getDataStorage().get(SimulationManager.FACTORY, SimulationManager.SAVED_DATA_ID);
//        if(manager == null) {
//            ResourceKey<Level> key = level.dimension();
//            ResourceLocation id = key.location();
//            LogUtil.getLogger().warn("Couldn't receive SimulationManager for Level: {}", id);
//            return;
//        }
//        manager.simulateTick(serverLevel);
    }
    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        ChunkBlockHeightCache.clearCache();
        ChunkMaxHeightCache.clearCache();
    }
}
