package com.semmelzahntiger.arms.core.obb;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopoSort {
    public static SortResult sort(final Bone[] boneArray) {
        Map<Bone, List<Bone>> childrenMap = new HashMap<>();
        Map<BoneId, Bone> bones = new HashMap<>();
        for (Bone bone : boneArray) {
            bones.put(bone.id(), bone);
        }

        Bone root = null;
        for (Bone bone : boneArray) {
            if(bone.parent() == null) {
                root = bone;
            }
            else {
                childrenMap.computeIfAbsent(bones.get(bone.parent()),
                        k -> new ArrayList<>()).add(bone);
            }
        }
        List<Bone> result = new ArrayList<>(boneArray.length);
        Map<Bone, Integer> endIndexOf = new HashMap<>();
        Deque<Bone> stack = new ArrayDeque<>();
        Deque<Integer> childIndexStack = new ArrayDeque<>();
        result.add(root);
        stack.push(root);
        childIndexStack.push(0);

        while (!stack.isEmpty()) {
            Bone current = stack.peek();
            int ci = childIndexStack.pop();
            List<Bone> children = childrenMap.getOrDefault(current,new ArrayList<>());

            if(ci < children.size()) {
                childIndexStack.push(ci + 1);
                Bone child = children.get(ci);
                result.add(child);
                stack.push(child);
                childIndexStack.push(0);
            }
            else {
                endIndexOf.put(current, result.size());
                stack.pop();
            }
        }
        Bone[] sortedArray = result.toArray(new Bone[0]);
        int[] subTreeEnds = new int[sortedArray.length];
        for (int i = 0; i < sortedArray.length; i++) {
            subTreeEnds[i] = endIndexOf.get(sortedArray[i]);
        }
        return new SortResult(sortedArray, subTreeEnds);
    }
    public record SortResult(Bone[] sortedBones, int[] subTreeEnds) {}
}
