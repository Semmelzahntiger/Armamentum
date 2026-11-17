package com.semmelzahntiger.arms.core.simulate.chunks;

import com.semmelzahntiger.arms.helper.Constants;
import com.semmelzahntiger.arms.helper.LRUCappedMap;
import com.semmelzahntiger.arms.helper.MiscUtil;
import net.minecraft.SharedConstants;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import util.LogUtil;

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

    public void cacheFromLoadedChunk(LevelChunk chunk) {
        short[] heights = new short[256];
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                heights[x + z * 16] = (short) (chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) + 1);
            }
        }
        cache.put(chunk.getPos(), heights);
    }

    public void cacheChunkHeights(ChunkPos pos) {
        final int minY = level.getMinBuildHeight();
        final int bits = Mth.ceillog2(level.getHeight() + 1);

        level.getChunkSource().chunkMap.read(pos)
                .thenAccept(chunkOptional -> chunkOptional.ifPresent(chunkTag -> {
                    if (chunkTag.getInt("DataVersion") != DATA_VER) return;
                    if (!"minecraft:full".equals(chunkTag.getString("Status"))) return;

                    CompoundTag heightmaps = chunkTag.getCompound("Heightmaps");
                    if (!heightmaps.contains("WORLD_SURFACE", Tag.TAG_LONG_ARRAY)) return;

                    long[] worldSurfaceHeightmap = heightmaps.getLongArray("WORLD_SURFACE");
                    SimpleBitStorage storage = new SimpleBitStorage(bits, 256, worldSurfaceHeightmap);

                    short[] heights = new short[256];
                    for (int i = 0; i < 256; i++) {
                        heights[i] = (short) (storage.get(i) + minY);
                    }
                    cacheQueue.add(new CacheData(pos, heights));
                }))
                .whenComplete((ignored, error) -> {
                    if (error != null) LogUtil.getLogger().error("L1 read failed for {}", pos, error);
                });
    }

    public void tickProcessQueue() {
        CacheData cacheData;
        while ((cacheData = cacheQueue.poll()) != null) {
            cache.put(cacheData.pos, cacheData.heightData);
        }
    }
    public int getProcessesInQueue() {
        return cacheQueue.size();
    }

    private short testForHeight(Vec3 position) {
        int bx = Mth.floor(position.x);
        int bz = Mth.floor(position.z);
        ChunkPos pos = new ChunkPos(SectionPos.blockToSectionCoord(bx), SectionPos.blockToSectionCoord(bz));

        short[] heights = cache.get(pos);
        if (heights == null) {
            return Constants.UNINITIALIZED;
        }
        int idx = (bx & 15) + (bz & 15) * 16;
        return heights[idx];
    }

    public SimulatedCollisionResult testForCollision(Vec3 position) {
        return testForCollision(position, 0.0);
    }

    public SimulatedCollisionResult testForCollision(Vec3 position, double margin) {
        short height = testForHeight(position);
        if (height == Constants.UNINITIALIZED) {
            return SimulatedCollisionResult.UNKNOWN;
        }
        return position.y < height + margin ? SimulatedCollisionResult.HIT : SimulatedCollisionResult.MISS;
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
