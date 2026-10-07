package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.blockentities.base.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import com.semmelzahntiger.arms.core.obb.BoneId;
import com.semmelzahntiger.arms.helper.MatrixHelper;
import com.semmelzahntiger.arms.helper.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import util.LogUtil;

import java.util.List;

import static com.semmelzahntiger.arms.common.blocks.base.GunBaseBlock.FACING;

public class MG08BlockEntity extends GunBaseBlockEntity<MG08> {
    public MG08BlockEntity(BlockPos pos, BlockState blockState, Class<MG08> entityClass, List<Vec3i> offsets) {
        super(BlockEntityRegister.MG_08_BE.get(), pos, blockState, entityClass, offsets);
    }

    @Override
    protected MG08 createGunEntity() {
        return new MG08(EntityRegister.MG08_GUN.get(), getLevel());
    }

    @Override
    protected void initializeHook(Level level, BlockPos blockPos, BlockState blockState) {
        LogUtil.getLogger().info(getManagedEntity().getOBBPose().getWorldRotationOf(new BoneId("mgmain")).toString());
        getManagedEntity().getOBBPose().setRotation(new BoneId("mgmain"),
                MatrixHelper.getRotationMatrix(
                        0,
                        MiscUtil.getDegreesFromRotation(blockState.getValue(FACING)),
                        0));
    }
}
