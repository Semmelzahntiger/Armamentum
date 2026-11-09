package com.semmelzahntiger.arms.core.simulate;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.UUID;

public class SimulateEvents {
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
}
