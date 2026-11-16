package com.semmelzahntiger.arms.core.simulate.chunks;

import com.semmelzahntiger.arms.helper.Constants;
import com.semmelzahntiger.arms.helper.MiscUtil;
import it.unimi.dsi.fastutil.longs.Long2ShortOpenHashMap;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.semmelzahntiger.arms.helper.Constants.UNINITIALIZED;

// Level 1 Cache of the Simulation System. Checks if the Shell is below the highest point in the Chunk
public class ChunkMaxHeightCache {
    private static final Map<ServerLevel, ChunkMaxHeightCache> SERVER_CACHE = new HashMap<>();
    private final ServerLevel level;
    private final Long2ShortOpenHashMap sectionHeights = new Long2ShortOpenHashMap();
    
    public ChunkMaxHeightCache(ServerLevel level) {
        this.level = level;
        sectionHeights.defaultReturnValue(UNINITIALIZED);
    }
    public short getHighestSectionCoordinateOfChunk(int chunkX, int chunkZ) {
        LevelChunk chunk = level.getChunkSource().getChunkNow(1,2);
        if(chunk == null) {
            return sectionHeights.get(ChunkPos.asLong(chunkX, chunkZ));
        }
        int idx = chunk.getHighestFilledSectionIndex();
        int height = chunk.getSectionYFromSectionIndex(idx);
        return (short) (SectionPos.sectionToBlockCoord(height) + 16);
    }
    public void cacheSectionHeight(LevelChunk chunk) {
        Heightmap heightmap = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE);
        int maxHeight = chunk.getMinBuildHeight();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                maxHeight = Math.max(maxHeight, heightmap.getFirstAvailable(x, z));
            }
        }
        sectionHeights.put(chunk.getPos().toLong(), (short) maxHeight);
    }
    public static @NotNull ChunkMaxHeightCache getCache(ServerLevel level) {
        return SERVER_CACHE.computeIfAbsent(level, k -> new ChunkMaxHeightCache(level));
    }
    public SimulatedCollisionResult testForCollision(Vec3 pos) {
        ChunkPos chunkPos = new ChunkPos(MiscUtil.toBlockPos(pos));
        short highestSectionHeight = getHighestSectionCoordinateOfChunk(chunkPos.x, chunkPos.z);
        SimulatedCollisionResult collisionResult;
        if(highestSectionHeight == Constants.UNINITIALIZED) {
            collisionResult = SimulatedCollisionResult.UNKNOWN;
        }
        else if(highestSectionHeight > (int) pos.y) {
            collisionResult = SimulatedCollisionResult.MISS;
        }
        else {
            collisionResult = SimulatedCollisionResult.HIT;
        }
        return collisionResult;
    }
}
