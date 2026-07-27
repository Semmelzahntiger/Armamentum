package com.semmelzahntiger.arms.core.obb;

import com.semmelzahntiger.arms.core.obb.math.TopoSort;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import util.LogUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Rigid Skeleton of an OBB
 */
public class Skeleton {
    final Bone[] bones;
    final int[] subTreeEnd;
    final Part[][] partArray;
    final Map<BoneId, Integer> idIndexMap;


    public Skeleton(Builder builder) {
        this.bones = builder.sortedBones;
        this.subTreeEnd = builder.subTreeEnd;
        this.partArray = builder.partArray;
        this.idIndexMap = builder.idIndexMap;
    }

    public int getBoneCount() {
        return bones.length;
    }

    public Bone[] getBones() {
        return bones;
    }
    public int[] getSubTreeEnd() {
        return subTreeEnd;
    }
    public int getIndexOfBoneId(BoneId id) {
        return idIndexMap.get(id);
    }
    public Part[] getPartOfBoneId(BoneId id) {
        return partArray[getIndexOfBoneId(id)];
    }

    public static class Builder {
        private final Set<BoneId> occupiedBoneIds = new HashSet<>();
        private final List<Bone> bones = new ArrayList<>();
        private Bone[] sortedBones = null;
        private int[] subTreeEnd = null;
        private Part[][] partArray = null;
        private final Map<BoneId, Integer> idIndexMap = new HashMap<>();
        private final Map<BoneId, List<Part>> parts = new HashMap<>();
        public Builder bone(BoneId id, @Nullable BoneId parent, Vec3 pivot, Matrix3f initial) {
            if(occupiedBoneIds.contains(id)) {
                LogUtil.getLogger().warn("Tried to register bone with id thats already registered, ignoring...");
                return this;
            }
            occupiedBoneIds.add(id);
            Bone bone = new Bone(id, parent, pivot, initial);
            bones.add(bone);
            return this;
        }
        public Builder bone(BoneId id, @Nullable BoneId parent, Vec3 pivot) {
            if(occupiedBoneIds.contains(id)) {
                LogUtil.getLogger().warn("Tried to register bone with id thats already registered, ignoring...");
                return this;
            }
            occupiedBoneIds.add(id);
            Bone bone = new Bone(id, parent, pivot, new Matrix3f());
            bones.add(bone);
            return this;
        }
        public Builder box(String partName, BoneId boneId, AABB localAABB) {
            if(!occupiedBoneIds.contains(boneId)) {
                LogUtil.getLogger().warn("Tried adding Box to non existent Bone, ignoring...");
                return this;
            }
            parts.computeIfAbsent(boneId, k -> new ArrayList<>()).add(new Part(partName, boneId, localAABB));
            return this;
        }
        private Part[][] createPartArray() {
            Part[][] partArray = new Part[sortedBones.length][];
            for (int i = 0; i < sortedBones.length; i++) {
                Bone bone = sortedBones[i];
                List<Part> partList = parts.getOrDefault(bone.id(), List.of());
                partArray[i] = partList.toArray(new Part[partList.size()]);
            }
            return partArray;
        }
        public Skeleton build() {
            TopoSort.SortResult result = TopoSort.sort(bones.toArray(new Bone[0]));
            sortedBones = result.sortedBones();
            subTreeEnd = result.subTreeEnds();
            for (int i = 0; i < sortedBones.length; i++) {
                Bone bone = sortedBones[i];
                idIndexMap.put(bone.id(), i);
            }
            partArray = createPartArray();
            return new Skeleton(this);
        }

    }

}
