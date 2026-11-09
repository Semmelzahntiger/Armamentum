package com.semmelzahntiger.arms.core.simulate;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import util.LogUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages Projectile Simulation so they still fly outside loaded Chunks.
 */
public class SimulationManager extends SavedData {
    public static final String SAVED_DATA_ID = "simulation_manager";

    public static final SavedData.Factory<SimulationManager> FACTORY =
            new SavedData.Factory<>(SimulationManager::new, SimulationManager::load, null);
    private final ConcurrentHashMap<UUID, SimulatedProjectile> simulatedProjectiles = new ConcurrentHashMap<>();

    public void simulateTick(ServerLevel level) {
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
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, SimulatedProjectile> entry : simulatedProjectiles.entrySet()) {
            SimulatedProjectile value = entry.getValue();
            CompoundTag simulationTag = value.serializeNBT(provider);
            ResourceLocation location = SimulationRegistry.getRegistryLocation(value.getClass());
            simulationTag.putString("simulation_type", location.toString());
            simulationTag.putUUID("simulation_uuid", entry.getKey());
            list.add(simulationTag);
        }
        compoundTag.put("simulated_projectiles", list);
        return compoundTag;
    }
    public static SimulationManager load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        SimulationManager manager = new SimulationManager();
        ListTag list = (ListTag) compoundTag.get("simulated_projectiles");
        if(list == null) {
            return manager;
        }
        for (Tag tag : list) {
            CompoundTag simulationTag = (CompoundTag) tag;
            String stringLocation = simulationTag.getString("simulation_type");
            UUID simulationUUID = simulationTag.getUUID("simulation_uuid");
            ResourceLocation resourceLocation = ResourceLocation.tryParse(stringLocation);
            SimulatedProjectile projectile = SimulationRegistry.getSimulationInstance(resourceLocation);
            projectile.deserializeNBT(provider, simulationTag);
            manager.addSimulatedProjectile(simulationUUID, projectile);
        }
        return manager;
    }
}
