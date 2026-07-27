package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;

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
        for (int i = 0; i < rotations.length; i++) {
            rotations[i] = new Matrix3f();
        }
        translations = new Vec3[count];
        for (int i = 0; i < translations.length; i++) {
            translations[i] = Vec3.ZERO;
        }
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
    public void addRotation(BoneId boneId, Matrix3f rotation) {
        int idx = skeleton.getIndexOfBoneId(boneId);
        rotations[idx] = rotations[idx].mul(rotation);
        cacheDirty = true;
    }
    public void addTranslation(BoneId boneIndex, Vec3 translation) {
        int idx = skeleton.getIndexOfBoneId(boneIndex);
        translations[idx] = translations[idx].add(translation);
        cacheDirty = true;
    }

    public void verifyCache() {
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
    public Matrix3f[] getWorldRotationCache() {
        return worldRotationCache;
    }
    public Vec3[] getWorldPositionCache() {
        return worldPositionCache;
    }
    public Matrix3f getWorldRotationOf(BoneId boneId) {
        return getWorldRotationOf(skeleton.getIndexOfBoneId(boneId));
    }
    public Matrix3f getWorldRotationOf(int boneIndex) {
        verifyCache();
        return worldRotationCache[boneIndex];
    }
    public Vec3 getWorldPositionOf(BoneId boneId) {
       return getWorldPositionOf(skeleton.getIndexOfBoneId(boneId));
    }
    public Vec3 getWorldPositionOf(int boneIndex) {
        verifyCache();
        return worldPositionCache[boneIndex];
    }

    public Vec3 getActualWorldPositionOf(BoneId boneId, Vec3 worldPos) {
       return getActualWorldPositionOf(skeleton.getIndexOfBoneId(boneId), worldPos);
    }
    public Vec3 getActualWorldPositionOf(int boneIndex, Vec3 worldPos) {
        verifyCache();
        return worldPositionCache[boneIndex].add(worldPos);
    }
    public Vec3 toLocalSpaceOf(BoneId boneId, Vec3 obbWorldPos, Vec3 transformableWorldPos) {
        return toLocalSpaceOf(skeleton.getIndexOfBoneId(boneId), obbWorldPos, transformableWorldPos);
    }
    public Vec3 toLocalSpaceOf(int boneIndex, Vec3 obbWorldPos, Vec3 transformableWorldPos) {
        Matrix3f rotation = getWorldRotationOf(boneIndex);
        Matrix3f inv = new Matrix3f(rotation).transpose();
        Vec3 position = getWorldPositionOf(boneIndex);
        position = position.add(obbWorldPos);
        transformableWorldPos = transformableWorldPos.subtract(position);
        Vector3f vec3f = transformableWorldPos.toVector3f();
        inv.transform(vec3f);
        return new Vec3(vec3f.x, vec3f.y, vec3f.z);
    }
    public @Nullable Part intersects(BoneId boneId, Vec3 obbWorldPos, Vec3 transformableWorldPos1, Vec3 transformableWorldPos2, HitResultCache cache) {
        return intersects(skeleton.getIndexOfBoneId(boneId), obbWorldPos, transformableWorldPos1, transformableWorldPos2, cache);
    }
    public @Nullable Part intersects(int boneIndex, @NotNull Vec3 obbWorldPos,
                                     @NotNull Vec3 transformableWorldPos1, @NotNull Vec3 transformableWorldPos2,
                                     @Nullable HitResultCache cache) {
        Vec3 localOrigin = toLocalSpaceOf(boneIndex, obbWorldPos, transformableWorldPos1);
        Vec3 localEnd    = toLocalSpaceOf(boneIndex, obbWorldPos, transformableWorldPos2);
        Vec3 dir = localEnd.subtract(localOrigin); // actual ray direction

        Part[] boneParts = skeleton.partArray[boneIndex];
        Part hitPart = null;
        double bestT = Double.MAX_VALUE;

        for (Part part : boneParts) {
            HitResult result = intersectsWithBox(localOrigin, dir, part);
            if (result.hit() && result.tMin() < bestT) { // only real hits, keep the nearest
                bestT = result.tMin();
                hitPart = part;
            }
        }

        if (cache != null && hitPart != null) {
            cache.newCandidate(hitPart, bestT);
        }
        return hitPart;
    }
    public int getBoneCount() {
        return skeleton.getBoneCount();
    }
    public @Nullable Part anyIntersection(Vec3 obbWorldPos, Vec3 transformableWorldPos1, Vec3 transformableWorldPos2) {
        Bone[] bones = skeleton.getBones();
        HitResultCache cache = new HitResultCache();
        for (int i = 0; i < bones.length; i++) {
            intersects(i, obbWorldPos, transformableWorldPos1, transformableWorldPos2, cache);
        }
        return cache.hitPart;
    }
    private @NotNull HitResult intersectsWithBox(Vec3 origin, Vec3 dir, Part part) {
        AABB aabb = part.localAABB();

        double tNear = Double.NEGATIVE_INFINITY;
        double tFar  = Double.POSITIVE_INFINITY;

        double[] o   = { origin.x(), origin.y(), origin.z() };
        double[] d   = { dir.x(),    dir.y(),    dir.z()    };
        double[] min = { aabb.minX,  aabb.minY,  aabb.minZ  };
        double[] max = { aabb.maxX,  aabb.maxY,  aabb.maxZ  };

        for (int a = 0; a < 3; a++) {
            if (Math.abs(d[a]) < EPSILON) {
                if (o[a] < min[a] || o[a] > max[a]) return new HitResult(false, 0);
            } else {
                double inv = 1.0 / d[a];
                double t1 = (min[a] - o[a]) * inv;
                double t2 = (max[a] - o[a]) * inv;
                if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
                if (t1 > tNear) tNear = t1;
                if (t2 < tFar)  tFar  = t2;
                if (tNear > tFar) return new HitResult(false, 0);
            }
        }
        // No Hit if the box is behind the ray or if the box is farther away than the ray segment
        if (tFar < 0 || tNear > 1) return new HitResult(false, 0);
        return new HitResult(true, tNear);
    }

    public AABB getEncapsulatingAABB(Vec3 obbWorldPos) {
        verifyCache();

        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        Bone[] bones = skeleton.getBones();
        for (int i = 0; i < bones.length; i++) {
            Matrix3f rot = worldRotationCache[i];

            // Absolute-valued rotation: projects half-extents onto world axes without cancellation.
            Matrix3f absRot = new Matrix3f(
                    Math.abs(rot.m00), Math.abs(rot.m01), Math.abs(rot.m02),
                    Math.abs(rot.m10), Math.abs(rot.m11), Math.abs(rot.m12),
                    Math.abs(rot.m20), Math.abs(rot.m21), Math.abs(rot.m22)
            );

            Vec3 bonePos = worldPositionCache[i].add(obbWorldPos); // fold in rig placement

            for (Part part : skeleton.partArray[i]) {
                AABB box = part.localAABB();

                // local box as center + half-extents
                Vector3f c = new Vector3f(
                        (float) ((box.minX + box.maxX) * 0.5),
                        (float) ((box.minY + box.maxY) * 0.5),
                        (float) ((box.minZ + box.maxZ) * 0.5)
                );
                Vector3f h = new Vector3f(
                        (float) ((box.maxX - box.minX) * 0.5),
                        (float) ((box.maxY - box.minY) * 0.5),
                        (float) ((box.maxZ - box.minZ) * 0.5)
                );

                // world center of this part's box
                Vector3f worldCenter = rot.transform(new Vector3f(c));
                double cx = worldCenter.x + bonePos.x;
                double cy = worldCenter.y + bonePos.y;
                double cz = worldCenter.z + bonePos.z;

                // world half-extents via the abs-matrix trick
                Vector3f worldHalf = absRot.transform(new Vector3f(h));

                minX = Math.min(minX, cx - worldHalf.x);  maxX = Math.max(maxX, cx + worldHalf.x);
                minY = Math.min(minY, cy - worldHalf.y);  maxY = Math.max(maxY, cy + worldHalf.y);
                minZ = Math.min(minZ, cz - worldHalf.z);  maxZ = Math.max(maxZ, cz + worldHalf.z);
            }
        }

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }


    public record HitResult(boolean hit, double tMin) {}
    public static class HitResultCache {
        public Part hitPart = null;
        public double tMin = Double.MAX_VALUE;

        public void newCandidate(Part part, double newTMin) {
            if(newTMin < tMin) {
                hitPart = part;
                tMin = newTMin;
            }
        }
    }

}
