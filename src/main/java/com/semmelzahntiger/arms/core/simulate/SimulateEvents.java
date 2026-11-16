package com.semmelzahntiger.arms.core.simulate;

import com.semmelzahntiger.arms.core.simulate.chunks.ChunkMaxHeightCache;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.UUID;

public class SimulateEvents {
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if(!(entity instanceof SimulatableProjectile)) {
            return;
        }
        Vec3 nextPos = entity.position().add(entity.getDeltaMovement());
        BlockPos nextBlockPos = new BlockPos((int) nextPos.x, (int) nextPos.y, (int) nextPos.z);
        ServerLevel level = (ServerLevel) entity.level();
        if(level.isPositionEntityTicking(nextBlockPos)) {
            return;
        }
        CompoundTag tag = new CompoundTag();
        boolean simulatable = entity.save(tag);
        if(!simulatable) {
            return;
        }
        SimulatedProjectile simulatedProjectile = ((SimulatableProjectile) entity).getSimulatedProjectile(entity, tag);
        UUID entityUUID = entity.getUUID();
        entity.discard();
        SimulationManager manager = SimulationManager.get(level);
        manager.addSimulatedProjectile(entityUUID, simulatedProjectile);
    }
    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        ChunkAccess chunk = event.getChunk();
        Level level = chunk.getLevel();
        if(!(level instanceof ServerLevel) || !(chunk instanceof LevelChunk)) {
            return;
        }
        ChunkMaxHeightCache levelCache = ChunkMaxHeightCache.getCache((ServerLevel) level);
        levelCache.cacheSectionHeight((LevelChunk) chunk);
    }
}
