package com.semmelzahntiger.arms.helper;

import com.semmelzahntiger.arms.core.obb.Pose;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MiscUtil {

    public static final List<Direction> dirList = List.of(Direction.NORTH, Direction.EAST,Direction.SOUTH, Direction.WEST);
    public static final Map<Direction, Integer> degreesList = Map.of(Direction.NORTH, 0, Direction.EAST, 90, Direction.SOUTH, 180, Direction.WEST, 270);
    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker
    ) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }
    public static Vec3i invertVec3i(Vec3i vec) {
        return new Vec3i(-vec.getX(), -vec.getY(), -vec.getZ());
    }

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

    /**
     * Returns the desired direction based off of the Block's view. E.g. An East oriented Block's south is the Level's West.
     * Bs method lowkey
     * @param blockDirection Direction the Block is facing
     * @param desiredDirection Direction to be rotated
     * @return Rotated Direction
     */
    public static Direction rotateHorizontalDirectionByBlockDirection(Direction blockDirection, Direction desiredDirection) {
        int blockIdx = dirList.indexOf(blockDirection);
        if(blockIdx == -1) {
            return desiredDirection;
        }
        int desiredIdx = dirList.indexOf(desiredDirection);
        return dirList.get((blockIdx + desiredIdx) % dirList.size());
    }
    public static int getDegreesFromRotation(Direction direction) {
        return degreesList.getOrDefault(direction, 0);
    }
}
