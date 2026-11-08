package com.semmelzahntiger.arms.core.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

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
        entity.discard();
        // Todo: Offload
    }
}
