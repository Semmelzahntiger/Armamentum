package com.semmelzahntiger.arms.core.simulate.chunks;

import com.semmelzahntiger.arms.helper.Constants;
import com.semmelzahntiger.arms.helper.LRUCappedMap;
import com.semmelzahntiger.arms.helper.MiscUtil;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

// Level 2 Cache of the Simulation System. Checks each individual column in the chunk if the shell is below that
public class ChunkBlockHeightCache {
    public static final int DATA_VER = SharedConstants.getCurrentVersion().getDataVersion().getVersion();

    private static final Map<ServerLevel, ChunkBlockHeightCache> cacheMap = new HashMap<>();


    private final LRUCappedMap<ChunkPos, short[]> cache = new LRUCappedMap<>(512);
    private final ConcurrentLinkedQueue<CacheData> cacheQueue = new ConcurrentLinkedQueue<>();

    private final ServerLevel level;

    public ChunkBlockHeightCache(ServerLevel level) {
        this.level = level;
    }

    public void cacheChunkHeights(ChunkPos pos) {
        CompletableFuture<Optional<CompoundTag>> tag = level.getChunkSource().chunkMap.read(pos);
        tag.thenAccept(chunkOptional ->
                chunkOptional.ifPresent(chunkTag -> {
                    int dataVer = chunkTag.getInt("DataVersion");
                    if(dataVer != DATA_VER) {
                        return;
                    }
                    String status = chunkTag.getString("Status");
                    if(!status.equals("minecraft:full")) {
                        return;
                    }
                    CompoundTag heightmaps = chunkTag.getCompound("Heightmaps");
                    if(!heightmaps.contains("WORLD_SURFACE")) {
                        return;
                    }
                    long[] worldSurfaceHeightmap = chunkTag.getLongArray("WORLD_SURFACE");
                    int bits = Mth.ceillog2(level.getHeight() + 1);
                    SimpleBitStorage simpleBitStorage = new SimpleBitStorage(bits, 256, worldSurfaceHeightmap);
                    short[] heights = new short[256];
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            int idx = x + z * 16;
                            heights[idx] = (short) (simpleBitStorage.get(idx) + level.getMinBuildHeight());
                        }
                    }
                    cacheQueue.add(new CacheData(pos, heights));
        }));
    }

    public void tickProcessQueue() {
        while(!cacheQueue.isEmpty()) {
            CacheData cacheData = cacheQueue.poll();
            cache.put(cacheData.pos, cacheData.heightData);
        }
    }
    private short testForHeight(Vec3 position) {
       ChunkPos pos = new ChunkPos(MiscUtil.toBlockPos(position));
       int idx = pos.x + pos.z * 16;
       short[] heights = cache.get(pos);
       if(heights == null) {
           return Constants.UNINITIALIZED;
       }
       return heights[idx];
    }
    public SimulatedCollisionResult testForCollision(Vec3 position) {
        short height = testForHeight(position);
        if(height == Constants.UNINITIALIZED) {
            return SimulatedCollisionResult.UNKNOWN;
        }
        else if(height > (int) position.y) {
            return SimulatedCollisionResult.MISS;
        }
        else {
            return SimulatedCollisionResult.HIT;
        }
    }
    public Collection<Map.Entry<ChunkPos, short[]>> getCacheEntries() {
        return cache.entrySet();
    }
    public Collection<ChunkPos> getCachedPositions() {
        return cache.keySet();
    }

    public static ChunkBlockHeightCache getCache(ServerLevel level) {
        return cacheMap.computeIfAbsent(level, ChunkBlockHeightCache::new);
    }
    public static Collection<ChunkBlockHeightCache> getCaches() {
        return cacheMap.values();
    }
    public static void clearCache() {
        cacheMap.clear();
    }

    private record CacheData(ChunkPos pos, short[] heightData) {}
}
