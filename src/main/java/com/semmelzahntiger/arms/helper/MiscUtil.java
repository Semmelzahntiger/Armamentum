package com.semmelzahntiger.arms.helper;

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
    public static Vec3 invertVec3(Vec3 vec) {
        return new Vec3(-vec.x(), -vec.y(), -vec.z());
    }
}
