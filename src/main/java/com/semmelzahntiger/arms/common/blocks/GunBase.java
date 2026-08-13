package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.blockentities.GunBaseExtensionBlockEntity;
import com.semmelzahntiger.arms.common.entities.GunEntity;
import com.semmelzahntiger.arms.helper.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class GunBase<G extends Entity & GunEntity, B extends GunBaseBlockEntity<G>> extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected final Map<Direction, List<Vec3i>> offsets;

    public GunBase(Properties properties) {
        super(properties);
        this.offsets = buildOffsets();
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    public abstract @NotNull Class<G> getGunEntityClass();

    public abstract @NotNull BiFunction<BlockPos, BlockState, B> getBlockEntityFactory();

    // Can return null if multi block is 1x1
    public abstract @Nullable GunBaseExtension<G> getExtensionType();

    public abstract @NotNull Function<Level, G> getEntityFactory();

    public int getPlatformLength() {
        return 1;
    }
    public int getPlatformWidth() {
        return 1;
    }
    public int getPlatformHeight() {
        return 1;
    }
    protected List<Vec3i> generateOffsets() {
        List<Vec3i> offsets = new ArrayList<>();
        // +X for East
        int width = getPlatformWidth();
        // -Z for North
        int length = getPlatformLength();
        int height = getPlatformHeight();

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                for (int y = 0; y < height; y++) {
                    if(x == 0 && y == 0&&z == 0) continue;
                    offsets.add(new Vec3i(x, y, -z));
                }
            }
        }
        return offsets;
    }

    protected Map<Direction, List<Vec3i>> buildOffsets() {
        Map<Direction, List<Vec3i>> map = new HashMap<>();
        List<Vec3i> northOffsets = generateOffsets();
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            map.put(facing, rotateAll(northOffsets, facing));
        }
        return map;
    }

    private static List<Vec3i> rotateAll(List<Vec3i> base, Direction facing) {
        List<Vec3i> out = new ArrayList<>(base.size());
        for (Vec3i v : base) {
            out.add(MiscUtil.rotateOffsetToFacing(v, facing));
        }
        return out;
    }

    /** The structure cell offsets for the orientation this state was placed with. */
    public List<Vec3i> getActiveOffsets(BlockState state) {
        return offsets.get(state.getValue(FACING));
    }

    protected boolean areOffsetsFree(Level level, BlockPos pos, BlockState state) {
        for (Vec3i offset : getActiveOffsets(state)) {
            if (!level.getBlockState(pos.offset(offset)).canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection());
        return areOffsetsFree(context.getLevel(), context.getClickedPos(), state) ? state : null;
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
        placeStructure(level, pos, state, gunBase);
    }
    @SuppressWarnings("unchecked")
    protected void placeStructure(Level level, BlockPos pos, BlockState state, GunBaseBlockEntity<G> blockEntity) {
        GunBaseExtension<G> extensionType = getExtensionType();
        if(extensionType == null) {
            return;
        }
        BlockState extensionBlockState = extensionType.defaultBlockState();
        for (Vec3i offset : getActiveOffsets(state)) {
            BlockPos offsetPos = pos.offset(offset);
            level.setBlockAndUpdate(offsetPos, extensionBlockState);
            GunBaseExtensionBlockEntity<G> extensionBlockEntity = (GunBaseExtensionBlockEntity<G>) level.getBlockEntity(offsetPos);
            extensionBlockEntity.setLinkedBlockEntity(blockEntity);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        GunBaseBlockEntity<G> gunBaseBlockEntity = getGunBaseBlockEntity(level, pos);
        if(gunBaseBlockEntity != null) {
            gunBaseBlockEntity.onBlockRemove();
        }
        GunBaseExtension<G> extensionType = getExtensionType();
        if(extensionType != null) {
            for (Vec3i activeOffset : getActiveOffsets(state)) {
                BlockPos extensionPos = pos.offset(activeOffset);
                if(!level.getBlockState(extensionPos).is(extensionType)) {
                    continue;
                }
                level.removeBlock(extensionPos, false);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
