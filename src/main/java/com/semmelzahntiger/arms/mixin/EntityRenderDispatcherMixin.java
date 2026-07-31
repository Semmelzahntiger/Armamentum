package com.semmelzahntiger.arms.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.semmelzahntiger.arms.core.obb.OBBEntity;
import com.semmelzahntiger.arms.core.obb.Part;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import util.LogUtil;

/**
 * Injects the OBB System into the F3+B hitbox rendering
 */
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderHitbox", at = @At("HEAD"))
    private static void renderHitbox(PoseStack poseStack, VertexConsumer buffer, Entity entity, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (entity instanceof OBBEntity obbEntity) {
            float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
            Pose pose = obbEntity.getOBBPose();
            Skeleton skeleton = obbEntity.getSkeleton();
            int boneCount = pose.getBoneCount();

            poseStack.pushPose();
            for (int i = 0; i < boneCount; i++) {
                Vec3 bonePos = pose.getWorldPositionOf(i);
                Matrix3f rotation = pose.getWorldRotationOf(i);

                Matrix4f transformation = new Matrix4f().set(rotation);
                transformation.setTranslation((float) bonePos.x, (float) bonePos.y, (float) bonePos.z);

                Part[] parts = skeleton.getPartOfIndex(i);
                poseStack.pushPose();
                poseStack.mulPose(transformation);
                for (Part part : parts) {
                    LevelRenderer.renderLineBox(poseStack, buffer, part.localAABB(), red, green, blue, alpha);
                }
                renderCoordinateBoxes(poseStack, buffer);
            }
            poseStack.popPose();
            poseStack.pushPose();
            renderCoordinateBoxes(poseStack, buffer);
        }
    }

    private static void renderCoordinateBoxes(PoseStack poseStack, VertexConsumer buffer) {
        LevelRenderer.renderLineBox(poseStack, buffer, new AABB(-0.05, -0.05, -0.05, 0.05, 0.05, 0.05), 0.3f, 0.3f, 0.3f, 0.3f);
        LevelRenderer.renderLineBox(poseStack, buffer, new AABB(-0.001, -0.001, -0.001, 3, 0.001, 0.001), 1.0f, 0.0f, 0.0f, 0.3f);
        LevelRenderer.renderLineBox(poseStack, buffer, new AABB(-0.001, -0.001, -0.001, 0.001, 3, 0.001), 0.0f, 1.0f, 0.0f, 0.3f);
        LevelRenderer.renderLineBox(poseStack, buffer, new AABB(-0.001, -0.001, -0.001, 0.001, 0.001, 3), 0.0f, 0.0f, 1.0f, 0.3f);
        poseStack.popPose();
    }
}
