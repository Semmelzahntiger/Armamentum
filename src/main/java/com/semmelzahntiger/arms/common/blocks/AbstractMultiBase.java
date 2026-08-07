package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.BaseBlockEntityExtension;
import com.semmelzahntiger.arms.common.register.BlockRegister;
import com.semmelzahntiger.arms.helper.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMultiBase extends AbstractBase {

    protected final List<Vec3i> offsets;
    public AbstractMultiBase(Properties properties) {
        super(properties);
        offsets = createOffsets();
    }

    public List<Vec3i> createOffsets() {
        List<Vec3i> offsetList = new ArrayList<>();
        int size = getPlatformSize();
        // For even numbers, shift min by +1 one to correct out the "0" coordinate
        int shift = size % 2 == 0 ? 1 : 0;
        int min = (int) (-Math.floor((double) size /2) + shift);
        int max = (int) Math.floor((double) size/2);

        for (int x = min; x <= max; x++) {
            for (int z = min; z <= max; z++) {
                if(x == 0 && z == 0) {
                    continue;
                }
                offsetList.add(new Vec3i(x, 0, z));
            }
        }
        return offsetList;
    }
    public List<Vec3i> getOffsets() {
        return offsets;
    }

    public boolean validate(Level level, BlockPos pos) {
        for (Vec3i offset : offsets) {
            BlockState state = level.getBlockState(pos.offset(offset));
            if(!(state.getBlock() instanceof BaseExtension)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        for (Vec3i offset : getOffsets()) {
            BlockPos offsetPos = pos.offset(offset);
            level.setBlock(offsetPos, BlockRegister.EXTENSION_BLOCK.get().defaultBlockState(), 2);
            BlockEntity be = level.getBlockEntity(offsetPos);
            if(be instanceof BaseBlockEntityExtension baseExt) {
                baseExt.setOwnerPosOffset(MiscUtil.invertVec3i(offset));
            }
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        for (Vec3i offset : getOffsets()) {
            BlockPos offsetPos = pos.offset(offset);
            BlockState oldState = level.getBlockState(offsetPos);
            if(oldState.getBlock() instanceof BaseExtension) {
                level.removeBlock(offsetPos, false);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);

    }
}
