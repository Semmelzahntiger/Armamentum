package com.semmelzahntiger.arms.core.projectile;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationManager extends SavedData {
    public static final String SAVED_DATA_ID = "simulation_manager";

    public static final SavedData.Factory<SimulationManager> FACTORY =
            new SavedData.Factory<>(SimulationManager::new, SimulationManager::load, null);
    private final ConcurrentHashMap<UUID, SimulatedProjectile> simulatedProjectiles = new ConcurrentHashMap<>();

    public void onTick() {
        for (SimulatedProjectile value : simulatedProjectiles.values()) {
            value.onSimulationTick();
        }
    }
    public void addSimulatedProjectile(UUID uuid, SimulatedProjectile simulatedProjectile) {
        simulatedProjectiles.put(uuid, simulatedProjectile);
    }
    public void removeSimulatedProjectile(UUID uuid) {
        simulatedProjectiles.remove(uuid);
    }

    public static SimulationManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, SAVED_DATA_ID);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return null;
    }
    public static SimulationManager load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return null;
    }
}
