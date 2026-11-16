package com.semmelzahntiger.arms.core.simulate;

import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChunkHighestBlockCache {

    public static final int DATA_VER = SharedConstants.getCurrentVersion().getDataVersion().getVersion();

    private final LRUMap<ChunkPos, short[]> cache = new LRUMap<>(512);
    private final ConcurrentLinkedQueue<CacheData> cacheQueue = new ConcurrentLinkedQueue<>();

    public void test(ServerLevel level, ChunkPos pos, Vec3 position) {
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
    private record CacheData(ChunkPos pos, short[] heightData) {}
}
