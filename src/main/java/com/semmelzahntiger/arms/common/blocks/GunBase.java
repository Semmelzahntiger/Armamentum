package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class GunBase<G extends Entity & GunEntity, B extends GunBaseBlockEntity<G>> extends Block implements EntityBlock {
    protected Map<, List<Vec3i>> offsets = new HashMap<>();
    public GunBase(Properties properties) {
        super(properties);
    }

    public abstract Class<G> getGunEntityClass();

    public abstract BiFunction<BlockPos, BlockState, B> getBlockEntityFactory();

    public abstract Function<Level, G> getEntityFactory();

    public int getPlatformLength() {
        return 1;
    }
    public int getPlatformWidth() {
        return 1;
    }
    public boolean canPlaceOnWall() {
        return false;
    }

    protected List<Vec3i> generateOffsets() {
        List<Vec3i> offsets = new ArrayList<>();
        // +X for East
        int width = getPlatformWidth();
        // -Z for North
        int length = getPlatformLength();

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                if(x == 0 && z == 0) continue;
                offsets.add(new Vec3i(x, 0, -z));
            }
        }
        return offsets;
    }


    @Override
    public final @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return getBlockEntityFactory().apply(blockPos, blockState);
    }
    @SuppressWarnings("unchecked")
    public B getGunBaseBlockEntity(Level level, BlockPos blockPos) {
        return (B) level.getBlockEntity(blockPos);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        GunBaseBlockEntity<G> gunBase = getGunBaseBlockEntity(level, pos);
        if(gunBase == null) {
            LogUtil.getLogger().warn("No BlockEntity detected.");
            return;
        }
        G entity = getEntityFactory().apply(level);
        Vec3 currentPos = pos.getCenter();
        Vec3 offsetPos = currentPos.add(entity.getOffsetFromControllerCenter());
        level.addFreshEntity(entity);
        entity.setPos(offsetPos);
        gunBase.setManagedEntity(entity);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        GunBaseBlockEntity<G> gunBaseBlockEntity = getGunBaseBlockEntity(level, pos);
        if(gunBaseBlockEntity != null) {
            gunBaseBlockEntity.onBlockRemove();
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
