package com.semmelzahntiger.arms.client.input;

import com.semmelzahntiger.arms.Arms;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class InputPriorityProfiles {
    public static final String CATEGORY = "key.categories.arms";

    public static final KeyMapping DEBUG_TOGGLE = new KeyMapping("key.arms.debug_toggle_profile", GLFW.GLFW_KEY_K, CATEGORY);

    public static final KeyMapping TEST_LEFT = new KeyMapping("key.arms.test_left", GLFW.GLFW_KEY_A, CATEGORY);
    public static final InputPriorityProfile TEST = InputPriorityProfileRegistry.register(
            new InputPriorityProfile(ResourceLocation.fromNamespaceAndPath(Arms.MODID, "test"), TEST_LEFT));

    public static void init() {}
}
