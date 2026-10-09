package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface OBBEntity {
    Pose getOBBPose();
    Skeleton getSkeleton();
    default InteractionResult interactPart(Vec3 position, Player player, InteractionHand interactionHand) {
        Part part = getOBBPose().anyIntersection(position, player.getEyePosition(), player.getLookAngle().scale(player.entityInteractionRange()));
        if(part != null) {
            return onPartInteraction(part,player, interactionHand);
        }
        return InteractionResult.FAIL;
    }
    default InteractionResult onPartInteraction(Part part, Player player, InteractionHand hand) {
        return InteractionResult.FAIL;
    }
}
