package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Pose {
    private static final float EPSILON = 1e-8f;
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

    public void setTranslation(BoneId boneIndex, Vec3 translation) {
        int idx = skeleton.getIndexOfBoneId(boneIndex);
        translations[idx] = translation;
        cacheDirty = true;
    }

    public void ensureCache() {
        if (cacheDirty) {
            calculateCache();
            cacheDirty = false;
        }
    }

    private void calculateCache() {
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
    public Matrix3f getWorldRotationOf(BoneId boneId) {
        ensureCache();
        int idx = skeleton.getIndexOfBoneId(boneId);
        return worldRotationCache[idx];
    }
    public Vec3 getWorldPositionOf(BoneId boneId) {
        ensureCache();
        int idx = skeleton.getIndexOfBoneId(boneId);
        return worldPositionCache[idx];
    }
    public Vec3 getActualWorldPositionOf(BoneId boneId, Vec3 worldPos) {
        ensureCache();
        int idx = skeleton.getIndexOfBoneId(boneId);
        return worldPositionCache[idx].add(worldPos);
    }
    public Vec3 toLocalSpaceOf(BoneId boneId, Vec3 obbWorldPos, Vec3 transformableWorldPos) {
        Matrix3f rotation = getWorldRotationOf(boneId);
        Matrix3f inv = new Matrix3f(rotation).transpose();
        Vec3 position = getWorldPositionOf(boneId);
        position = position.add(obbWorldPos);
        transformableWorldPos = transformableWorldPos.subtract(position);
        Vector3f vec3f = transformableWorldPos.toVector3f();
        inv.transform(vec3f);
        return new Vec3(vec3f.x, vec3f.y, vec3f.z);
    }
    public @Nullable Part intersects(BoneId boneId, Vec3 obbWorldPos, Vec3 transformableWorldPos1, Vec3 transformableWorldPos2) {
        Vec3 localPos1 = toLocalSpaceOf(boneId, obbWorldPos, transformableWorldPos1);
        Vec3 localPos2 = toLocalSpaceOf(boneId, obbWorldPos, transformableWorldPos2);
        List<Part> parts = skeleton.getPartOfBoneId(boneId);
        Part hitPart = null;
        HitResult cachedResult = null;
        for (Part part : parts) {
            HitResult result = intersectsWithBox(localPos1, localPos2, part);
            if (cachedResult != null) {
                boolean nearer = cachedResult.tMin() < result.tMin();
                if (nearer) {
                    hitPart = part;
                    cachedResult = result;
                }
            } else {
                hitPart = part;
                cachedResult = result;
            }
        }
        return hitPart;
    }
    public Part anyIntersection(Vec3 obbWorldPos, Vec3 transformableWorldPos1, Vec3 transformableWorldPos2) {

    }
    private HitResult intersectsWithBox(Vec3 origin, Vec3 dir, Part part) {
        AABB aabb = part.localAABB();
        double t1x = (aabb.minX - origin.x()) / (dir.x() + EPSILON);
        double t2x = (aabb.maxX - origin.x()) / (dir.x() + EPSILON);
        double tMinX = Math.min(t1x, t2x);
        double tMaxX = Math.max(t1x, t2x);
        double t1y = (aabb.minY -  origin.y()) / (dir.y() + EPSILON);
        double t2y = (aabb.maxY - origin.y()) / (dir.y() + EPSILON);
        double tMinY = Math.min(t1y, t2y);
        double tMaxY = Math.max(t1y, t2y);
        double t1z = (aabb.minZ - origin.z()) / (dir.z() + EPSILON);
        double t2z = (aabb.maxZ - origin.z()) / (dir.z() + EPSILON);
        double tMinZ = Math.min(t1z, t2z);
        double tMaxZ = Math.max(t1z, t2z);

        double tMin = Math.min(Math.min(tMinX, tMinY), tMinZ);
        double tMax = Math.max(Math.max(tMaxX, tMaxY), tMaxZ);

        if(tMax < 0) {
            return new HitResult(false, 0);
        }
        else return new HitResult(!(tMin > tMax), tMin);
    }
    public record HitResult(boolean hit, double tMin) {}

}
