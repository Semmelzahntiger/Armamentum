package com.semmelzahntiger.arms.helper;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MiscUtil {
    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker
    ) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }
    public static Vec3i invertVec3i(Vec3i vec) {
        return new Vec3i(-vec.getX(), -vec.getY(), -vec.getZ());
    }

    /**
     * Rotates a North-oriented offset around the Y axis onto the given horizontal facing.
     * North is the identity orientation; Y is preserved so vertical (wall) offsets stay vertical.
     * Matches Minecraft's {@code Rotation.CLOCKWISE_90/180/270} mapping.
     */
    public static Vec3i rotateOffsetToFacing(Vec3i vec, Direction facing) {
        int x = vec.getX(), y = vec.getY(), z = vec.getZ();
        return switch (facing) {
            case EAST  -> new Vec3i(-z, y,  x);   // CW 90
            case SOUTH -> new Vec3i(-x, y, -z);   // 180
            case WEST  -> new Vec3i( z, y, -x);   // CW 270
            default    -> new Vec3i( x, y,  z);   // NORTH / identity
        };
    }
    public static Vec3 invertVec3(Vec3 vec) {
        return new Vec3(-vec.x(), -vec.y(), -vec.z());
    }
}
