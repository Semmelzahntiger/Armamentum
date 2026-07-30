package com.semmelzahntiger.arms.core.misc;

import net.neoforged.bus.api.Event;

public @interface InstanceEventListener {
    Class<? extends Event>[] events();
}
