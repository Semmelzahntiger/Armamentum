package com.semmelzahntiger.arms.helper;

import com.semmelzahntiger.arms.core.obb.Pose;
import net.minecraft.network.syncher.EntityDataSerializer;

public class PoseUtil {

    public static final EntityDataSerializer<Pose.Snapshot> POSE_SERIALIZER = EntityDataSerializer.forValueType(Pose.Snapshot.STREAM_CODEC);

}
