package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.BEMountedEntity;
import com.semmelzahntiger.arms.common.entities.BEMountedGun;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class BaseBlockEntity extends BlockEntity {
    private boolean loadLink = false;
    private UUID loadEntityUUID = null;
    private boolean powered = false;
    private Entity mountedEntity = null;
    public BaseBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegister.BASE_BE.get(), pos, blockState);
    }

    public <E extends Entity & BEMountedEntity> void setMountedEntity(E mountedEntity, Vec3 offsetFromCenter) {
        this.mountedEntity = mountedEntity;
        mountedEntity.setOwnerPos(getBlockPos());
        if(getLevel() == null) {
            return;
        }
        getLevel().addFreshEntity(mountedEntity);
        mountedEntity.setPos(getBlockPos().getCenter().add(mountedEntity.getInitialOffset()).add(offsetFromCenter));
    }
    protected void setAlreadyPresentEntity(Entity mountedEntity) {
        this.mountedEntity = mountedEntity;
    }
    public void unmountEntity() {
        if(mountedEntity != null) {
            mountedEntity.discard();
        }
        mountedEntity = null;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        BEMountedEntity mounted = (BEMountedEntity) mountedEntity;
        if(mounted != null) {
            mounted.setState(powered);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BaseBlockEntity blockEntity) {
        if(blockEntity.loadLink && level instanceof ServerLevel serverLevel) {
            blockEntity.setAlreadyPresentEntity(serverLevel.getEntity(blockEntity.loadEntityUUID));
            blockEntity.loadLink = false;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(mountedEntity != null) {
            tag.putBoolean("loadLink", true);
            tag.putUUID("mountedEntityUUID", mountedEntity.getUUID());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("loadLink")) {
            loadLink = tag.getBoolean("loadLink");
            loadEntityUUID = tag.getUUID("mountedEntityUUID");
        }
    }
}
