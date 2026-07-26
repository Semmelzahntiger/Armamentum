package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.ArrayDeque;
import java.util.Deque;

public class Pose {
    private final Skeleton skeleton;
    Matrix3f[] rotations;
    Vec3[] translations;
    Matrix3f[] worldRotationCache;
    Vec3[] worldPositionCache;

    private boolean cacheDirty = true;

    public Pose(Skeleton skeleton) {
        this.skeleton = skeleton;
        int count = skeleton.getBoneCount();
        rotations = new Matrix3f[count];
        translations = new Vec3[count];
        worldRotationCache = new Matrix3f[count];
        worldPositionCache = new Vec3[count];
    }

    public void setRotation(BoneId boneId, Matrix3f rotation) {
        int idx = skeleton.getIndexOfBoneId(boneId);
        rotations[idx] = rotation;
        cacheDirty = true;
    }

    public void setTranslation(int boneIndex, Vec3 translation) {
        translations[boneIndex] = translation;
        cacheDirty = true;
    }

    public void ensureCache() {
        if (cacheDirty) {
            calculateCache();
            cacheDirty = false;
        }
    }

    public void calculateCache() {
        Bone[] bones = skeleton.getBones();
        int[] indexEnds = skeleton.getSubTreeEnd();
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < bones.length; i++) {
            while (!stack.isEmpty() && indexEnds[stack.peek()] <= i) {
                stack.pop();
            }
            Matrix3f localRot = new Matrix3f(bones[i].initialTransformation()).mul(rotations[i]);
            Vec3 localOffset = new Vec3(bones[i].distFromParent().toVector3f()).add(translations[i]);

            if (stack.isEmpty()) {
                worldRotationCache[i] = localRot;
                worldPositionCache[i] = localOffset;
            } else {
                int p = stack.peek();
                worldRotationCache[i] = new Matrix3f(worldRotationCache[p]).mul(localRot);
                Vector3f rotatedOffset = worldRotationCache[p].transform(localOffset.toVector3f());
                worldPositionCache[i] = new Vec3(rotatedOffset).add(worldPositionCache[p]);
            }
            stack.push(i);
        }
    }
    public Matrix3f[] getWorldRotation() {
        return worldRotationCache;
    }
    public Vec3[] getWorldPosition() {
        return worldPositionCache;
    }
}
