package com.semmelzahntiger.arms.common.skeletons;

import com.semmelzahntiger.arms.core.obb.BoneId;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import com.semmelzahntiger.arms.helper.MatrixHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

public class Skeletons {
    public static final Skeleton TEST_SKELETON;
    static {
        BoneId main = new BoneId("TestMain");
        BoneId child = new BoneId("Child");
        BoneId childRotated = new BoneId("ChildRotated");
        BoneId childRotatedChild = new BoneId("ChildRotatedChild");
        TEST_SKELETON = new Skeleton.Builder()
                .bone(main, null, Vec3.ZERO, new Matrix3f())
                .bone(child, main, new Vec3(0,2,0), new Matrix3f())
                .bone(childRotated, child, new Vec3(0,2,0), MatrixHelper.getRotationMatrix(0,45,0))
                .bone(childRotatedChild, childRotated, new Vec3(2,0,0), MatrixHelper.getRotationMatrix(0,0,0))
                .box("TestBox", main, new AABB(-1, -1, -1, 1,1,1))
                .box("TestBox2",child, new AABB(-1, -1, -1, 1,1,1))
                .box("TestBox3",childRotated, new AABB(-1, -1, -1, 1,1,1))
                .box("TestBox4", childRotatedChild, new AABB(-1, -1, -1, 1,1,1))
                .build();
    }
}
