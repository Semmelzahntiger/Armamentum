package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import util.LogUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Skeleton {
    final Bone[] bones;
    final Part[] parts;
    int[] subTreeEnd;
    Map<BoneId, Integer> index;

    public Skeleton(Builder builder) {

    }





    public static class Builder {
        private Set<BoneId> occupiedBoneIds = new HashSet<>();
        private List<Bone> bones = new ArrayList<>();
        public Builder bone(BoneId id, @Nullable BoneId parent, Vec3 pivot, Transformation3x3 initial) {
            if(occupiedBoneIds.contains(id)) {
                LogUtil.getLogger().warn("Tried to register bone with id thats already registered, ignoring...");
                return this;
            }
            occupiedBoneIds.add(id);
        }
        public Builder bone(BoneId id, @Nullable BoneId parent, Vec3 pivot) {
            if(occupiedBoneIds.contains(id)) {
                LogUtil.getLogger().warn("Tried to register bone with id thats already registered, ignoring...");
                return this;
            }
            occupiedBoneIds.add(id);
        }
        public Builder box(BoneId boneId, AABB localAABB) {

        }
        public Skeleton build() {
            return new Skeleton(this);
        }

    }

}
