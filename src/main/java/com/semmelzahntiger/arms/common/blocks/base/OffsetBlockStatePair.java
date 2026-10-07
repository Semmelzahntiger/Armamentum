package com.semmelzahntiger.arms.common.blocks.base;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public record OffsetBlockStatePair(Vec3i offset, BlockState state) {
}
