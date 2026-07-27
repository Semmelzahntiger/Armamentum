package com.semmelzahntiger.arms.common.skeletons;

import com.semmelzahntiger.arms.core.obb.Bone;
import com.semmelzahntiger.arms.core.obb.BoneId;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import com.semmelzahntiger.arms.helper.MatrixHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

public class TestSkeleton {
    public static final Skeleton TEST_SKELETON = new Skeleton.Builder()
            .bone(new BoneId("TestMain"), null, Vec3.ZERO, new Matrix3f())
            .bone(new BoneId("Child"), new BoneId("TestMain"), new Vec3(0,2,0), new Matrix3f())
            .bone(new BoneId("ChildRotated"), new BoneId("Child"), new Vec3(0,2,0), MatrixHelper.getRotationMatrix(0,45,0))
            .box("TestBox", new BoneId("TestMain"), new AABB(-1, -1, -1, 1,1,1))
            .build();
}
