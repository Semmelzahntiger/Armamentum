package com.semmelzahntiger.arms.core.cts;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface CTSEntity<T extends Entity, D extends CTSData<T>> {
    @NotNull Supplier<D> getDataInstance();
    @NotNull T getEntity();
    default void update() {
        updateData(getEntity());
    }
    void updateData(@NotNull T entity);
}
