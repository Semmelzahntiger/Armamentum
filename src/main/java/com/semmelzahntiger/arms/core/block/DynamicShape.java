package com.semmelzahntiger.arms.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

// Allows "easier?" construction of VoxelShapes and contains interaction logic.
public class DynamicShape {
    public static Vec3 CENTER_SPACE_VECTOR = new Vec3(0.5D, 0.5D, 0.5D);

    public VoxelShape finalShape = Shapes.empty();
    public final Map<String,VoxelShape> partShapes = new HashMap<>();
    public final Map<String,String> interactionMap = new HashMap<>();

    public DynamicShape() {

    }
    public DynamicShape(VoxelShape shape) {
        finalShape = shape;
    }
    public DynamicShape addShape(VoxelShape shape, String name, String interaction) {
        construct(shape);
        partShapes.put(name,finalShape);
        interactionMap.put(name,interaction);
        return this;
    }
    public DynamicShape addShape(VoxelShape shape, String name) {
        construct(shape);
        partShapes.put(name,finalShape);
        interactionMap.put(name,"default");
        return this;
    }
    public String getInteraction(String shapeName) {
        return interactionMap.get(shapeName);
    }
    public VoxelShape getShape() {
        return finalShape;
    }
    private void construct(VoxelShape shape) {
        finalShape = Shapes.or(finalShape, shape);
    }
    private void deconstruct(VoxelShape shape) {
        finalShape = Shapes.join(finalShape, shape, BooleanOp.NOT_SECOND);
    }
    public String checkForInteraction(Vec3 interactionVector, BlockPos pos) {
        Vec3 blockSpaceVector = blockSpaceVector(interactionVector, pos);
        final String[] contextID = {"none"};
        partShapes.forEach((name, shape) -> {
            if(isColliding(blockSpaceVector, shape)) {
                String id = interactionMap.get(name) != null ? interactionMap.get(name) : "none";
                contextID[0] = id;
            }
        });
        return contextID[0];
    }
    public static boolean isColliding(@NotNull Vec3 blockSpaceVector, @NotNull VoxelShape partShape) {
        boolean xColliding = partShape.min(Direction.Axis.X) <= blockSpaceVector.x() && partShape.max(Direction.Axis.X) >= blockSpaceVector.x();
        boolean yColliding = partShape.min(Direction.Axis.Y) <= blockSpaceVector.y() && partShape.max(Direction.Axis.Y) >= blockSpaceVector.y();
        boolean zColliding = partShape.min(Direction.Axis.Z) <= blockSpaceVector.z() && partShape.max(Direction.Axis.Z) >= blockSpaceVector.z();
        return xColliding && yColliding && zColliding;
    }
    public static DynamicShape createFull() {
        return new DynamicShape().addShape(Shapes.block(), "block");
    }
    public static DynamicShape createEmpty() {
        return new DynamicShape();
    }
    public DynamicShape sculptShape(VoxelShape shape) {
        deconstruct(shape);
        partShapes.forEach((name, shapes) -> {
            Shapes.join(shapes, shape, BooleanOp.NOT_SECOND);
        });
        return this;
    }
    public static Vec3 blockSpaceVector(Vec3 vector, BlockPos pos) {
        Vec3 blockSpaceVector = vector.subtract(pos.getCenter()).subtract(CENTER_SPACE_VECTOR).add(new Vec3(1,1,1));
        return new Vec3(round(blockSpaceVector.x,2), round(blockSpaceVector.y,2), round(blockSpaceVector.z,2));
    }
    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}


