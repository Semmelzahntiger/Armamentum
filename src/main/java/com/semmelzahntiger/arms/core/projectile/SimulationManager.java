package com.semmelzahntiger.arms.core.projectile;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import util.LogUtil;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationManager extends SavedData {
    public static final String SAVED_DATA_ID = "simulation_manager";

    public static final SavedData.Factory<SimulationManager> FACTORY =
            new SavedData.Factory<>(SimulationManager::new, SimulationManager::load, null);
    private final ConcurrentHashMap<UUID, SimulatedProjectile> simulatedProjectiles = new ConcurrentHashMap<>();

    public void onTick(ServerLevel level) {
        Iterator<SimulatedProjectile> iterator = simulatedProjectiles.values().iterator();
        while(iterator.hasNext()) {
            SimulatedProjectile projectile = iterator.next();
            projectile.onSimulationTick(level);
            if(projectile.shouldReassemble()) {
                iterator.remove();
                reassembleEntity(level, projectile);
            }
        }
    }
    private void reassembleEntity(ServerLevel level, SimulatedProjectile projectile) {
        CompoundTag tag = projectile.getProjectileTag();
        Vec3 reassemblyPosition = projectile.getReassemblyPosition();
        Vec3 reassemblyDeltaMovement = projectile.getDeltaMovement();
        Entity reassembledEntity = EntityType.loadEntityRecursive(tag, level, entity -> {
            entity.setPos(reassemblyPosition);
            entity.setDeltaMovement(reassemblyDeltaMovement);
            return entity;
        });
        if(reassembledEntity == null) {
            LogUtil.getLogger().error("Couldn't properly reassemble Entity");
            return;
        }
        if(!level.tryAddFreshEntityWithPassengers(reassembledEntity)) {
            LogUtil.getLogger().error("Couldn't add reassembled Entity to Level");
        }
    }
    public void addSimulatedProjectile(UUID uuid, SimulatedProjectile simulatedProjectile) {
        simulatedProjectiles.put(uuid, simulatedProjectile);
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
